package com.green.glampick.repository;

import com.green.glampick.common.Role;
import com.green.glampick.entity.OwnerEntity;
import com.green.glampick.dto.object.owner.GetCancelDto;
import com.green.glampick.dto.object.owner.GetRevenue;
import com.green.glampick.dto.object.owner.GetStarHeart;
import com.green.glampick.entity.UserEntity;
import com.green.glampick.repository.resultset.GetDeleteOwnerListResultSet;
import com.green.glampick.repository.resultset.OwnerInfoResultSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public interface OwnerRepository extends JpaRepository<OwnerEntity, Long> {

    OwnerEntity findByOwnerEmail(String ownerEmail);
    OwnerEntity findByOwnerId(Long ownerId);
    OwnerEntity findByOwnerPhone(String ownerPhone);

    OwnerEntity findByOwnerNameAndOwnerPhone(String ownerName, String ownerPhone);
    OwnerEntity findByOwnerEmailAndOwnerName(String ownerEmail, String ownerName);

    @Modifying
    @Transactional
    @Query( "update OwnerEntity oe set oe.role = :role where oe.ownerId = :ownerId" )
    void updateOwnerRole (Role role, Long ownerId);

    @Transactional
    @Query( "select o.ownerEmail AS ownerEmail, o.ownerName AS ownerName" +
            ", o.businessNumber AS businessNumber, o.ownerPhone AS ownerPhone" +
            " from OwnerEntity o where o.ownerId = :ownerId" )
    OwnerInfoResultSet getOwnerInfo(Long ownerId);

    @Query( "select o.ownerId AS ownerId, o.ownerName AS ownerName, o.businessNumber AS businessNumber" +
            ", o.ownerPhone AS ownerPhone from OwnerEntity o where o.activateStatus = 0 " )
    List<GetDeleteOwnerListResultSet> getDeleteOwnerList();

    boolean existsByOwnerPhone(String ownerPhone);
    boolean existsByOwnerEmail(String ownerEmail);



    @Query(
            value =
                    "SELECT A.star_point_avg as starPointAvg" +
                            ", COUNT(A.glamp_id) AS heart " +
                            ", A.owner_id " +
                            "FROM glamping A " +
                            "JOIN glamp_favorite B ON B.glamp_id = A.glamp_id " +
                            "Group BY A.glamp_id " +
                            "HAVING  A.owner_id = :ownerId ",
            nativeQuery = true
    )
    List<GetStarHeart> findByIdStarPoint(long ownerId);


    @Query(
            value =
                    "SELECT SUM(glamp_count) AS totalCount " +
                            "FROM (SELECT COUNT(A.glamp_id) AS glamp_count , A.check_in_date as checkInDate " +
                            "FROM reservation_before A join glamping B ON A.glamp_id = B.glamp_id WHERE B.owner_id = :ownerId " +
                            "AND check_in_date BETWEEN :startDayId AND :endDayId " +
                            "UNION ALL " +
                            "SELECT COUNT(C.glamp_id) AS glamp_count , C.check_in_date as checkInDate " +
                            "FROM reservation_cancel C join glamping B ON C.glamp_id = B.glamp_id WHERE B.owner_id = :ownerId " +
                            "AND check_in_date BETWEEN :startDayId AND :endDayId " +
                            "UNION ALL " +
                            "SELECT COUNT(D.glamp_id) AS glamp_count , D.check_in_date as checkInDate " +
                            "FROM reservation_complete D join glamping B ON D.glamp_id = B.glamp_id WHERE B.owner_id = :ownerId " +
                            "AND check_in_date BETWEEN :startDayId AND :endDayId) AS counts ",
            nativeQuery = true
    )
    Long findTotalCount(@Param("ownerId") long ownerId, @Param("startDayId") String startDayId, @Param("endDayId") String endDayId);

    @Query(
            value =
                    "SELECT SUM(glampId) FROM( " +
                            "SELECT COUNT(A.glamp_id) AS glampId, A.check_in_date AS checkInDate " +
                            "FROM reservation_cancel A join glamping B ON A.glamp_id = B.glamp_id WHERE B.owner_id = :ownerId " +
                            "Group BY check_in_date " +
                            "HAVING check_in_date BETWEEN :startDayId AND :endDayId) AS counts ",
            nativeQuery = true
    )
    Long findCancelCount(@Param("ownerId") long ownerId, @Param("startDayId") String startDayId, @Param("endDayId") String endDayId);

    @Query(
            value =

                    "WITH DateRoom AS ( " +
                            "SELECT DATE(ADDDATE(:startDayId, INTERVAL seq DAY)) AS date, D.room_name, E.owner_id " +
                            "FROM (SELECT @rownum := @rownum + 1 AS seq " +
                            "FROM (SELECT 0 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) a, " +
                            "(SELECT 0 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) b, " +
                            "(SELECT @rownum := -1) r) seq " +
                            "CROSS JOIN room D " +
                            "JOIN glamping E ON D.glamp_id = E.glamp_id " +
                            "WHERE DATE(ADDDATE(:startDayId, INTERVAL seq DAY)) BETWEEN :startDayId AND :endDayId " +
                            "AND E.owner_id = :ownerId) " +
                            ", SubQuery AS (SELECT A.glamp_id, " +
                            "SUM(A.pay_amount) AS pay, " +
                            "DATE(A. check_in_date) AS  checkInDate, " +
                            "C.room_name AS roomNam " +
                            "FROM reservation_complete A " +
                            "JOIN glamping B ON A.glamp_id = B.glamp_id " +
                            "JOIN room C ON A.room_id = C.room_id " +
                            "WHERE B.owner_id = :ownerId AND A. check_in_date BETWEEN :startDayId AND :endDayId " +
                            "GROUP BY A. check_in_date, C.room_name) " +
                            "SELECT IFNULL(SUM(sub.pay), 0) AS pay, " +
                            "DR.date AS times, " +
                            "DR.room_name AS roomName " +
                            "FROM DateRoom DR " +
                            "LEFT JOIN SubQuery sub ON DR.date = sub. checkInDate AND DR.room_name = sub.roomNam " +
                            "GROUP BY DR.date, DR.room_name " +
                            "ORDER BY DR.date, DR.room_name ",
            nativeQuery = true
    )
    List<GetRevenue> findRevenue(long ownerId, String startDayId, String endDayId);

    @Query(
            value =
                    "WITH RECURSIVE dates AS ( " +
                            "SELECT :startDayId AS check_in_date " +
                            "UNION ALL " +
                            "SELECT DATE_ADD(check_in_date, INTERVAL 1 DAY) " +
                            "FROM dates " +
                            "WHERE check_in_date < :endDayId) " +
                            "SELECT " +
                            "sud.roomName AS nameing, " +
                            "IFNULL(COUNT(sud.check_in_date), 0) AS cancelCount " +
                            "FROM dates " +
                            "LEFT JOIN ( " +
                            "SELECT " +
                            "A.room_id as counts, C.owner_id, B.room_name as roomName, A.check_in_date " +
                            "FROM reservation_cancel A " +
                            "JOIN room B ON A.room_id = B.room_id " +
                            "JOIN glamping C ON B.glamp_id = C.glamp_id " +
                            "WHERE C.owner_id = :ownerId) AS sud " +
                            "ON sud.check_in_date = dates.check_in_date " +
                            "GROUP BY  sud.roomName " +
                            "ORDER BY dates.check_in_date ",
            nativeQuery = true
    )
    List<GetCancelDto> findRoomCount(@Param("ownerId") long ownerId, @Param("startDayId") String startDayId, @Param("endDayId") String endDayId);

    @Query(
            value =
                    "WITH RECURSIVE dates AS ( " +
                            "SELECT :startDayId AS check_in_date " +
                            "UNION ALL " +
                            "SELECT DATE_ADD(check_in_date, INTERVAL 1 DAY) " +
                            "FROM dates " +
                            "WHERE check_in_date < :endDayId) " +
                            "SELECT " +
                            "sum(sud.pay_amount) AS totalpay " +
                            "FROM dates " +
                            "LEFT JOIN ( " +
                            "SELECT " +
                            "A.check_in_date, A.pay_amount " +
                            "FROM reservation_complete A " +
                            "JOIN room B ON A.room_id = B.room_id " +
                            "JOIN glamping C ON B.glamp_id = C.glamp_id " +
                            "WHERE C.owner_id = :ownerId) AS sud " +
                            "ON sud.check_in_date = dates.check_in_date ",
            nativeQuery = true
    )
    Long findTotalPay(@Param("ownerId") long ownerId, @Param("startDayId") String startDayId, @Param("endDayId") String endDayId);

}

package com.green.glampick.repository;

import com.green.glampick.dto.object.owner.OwnerBookItem;
import com.green.glampick.dto.response.owner.get.GetOwnerBookCompleteCountResponseDto;
import com.green.glampick.entity.ReservationCompleteEntity;
import com.green.glampick.dto.object.owner.GetPopularRoom;
import com.green.glampick.repository.resultset.GetReservationCompleteResultSet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationCompleteRepository extends JpaRepository<ReservationCompleteEntity, Long> {

    @Query(
            value =
                    "SELECT C.glamp_name AS glampName" +
                            ", C.glamp_id AS glampId " +
                            ", C.glamp_image AS glampImage " +
                            ", A.book_id AS bookId " +
                            ", B.room_name AS roomName " +
                            ", A.reservation_id AS reservationId" +
                            ", A.check_in_date AS checkInDate " +
                            ", A.check_out_date AS checkOutDate " +
                            ", A.status AS status " +
                            ", A.created_at AS createdAt " +
                            ", B.check_in_time AS checkInTime " +
                            ", B.check_out_time AS checkOutTime " +
                            "FROM reservation_complete A " +
                            "JOIN room B " +
                            "ON A.room_id = B.room_id " +
                            "JOIN glamping C " +
                            "ON B.glamp_id = C.glamp_id " +
                            "WHERE A.user_id = :userId " +
                            "ORDER BY A.check_in_date DESC ",
            nativeQuery = true
    )
    List<GetReservationCompleteResultSet> getBook(Long userId);

    //    @Query(
//            value =
//                    "SELECT " +
//                            "B.glamp_name AS glampName, " +
//                            "B.glamp_id AS glampId, " +
//                            "A.book_id AS bookId, " +
//                            "C.room_name AS roomName, " +
//                            "A.reservation_id AS reservationId, " +
//                            "A.check_in_date AS checkInDate, " +
//                            "A.check_out_date AS checkOutDate, " +
//                            "A.created_at AS createdAt, " +
//                            "C.check_in_time AS checkInTime, " +
//                            "C.check_out_time AS checkOutTime, " +
//                            "A.status AS status, " +
//                            "C.room_id AS roomId " +
//                            "FROM reservation_complete A " +
//                            "INNER JOIN	glamping B ON A.glamp_id = B.glamp_id " +
//                            "INNER JOIN room C ON A.room_id = C.room_id " +
//                            "WHERE B.owner_id = ?1 " +
//                            "ORDER BY A.check_in_date " +
//                            "LIMIT ?2 OFFSET ?3 ",
//            nativeQuery = true
//    )
//
//    List<GetReservationCompleteResultSet> getReservationCompleteByOwnerId(@Param("ownerId") Long ownerId,int limit ,int offset );
    @Query( "SELECT rc.inputName AS inputName,rc.personnel AS personnel, rc.checkInDate AS checkInDate, rc.checkOutDate AS checkOutDate, rc.payAmount AS payAmount, r.roomName AS roomName, " +
            "rc.pg AS pg, rc.createdAt AS createdAt, u.userPhone AS userPhone, rc.reservationId AS reservationId " +
            "FROM ReservationCompleteEntity rc " +
            "JOIN rc.room r JOIN rc.glamping g JOIN rc.user u " +
            "WHERE rc.checkInDate = :date AND g.owner.ownerId = :ownerId ")
    List<OwnerBookItem> getReservationCompleteByOwnerId(@Param("ownerId") Long ownerId, Pageable pageable, @Param("date") LocalDate date);

    //@Transactional
    ReservationCompleteEntity findByReservationId(Long reservationId);

    @Query("SELECT rc.checkInDate AS checkInDate, COUNT(rc.checkInDate) AS countComplete " +
            "FROM  ReservationCompleteEntity rc " +
            "JOIN rc.glamping g JOIN g.owner o " +
            "WHERE FUNCTION('MONTH', rc.checkInDate) = :month AND o.ownerId = :ownerId " +
            "GROUP BY rc.checkInDate")
    List<GetOwnerBookCompleteCountResponseDto> getCountFromReservationComplete(@Param("month") int month, @Param("ownerId") Long ownerId);

    @Query(
            value =
                    "WITH RECURSIVE date_range AS ( " +
                            "SELECT :startDayId AS check_in_date " +
                            "UNION ALL " +
                            "SELECT DATE_ADD(check_in_date, INTERVAL 1 DAY) " +
                            "FROM date_range " +
                            "WHERE check_in_date < :endDayId) " +
                            "SELECT " +
                            "date_range.check_in_date as checkInDate, " +
                            "IFNULL(COUNT(filtered_reservations.check_in_date), 0) AS reservationCount " +
                            "FROM date_range " +
                            "LEFT JOIN ( " +
                            "SELECT A.check_in_date " +
                            "FROM reservation_complete A " +
                            "JOIN room C ON A.room_id = C.room_id " +
                            "JOIN glamping B ON B.glamp_id = C.glamp_id " +
                            "WHERE B.owner_id = :ownerId " +
                            ") AS filtered_reservations ON filtered_reservations.check_in_date = date_range.check_in_date " +
                            "GROUP BY date_range.check_in_date " +
                            "ORDER BY date_range.check_in_date ",
            nativeQuery = true
    )
    List<GetPopularRoom> findPopularRoom(@Param("ownerId") long ownerId, @Param("startDayId") String startDayId, @Param("endDayId") String endDayId);
    @Query(
            value =
                    "WITH RECURSIVE dates AS ( " +
            "SELECT :startDayId AS check_in_date " +
            "UNION ALL " +
            "SELECT DATE_ADD(check_in_date, INTERVAL 1 DAY) " +
    "FROM dates " +
    "WHERE check_in_date < :endDayId) " +
    "SELECT " +
    "IFNULL(COUNT(sud.check_in_date), 0) AS totalCount " +
    "FROM dates " +
    "LEFT JOIN ( " +
            "SELECT A.check_in_date " +
            "FROM reservation_complete A " +
            "JOIN room C ON A.room_id = C.room_id " +
            "JOIN glamping B ON B.glamp_id = C.glamp_id " +
            "WHERE B.owner_id = :ownerId " +
    ") AS sud ON sud.check_in_date = dates.check_in_date ",
            nativeQuery = true
    )
    Long findTotal(@Param("ownerId") long ownerId, @Param("startDayId") String startDayId, @Param("endDayId") String endDayId);

}

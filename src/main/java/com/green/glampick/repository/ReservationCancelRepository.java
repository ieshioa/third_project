package com.green.glampick.repository;

import com.green.glampick.dto.object.owner.OwnerBookItem;
import com.green.glampick.dto.response.owner.get.GetOwnerBookCancelCountResponseDto;
import com.green.glampick.entity.ReservationCancelEntity;
import com.green.glampick.repository.resultset.GetReservationBeforeResultSet;
import com.green.glampick.repository.resultset.GetReservationCancelResultSet;
import com.green.glampick.repository.resultset.GetReservationCompleteResultSet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationCancelRepository extends JpaRepository<ReservationCancelEntity, Long> {

    @Query(
            value =
                    "SELECT C. glamp_name AS glampName" +
                            ", C.glamp_id AS glampId " +
                            ", C.glamp_image AS glampImage " +
                            ", A.book_id AS bookId " +
                            ", B.room_name AS roomName " +
                            ", A. reservation_id AS reservationId" +
                            ", A. check_in_date AS checkInDate " +
                            ", A. check_out_date AS checkOutDate " +
                            ", A. comment AS comment " +
                            ", A. created_at AS createdAt " +
                            ", B. check_in_time AS checkInTime " +
                            ", B. check_out_time AS checkOutTime " +
                            "FROM reservation_cancel A " +
                            "JOIN room B " +
                            "ON A.room_id = B.room_id " +
                            "JOIN glamping C " +
                            "ON B.glamp_id = C.glamp_id " +
                            "WHERE A.user_id = :userId " +
                            "ORDER BY A. reservation_id DESC ",
            nativeQuery = true
    )
    List<GetReservationCancelResultSet> getBook(Long userId);


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
//                            "C.room_id AS roomId " +
//                            "FROM reservation_cancel A " +
//                            "INNER JOIN	glamping B ON A.glamp_id = B.glamp_id " +
//                            "INNER JOIN room C ON A.room_id = C.room_id " +
//                            "WHERE B.owner_id = ?1 " +
//                            "ORDER BY A.check_in_date " +
//                            "LIMIT ?2 OFFSET ?3 ",
//            nativeQuery = true
//    )
//    List<GetReservationCancelResultSet> getReservationCancelByOwnerId(Long userId, int limit, int offset);
    @Query( "SELECT rc.inputName AS inputName,rc.personnel AS personnel, rc.checkInDate AS checkInDate, rc.checkOutDate AS checkOutDate, rc.payAmount AS payAmount, r.roomName AS roomName, " +
            "rc.pg AS pg, rc.createdAt AS createdAt, u.userPhone AS userPhone, rc.reservationId AS reservationId " +
            "FROM ReservationCancelEntity rc " +
            "JOIN rc.roomId r JOIN rc.glamping g JOIN rc.user u " +
            "WHERE rc.checkInDate = :date AND g.owner.ownerId = :ownerId ")
    List<OwnerBookItem> getReservationCancelByOwnerId(@Param("ownerId") Long ownerId, Pageable pageable, @Param("date") LocalDate date);

    @Query("SELECT rc.checkInDate AS checkInDate, COUNT(rc.checkInDate) AS countCancel " +
            "FROM  ReservationCancelEntity rc " +
            "JOIN rc.glamping g JOIN g.owner o " +
            "WHERE FUNCTION('MONTH', rc.checkInDate) = :month AND o.ownerId = :ownerId " +
            "GROUP BY rc.checkInDate")
    List<GetOwnerBookCancelCountResponseDto> getCountFromReservationCancel(@Param("month")int month, @Param("ownerId")Long ownerId);
}

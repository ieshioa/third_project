package com.green.glampick.repository;

import com.green.glampick.dto.object.owner.OwnerBookItem;
import com.green.glampick.dto.response.owner.get.GetOwnerBookBeforeCountResponseDto;
import com.green.glampick.entity.GlampingEntity;
import com.green.glampick.entity.ReservationBeforeEntity;
import com.green.glampick.entity.RoomEntity;
import com.green.glampick.repository.resultset.GetBookDateResultSet;
import com.green.glampick.repository.resultset.GetReservationBeforeResultSet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationBeforeRepository extends JpaRepository<ReservationBeforeEntity, Long> {

    @Query(
            value =
            "SELECT C. glamp_name AS glampName " +
            ", C.glamp_id AS glampId " +
            ", C. glamp_image AS glampImage " +
            ", A. book_id AS bookId " +
            ", B. room_name AS roomName " +
            ", A. reservation_id AS reservationId" +
            ", A. check_in_date AS checkInDate " +
            ", A. check_out_date AS checkOutDate " +
            ", A. created_at AS createdAt " +
            ", B. check_in_time AS checkInTime " +
            ", B. check_out_time AS checkOutTime " +
            "FROM reservation_before A " +
            "JOIN room B " +
            "ON A.room_id = B.room_id " +
            "JOIN glamping C " +
            "ON B.glamp_id = C.glamp_id " +
            "WHERE A.user_id = :userId " +
            "ORDER BY A.check_in_date ",
            nativeQuery = true
    )
    List<GetReservationBeforeResultSet> getBook(Long userId);
    @Query( "SELECT rb.inputName AS inputName,rb.personnel AS personnel, rb.checkInDate AS checkInDate, rb.checkOutDate AS checkOutDate, rb.payAmount AS payAmount, r.roomName AS roomName, " +
            "rb.pg AS pg, rb.createdAt AS createdAt, u.userPhone AS userPhone, rb.reservationId AS reservationId " +
            "FROM ReservationBeforeEntity rb " +
            "JOIN rb.room r JOIN rb.glamping g JOIN rb.user u " +
            "WHERE rb.checkInDate = :date AND g.owner.ownerId = :ownerId ")
    List<OwnerBookItem> getReservationBeforeByOwnerId(@Param("ownerId") Long ownerId, Pageable pageable, @Param("date") LocalDate date);

//    List<ReservationBeforeEntity> findByGlamping_Owner_OwnerIdAndCheckInDate(Long ownerId, LocalDate date);
    boolean existsByReservationId(Long reservationId);

    @Query("SELECT r FROM ReservationBeforeEntity r JOIN r.glamping g JOIN r.room rm JOIN r.user u WHERE r.checkOutDate < :dateTime")
    List<ReservationBeforeEntity> findAllByCheckOutDateBefore(LocalDate dateTime);

    @Query("SELECT rb.checkInDate AS checkInDate, COUNT(rb.checkInDate) AS countBefore " +
            "FROM  ReservationBeforeEntity rb " +
            "JOIN rb.glamping g JOIN g.owner o " +
            "WHERE FUNCTION('MONTH', rb.checkInDate) = :month AND o.ownerId = :ownerId " +
            "GROUP BY rb.checkInDate")
    List<GetOwnerBookBeforeCountResponseDto> getCountFromReservationBefore(@Param("month")int month, @Param("ownerId")Long ownerId);

    List<ReservationBeforeEntity> findByGlamping(GlampingEntity glamping);


    @Query ("SELECT rb.glamping.glampId AS glampId " +
            " ,rb.checkInDate AS checkInDate, rb.checkOutDate AS checkOutDate" +
            " from ReservationBeforeEntity rb " +
            "WHERE rb.room = :room")
    List<GetBookDateResultSet> getBookDateByRoom(RoomEntity room);
}
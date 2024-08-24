package com.green.glampick.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "reservation_cancel")
public class ReservationCancelEntity extends CreatedAt {

    //예약 테이블
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Comment("예약 ID")
    private long reservationId;

    @Column(length = 13, nullable = false, unique = true) @Comment("예약 번호")
    private String bookId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) @Comment("유저 ID")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "glamp_id", nullable = false) @Comment("글램핑 ID")
    private GlampingEntity glamping;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false) @Comment("객실 ID")
    private RoomEntity roomId;

    @Column(length = 10, nullable = false) @Comment("예약자 성함")
    private String inputName;

    @Column(length = 11, nullable = false) @Comment("예약 인원")
    private Integer personnel;

    @Column(nullable = false) @Comment("체크인 일자")
    private LocalDate checkInDate;

    @Column(nullable = false) @Comment("체크아웃 일자")
    private LocalDate checkOutDate;

    @Column(length = 10, nullable = false) @Comment("결제 수단")
    private String pg;

    @Column(length = 20, nullable = false) @Comment("최종 결제 가격")
    private Long payAmount;

    @Column(length = 500) @Comment("예약 취소 사유")
    private String comment;


}

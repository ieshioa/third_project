package com.green.glampick.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "room_price")
@ToString
public class RoomPriceEntity extends CreatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Comment("가격 PK")
    private Long roomPriceId;

    @OneToOne
    @JoinColumn(name = "room_id", nullable = false, unique = true) @Comment("객실 ID")
    private RoomEntity room;

    @Column( nullable = false) @Comment("비수기 주중 가격")
    private Integer weekdayPrice;

    @Column( nullable = false) @Comment("비수기 주말 가격")
    private Integer weekendPrice;

    @Column( nullable = false) @Comment("성수기 주중 가격")
    private Integer peakWeekdayPrice;

    @Column( nullable = false) @Comment("성수기 주말 가격")
    private Integer peakWeekendPrice;

//    @Column(nullable = false) @Comment("성수기 시작")
//    private LocalDate startPeakDay;
//
//    @Column(nullable = false) @Comment("성수기 끝")
//    private LocalDate endPeakDay;


}

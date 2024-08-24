package com.green.glampick.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@Table(name = "room_service", uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"room_id","service_id"}
)})
public class RoomServiceEntity extends CreatedAt {
    //룸 서비스시설 테이블
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Comment("룸 서비스 ID")
    private Long roomServiceId;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false) @Comment("객실 ID")
    private RoomEntity room;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false) @Comment("서비스 ID")
    private ServiceEntity service;

}

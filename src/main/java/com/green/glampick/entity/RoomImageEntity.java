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
@Table(name = "room_image")
public class RoomImageEntity extends CreatedAt {
    //객실 이미지 테이블
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Comment("객실 이미지 ID")
    private Long roomImageId;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false) @Comment("객실 ID")
    private RoomEntity roomId;

    @Column(length = 200, nullable = false) @Comment("객실 이미지명")
    private String roomImageName;

}

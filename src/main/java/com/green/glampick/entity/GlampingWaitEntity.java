package com.green.glampick.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.locationtech.jts.geom.*;

@Getter
@Setter
@Entity
@Table(name = "glamping_wait")
public class GlampingWaitEntity extends CreatedAt {
    // 글램핑 테이블
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Comment("글램핑 ID")
    private Long glampId;

    @OneToOne
    @JoinColumn(name = "owner_id", nullable = false, unique = true) @Comment("사장님 ID")
    private OwnerEntity owner;

    @Column(length = 50, nullable = false) @Comment("글램핑명")
    private String glampName;

    @Column(length = 11) @Comment("글램핑 전화번호")
    private String glampCall;

    @Column(length = 200, nullable = false) @Comment("대표 이미지")
    private String glampImage;

    @Column(length = 50, nullable = false) @Comment("글램핑 위치")
    private String glampLocation;

//    @Column(columnDefinition = "POINT") @Comment("글램핑 위치 좌표")
//    private Point location;

    @Column(nullable = false, length = 9) @Comment("글램핑 지역분류")
    private String region;

    @ColumnDefault("0") @Comment("추가 인원당 요금")
    private Integer extraCharge;

    @Column(columnDefinition = "LONGTEXT", nullable = false) @Comment("글램핑 소개")
    private String glampIntro;

    @Column(columnDefinition = "LONGTEXT", nullable = false) @Comment("글램핑 기본정보")
    private String infoBasic;

    @Column(columnDefinition = "LONGTEXT", nullable = false) @Comment("글램핑 유의사항")
    private String infoNotice;

    @Column(columnDefinition = "LONGTEXT", nullable = false) @Comment("추가 위치 정보")
    private String traffic;

    @Column(columnDefinition = "TINYINT", nullable = false) @ColumnDefault("0") @Comment("심사 반려 : -1")
    private Integer exclusionStatus;

}

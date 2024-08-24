package com.green.glampick.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "glamp_peak")
public class GlampPeakEntity extends UpdatedAt {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Comment("글램프 피크 PK")
    private Long glampPeakId;

    @OneToOne @JoinColumn(name = "glamp_id", nullable = false, unique = true) @Comment("글램핑 PK")
    private GlampingEntity glamp;

    @Column(nullable = false) @Comment("성수기 시작 날짜")
    private LocalDate peakStart;

    @Column(nullable = false) @Comment("성수기 끝 날짜")
    private LocalDate peakEnd;

    @Column(nullable = false) @Comment("퍼센트")
    private Integer percent;

}

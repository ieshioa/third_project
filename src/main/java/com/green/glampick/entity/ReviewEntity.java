package com.green.glampick.entity;

import com.green.glampick.dto.request.user.PostReviewRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "review")
public class ReviewEntity extends CreatedAt{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Comment("리뷰 ID")
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) @Comment("객실 ID")
    private UserEntity userId;

    @ManyToOne(fetch = FetchType.LAZY)    @JoinColumn(name = "glamp_id", nullable = false) @Comment("글램핑 ID")
    private GlampingEntity glampId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false) @Comment("객실 ID")
    private ReservationCompleteEntity reservationId;

    @Column(length = 500, nullable = false ) @Comment("리뷰 내용")
    private String reviewContent;

    @Column(nullable = false, columnDefinition = "TINYINT") @Comment("리뷰 별점")
    private Integer reviewStarPoint;

    @Column(length = 500) @Comment("사장님 답변")
    private String reviewComment;



}

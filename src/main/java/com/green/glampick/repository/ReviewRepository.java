package com.green.glampick.repository;

import com.green.glampick.entity.GlampingEntity;
import com.green.glampick.entity.ReviewEntity;
import com.green.glampick.repository.resultset.GetUserReviewResultSet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    @Query("SELECT r FROM ReviewEntity r WHERE r.reviewId = :reviewId")
    ReviewEntity findReviewById(@Param("reviewId") Long reviewId);

    @Query(
            value =
                    "SELECT E.glamp_name AS glampName " +
                            ", D.room_name AS roomName " +
                            ", E.glamp_id AS glampId " +
                            ", C.user_nickname AS userNickname " +
                            ", C.user_profile_image AS userProfileImage " +
                            ", B.review_id AS reviewId " +
                            ", B.reservation_id AS reservationId " +
                            ", B.review_content AS reviewContent " +
                            ", B.review_star_point AS reviewStarPoint " +
                            ", B.review_comment AS ownerReviewComment " +
                            ", B.created_at AS createdAt " +
                            ", A.book_id AS bookId " +
                            "FROM reservation_complete A " +
                            "JOIN review B " +
                            "ON A.reservation_id = B.reservation_id " +
                            "JOIN user C " +
                            "ON C.user_id = B.user_id " +
                            "JOIN room D " +
                            "ON A.room_id = D.room_id " +
                            "JOIN glamping E " +
                            "ON A.glamp_id = E.glamp_id " +
                            "WHERE C.user_id = ?1 " +
                            "ORDER BY B.review_id DESC " +
                            "LIMIT ?2 OFFSET ?3",
            nativeQuery = true
    )
    List<GetUserReviewResultSet> getReview(long userId, int limit, int offset);
    @Query(
            value =
                    "SELECT   D.room_name AS roomName, " +
                            " E.glamp_id AS glampId, " +
                            " B.user_nickname AS userNickname," +
                            " B.user_profile_image AS userProfileImage, " +
                            " A.review_id AS reviewId, " +
                            " A.reservation_id AS reservationId, " +
                            " A.review_content AS reviewContent, " +
                            " A.review_star_point AS reviewStarPoint, " +
                            " A.review_comment AS ownerReviewComment, " +
                            " A.created_at AS createdAt " +
                            "FROM review A " +
                            "INNER JOIN user B " +
                            "ON A.user_id = B.user_id " +
                            "INNER JOIN reservation_complete C " +
                            "ON A.reservation_id = C.reservation_id " +
                            "INNER JOIN room D " +
                            "ON D.room_id = C.room_id " +
                            "INNER JOIN glamping E " +
                            "ON A.glamp_id = E.glamp_id " +
                            "INNER JOIN owner F " +
                            "ON	F.owner_id = E.owner_id " +
                            "WHERE F.owner_id = ?1 " +
                            "ORDER BY A.review_id DESC " +
                            "LIMIT ?2 OFFSET ?3",
            nativeQuery = true
    )
    List<GetUserReviewResultSet> getReviewForOwner(Long ownerId, int limit, int offset);

    @Query(
            value =
                    "SELECT   D.room_name AS roomName, " +
                            " E.glamp_id AS glampId, " +
                            " B.user_nickname AS userNickname," +
                            " B.user_profile_image AS userProfileImage, " +
                            " A.review_id AS reviewId, " +
                            " A.reservation_id AS reservationId, " +
                            " A.review_content AS reviewContent, " +
                            " A.review_star_point AS reviewStarPoint, " +
                            " A.review_comment AS ownerReviewComment, " +
                            " A.created_at AS createdAt " +
                            "FROM review A " +
                            "INNER JOIN user B " +
                            "ON A.user_id = B.user_id " +
                            "INNER JOIN reservation_complete C " +
                            "ON A.reservation_id = C.reservation_id " +
                            "INNER JOIN room D " +
                            "ON D.room_id = C.room_id " +
                            "INNER JOIN glamping E " +
                            "ON A.glamp_id = E.glamp_id " +
                            "INNER JOIN owner F " +
                            "ON	F.owner_id = E.owner_id " +
                            "WHERE F.owner_id = ?1 AND A.review_comment IS NULL " +
                            "ORDER BY A.review_id DESC " +
                            "LIMIT ?2 OFFSET ?3",
            nativeQuery = true
    )
    List<GetUserReviewResultSet> getReviewForOwnerExcludeComment(Long ownerId, int limit, int offset);

    @Query(
            value = "SELECT COUNT(*) FROM reservation_complete A " +
                    "JOIN review B ON A.reservation_id = B.reservation_id " +
                    "JOIN user C ON C.user_id = B.user_id " +
                    "JOIN room D ON A.room_id = D.room_id " +
                    "JOIN glamping E ON A.glamp_id = E.glamp_id " +
                    "WHERE C.user_id = ?1",
            nativeQuery = true
    )
    long getTotalReviewsCount(Long userId);

    @Query(
            value =
                    "UPDATE glamping g " +
                            "JOIN ( " +
                            "SELECT " +
                            "glamp_id, " +
                            "TRUNCATE(AVG(review_star_point), 1) AS avg_star_point, " +
                            "COUNT(review_content) AS review_count " +
                            "FROM review " +
                            "GROUP BY glamp_id " +
                            ") r ON g.glamp_id = r.glamp_id " +
                            "SET " +
                            "g.star_point_avg = r.avg_star_point, " +
                            "g.review_count = r.review_count ",
            nativeQuery = true)
    void findStarPointAvg();
    @Modifying
    @Transactional
    @Query(
            value =
                    "UPDATE glamping " +
                            "SET star_point_avg = (" +
                            "SELECT TRUNCATE(AVG(review_star_point),1) " +
                            "FROM review WHERE glamp_id = :glampId) " +
                            ", review_count = (SELECT COUNT(review_content) " +
                            "FROM review WHERE glamp_id = :glampId) " +
                            "WHERE glamp_id = :glampId ",

            nativeQuery = true)
    void findStarPointAvg(long glampId);


    @Modifying
    @Transactional
    @Query(
            value =
                    "UPDATE review B " +
                            "JOIN reservation_complete A " +
                            "ON A.reservation_id = B.reservation_id " +
                            "SET B.glamp_id = A.glamp_id , A.status = 1 " +
                            "WHERE B.reservation_id = :reservationId ",
            nativeQuery = true)
    void fin(long reservationId);


    @Query("SELECT COUNT(r.reviewId) FROM ReviewEntity r JOIN r.glampId g JOIN g.owner o WHERE o.ownerId = :ownerId")
    Long getTotalOwnersReviewsCount(@Param("ownerId") Long ownerId);

    @Query("SELECT COUNT(r.reviewId) FROM ReviewEntity r JOIN r.glampId g JOIN g.owner o WHERE o.ownerId = :ownerId AND r.reviewComment IS NULL")
    Long getTotalOwnersNoReviewsCount(@Param("ownerId") Long ownerId);
}

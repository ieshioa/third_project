package com.green.glampick.repository;

import com.green.glampick.entity.GlampFavoriteEntity;
import com.green.glampick.entity.GlampingEntity;
import com.green.glampick.repository.resultset.GetFavoriteGlampingResultSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteGlampingRepository extends JpaRepository<GlampFavoriteEntity, Long> {


    @Query(
            value =
            "SELECT distinct(A.glamp_id) AS glampId " +
            ", A.glamp_name AS glampName " +
            ", A.region  AS region " +
            ", A.star_point_avg AS starPoint " +
            ", A.review_count AS reviewCount " +
//            ", B.room_price AS price " +
            ", A.glamp_image AS glampImage " +
            ", C.user_id " +
            "FROM glamping A " +
            "JOIN room B " +
            "ON A.glamp_id = B.glamp_id " +
            "JOIN glamp_favorite C " +
            "ON A.glamp_id = C.glamp_id " +
            "WHERE C.user_id = ?1 " +
//            "AND B.room_price = ( SELECT MIN(room_price) " +
//                                "FROM room " +
//                                "WHERE glamp_id = A.glamp_id ) " +
            "ORDER BY C.created_at DESC ",
            nativeQuery = true
    )
    List<GetFavoriteGlampingResultSet> getFavoriteGlamping(Long glampId);



    Long countByGlamping(GlampingEntity glampingEntity);
}





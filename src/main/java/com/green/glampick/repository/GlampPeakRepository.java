package com.green.glampick.repository;

import com.green.glampick.entity.GlampPeakEntity;
import com.green.glampick.entity.GlampingEntity;
import com.green.glampick.repository.resultset.GetPeakDateResultSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GlampPeakRepository extends JpaRepository<GlampPeakEntity, Long> {

    @Query(" select gp.peakStart as startDate, gp.peakEnd as endDate, gp.percent AS percent from GlampPeakEntity gp " +
            "where gp.glamp.glampId = :glampId")
    GetPeakDateResultSet getPeak(Long glampId);


//    @Query("delete from GlampPeakEntity gp where gp.glamp.glampId = :glampId ")
//    void deleteById(@Param("glampId") Long glampId);

    Optional<GlampPeakEntity> findByGlamp(GlampingEntity glamp);
}

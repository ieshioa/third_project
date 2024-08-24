package com.green.glampick.repository;

import com.green.glampick.repository.resultset.GetGlampingInfoResultSet;
import org.springframework.data.jpa.repository.Modifying;
import com.green.glampick.entity.GlampingWaitEntity;
import com.green.glampick.entity.OwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface GlampingWaitRepository extends JpaRepository<GlampingWaitEntity, Long> {

    @Modifying
    @Transactional
    @Query("update GlampingWaitEntity g" +
            " set  g.glampImage = :glampImg" +
            " where g.glampId = :glampId")
    void updateGlampImageByGlampId(@Param("glampImg") String glampImg, @Param("glampId") Long glampId);


    GlampingWaitEntity findByOwner(OwnerEntity owner);
    GlampingWaitEntity findByGlampId(Long glampId);
    GlampingWaitEntity findByGlampLocation(String glampLocation);

    @Query(" select g.glampId as glampId, g.glampName AS name, g.glampCall AS call" +
            ", g.glampImage AS image, g.glampLocation AS location, " +
            "g.region AS region, g.extraCharge AS charge, g.glampIntro AS intro, g.infoBasic AS basic" +
            ", g.infoNotice AS notice, g.traffic AS traffic, g.exclusionStatus AS exclusionStatus" +
            " from GlampingWaitEntity g where g.owner = :owner")
    GetGlampingInfoResultSet getGlampingInfo(OwnerEntity owner);
}

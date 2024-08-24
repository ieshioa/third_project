package com.green.glampick.repository;

import com.green.glampick.entity.GlampingEntity;
import com.green.glampick.entity.GlampingWaitEntity;
import com.green.glampick.entity.OwnerEntity;
import com.green.glampick.repository.resultset.GetGlampingInfoResultSet;
import com.green.glampick.repository.resultset.GetMapListResultSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public interface GlampingRepository extends JpaRepository<GlampingEntity, Long> {

    GlampingEntity findByGlampId(Long glampId);

    Double findStarPointAvgByGlampId(Long glampId);

    GlampingEntity findByOwner(OwnerEntity owner);
    GlampingEntity findByGlampLocation(String glampLocation);

    @Modifying
    @Transactional
    @Query("update GlampingEntity g set g.glampName = :name, g.glampCall = :call" +
            ", g.glampLocation = :location, g.region = :region" +
            ", g.extraCharge = :extra, g.glampIntro = :intro" +
            ", g.infoBasic = :basic, g.infoNotice = :notice" +
            ", g.traffic = :traffic where g.glampId = :glampId")
    void updateGlampingInformation(String name, String call, String location, String region, int extra,
            String intro, String basic, String notice, String traffic, Long glampId);

    @Modifying
    @Transactional
    @Query(value = "update glamping g" +
            " set g.location = ST_GeomFromText(:point)" +
            " where g.glamp_id = :glampId", nativeQuery = true)
    void updateGlampLocation(@Param("glampId") Long glampId, @Param("point") String point);


    @Modifying
    @Transactional
    @Query("update GlampingEntity g set g.glampImage = :glampImg where g.glampId = :glampId")
    void updateGlampImageByGlampId(String glampImg, Long glampId);

    @Query(" select g.glampId AS glampId, g.glampName AS name, g.glampCall AS call" +
            ", g.glampImage AS image, g.glampLocation AS location, " +
            "g.region AS region, g.extraCharge AS charge, g.glampIntro AS intro, g.infoBasic AS basic" +
            ", g.infoNotice AS notice, g.traffic AS traffic from GlampingEntity g where g.owner = :owner")
    GetGlampingInfoResultSet getGlampingInfo(OwnerEntity owner);

    @Query ("select g.glampId as glampId, g.glampName as glampName, g.glampImage as glampPic" +
            ", g.starPointAvg as starPoint, g.reviewCount as reviewCount" +
            ", g.glampLocation as location" +
            " from GlampingEntity g where g.region = :region and g.activateStatus = 1 " +
            "order by g.recommendScore DESC")
    List<GetMapListResultSet> getMapList(String region);
    @Query("select g.glampId as glampId, g.glampName as glampName, g.glampImage as glampPic" +
            ", g.starPointAvg as starPoint, g.reviewCount as reviewCount" +
            ", g.glampLocation as location " +
            "from GlampingEntity g where g.activateStatus = 1 " +
            "order by g.recommendScore DESC")
    List<GetMapListResultSet> getMapList();

}

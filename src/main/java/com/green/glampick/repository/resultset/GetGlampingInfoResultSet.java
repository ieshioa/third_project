package com.green.glampick.repository.resultset;

public interface GetGlampingInfoResultSet {
    Long getGlampId();
    String getName();
    String getCall();
    String getImage();
    String getLocation();
    String getRegion();
    Integer getCharge();
    String getIntro();
    String getBasic();
    String getNotice();
    String getTraffic();
    Integer getExclusionStatus();
}

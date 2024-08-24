package com.green.glampick.repository.resultset;
import org.locationtech.jts.geom.Point;

public interface GetMapListResultSet {
    Long getGlampId();
    String getGlampName();
    String getGlampPic();
    Double getStarPoint();
    Integer getReviewCount();
    String getLocation();
}

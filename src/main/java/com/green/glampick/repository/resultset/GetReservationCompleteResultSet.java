package com.green.glampick.repository.resultset;

public interface GetReservationCompleteResultSet {

    String getGlampName();
    String getGlampImage();
    Long getGlampId();
    Long getRoomId();
    String getBookId();
    String getRoomName();
    Long getReservationId();
    String getCheckInDate();
    String getCheckOutDate();
    Integer getStatus();
    String getCreatedAt();
    String getCheckInTime();
    String getCheckOutTime();

}

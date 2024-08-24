package com.green.glampick.repository.resultset;

public interface GetReservationCancelResultSet {

    String getGlampName();
    String getGlampImage();
    Long getGlampId();
    Long getRoomId();
    String getBookId();
    String getRoomName();
    Long getReservationId();
    String getCheckInDate();
    String getCheckOutDate();
    String getComment();
    String getCreatedAt();
    String getCheckInTime();
    String getCheckOutTime();

}

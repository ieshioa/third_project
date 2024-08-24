package com.green.glampick.dto.object.owner;

import java.time.LocalDate;
import java.time.LocalDateTime;


public interface OwnerBookItem {
    Long getPersonnel();
    Long getPayAmount();
    Long getReservationId();
    LocalDate getCheckInDate();
    LocalDate getCheckOutDate();
    LocalDateTime getCreatedAt();
    String getInputName();
    String getPg();
    String getUserPhone();
    String getRoomName();
}

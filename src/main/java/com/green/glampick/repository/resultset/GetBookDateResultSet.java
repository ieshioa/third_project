package com.green.glampick.repository.resultset;

import java.time.LocalDate;

public interface GetBookDateResultSet {
    Long getGlampId();
    LocalDate getCheckInDate();
    LocalDate getCheckOutDate();
}

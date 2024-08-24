package com.green.glampick.repository.resultset;

import java.time.LocalDate;

public interface GetPeakDateResultSet{
    LocalDate getStartDate();
    LocalDate getEndDate();
    Integer getPercent();
}

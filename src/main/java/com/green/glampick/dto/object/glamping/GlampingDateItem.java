package com.green.glampick.dto.object.glamping;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GlampingDateItem {
    private long glampId;
    private long roomId;
    private String checkInDate;
    private String checkOutDate;
}

package com.green.glampick.dto.object.owner;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OwnerBookDetailListItem {
    private String inputName;
    private String roomName;
    private Integer period;
    private Long personnel;
    private Long payAmount;
    private Long reservationId;
    private String checkInDate;
    private String checkOutDate;
    private String userPhone;
    private String pg;
    private String createdAt;

}

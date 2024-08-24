package com.green.glampick.dto.object.glamping;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class GlampingRoomListItem implements Comparable<GlampingRoomListItem> {

    private List<String> roomServices;

    @Schema(example = "5", description = "객실 pk")
    private long roomId;
    @Schema(example = "cb4a0b8f-629e-4d20-9626-cd45347837df.png", description = "객실사진")
    private String pic;
    @Schema(example = "우당탕탕카라반", description = "객실네임")
    private String roomName;
    @Schema(example = "137,000", description = "객실 가격")
    private String roomPrice;
    @Schema(example = "기준 2명", description = "기준 명 수")
    private int roomNumPeople;
    @Schema(example = "최대 5명", description = "최대 명 수")
    private int roomMaxPeople;
    @Schema(example = "15:00", description = "체크인 시간")
    private String checkInTime;
    @Schema(example = "23:00", description = "체크아웃 시간")
    private String checkOutTime;
    @Schema(example = "true", $dynamicAnchor = "예약가능여부")
    private boolean isReservationAvailable;
    @Schema(example = "15000", description = "인당추가요금")
    private Integer extraCharge;

    @Override
    public int compareTo(GlampingRoomListItem other) {
        // 첫 번째 기준: isReservationAvailable이 true이면 우선순위를 높게 설정
        if (this.isReservationAvailable && !other.isReservationAvailable) {
            return -1; // 현재 객체가 우선순위가 높음
        } else if (!this.isReservationAvailable && other.isReservationAvailable) {
            return 1; // 다른 객체가 우선순위가 높음
        } else {
            // 두 번째 기준: roomName을 알파벳 순으로 정렬
            return this.roomName.compareTo(other.roomName);
        }
    }
}

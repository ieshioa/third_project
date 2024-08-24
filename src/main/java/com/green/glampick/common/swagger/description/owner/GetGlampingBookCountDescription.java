package com.green.glampick.common.swagger.description.owner;

public class GetGlampingBookCountDescription {
    public static final String BOOK_COUNT_FROM_OWNER_GLAMPING_DESCRIPTION =

            "<strong>사장이 글램핑에 대한 예약정보 건수를 모두 불러옵니다.</strong>" +
            "<p>로그인이 필요한 기능입니다. 상단 Authorize 에 토큰값을 입력 후 이용해주세요.</p>" +
            "<p>date : 캘린더 날짜</p>";

    public static final String BOOK_COUNT_RESPONSE_DESCRIPTION =
            "checkInDate: 예약 날짜" +
            "ingCount: 현재 예약 건수" +
            "cancelCount: 취소 예약 건수" +
            "completeCount: 완료된 예약 건수";
}

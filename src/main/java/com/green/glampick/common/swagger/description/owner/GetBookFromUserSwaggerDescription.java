package com.green.glampick.common.swagger.description.owner;

public class GetBookFromUserSwaggerDescription {

    public static final String BOOK_FROM_USER_REVIEW_VIEW_DESCRIPTION =

            "<strong>글램핑에 대한 예약정보를 모두 불러옵니다.</strong>" +
            "<p>로그인이 필요한 기능입니다. 상단 Authorize 에 토큰값을 입력 후 이용해주세요.</p>" +
            "<p>-------------------------------------------------</p>" +
            "<p>date : 캘린더 선택 날짜 PK</p>" +
            "<p>page : 선택 페이지</p>"
            ;

    public static final String BOOK_FROM_USER_REVIEW_VIEW_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
            "<p>RN - 예약된 내역이 존재하지 않음 (400)</p>" +
            "<p>DBE - 데이터베이스 에러 (500)</p>" +
            "<p>INVALID_ENTITY - PK 입력 오류 (400)</p>"
            ;

}

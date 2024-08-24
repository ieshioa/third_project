package com.green.glampick.common.swagger.description.user;

public class PostUserBookCancelSwaggerDescription {

    public static final String USER_BOOK_CANCEL_DESCRIPTION =

            "<strong>로그인한 유저의 예약내역을 취소합니다.</strong>" +
            "<p>로그인이 필요한 기능입니다. 상단 Authorize 에 토큰값을 입력 후 이용해주세요.</p>" +
            "<p>-------------------------------------------------</p>" +
            "<p>comment : 예약 취소 사유 (필수값이 아닙니다.)</p>"
            ;

    public static final String USER_BOOK_CANCEL_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
            "<p>CU - 미 로그인 (400)</p>" +
            "<p>DBE - 데이터베이스 에러 (500)</p>"
            ;

}

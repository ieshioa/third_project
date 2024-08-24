package com.green.glampick.common.swagger.description.book;

public class PostBookSwaggerDescription {

    public static final String POST_BOOK_DESCRIPTION =

            "<strong>로그인 한 유저가 글램핑을 예약합니다.</strong>" +
            "<p>로그인이 필요한 기능입니다. 상단 Authorize 에 토큰값을 입력 후 이용해주세요.</p>" +
            "<p>-------------------------------------------------</p>" +
            "<p>glampId : 글램핑 PK</p>" +
            "<p>roomId : 룸 PK</p>" +
            "<p>personnel : 예약 인원</p>" +
            "<p>inputName : 예약자 성함</p>" +
            "<p>checkInDate : 체크인 일자</p>" +
            "<p>checkOutDate : 체크아웃 일자</p>" +
            "<p>pg : 결제 수단</p>" +
            "<p>payAmount : 최종 결제 금액</p>" +
            "<p>-------------------------------------------------</p>" +
            "<strong>체크인 일자 및 체크아웃 일자에 대한 양식은 아래와 같습니다.</strong>" +
            "<p>\"2024-08-01\"</p>"
            ;

    public static final String POST_BOOK_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
            "<p>NMF - 찾을수 없는 회원정보 (400)</p>" +
            "<p>DB - 중복된 예약처리 (400)</p>" +
            "<p>WD - 잘못 입력된 날짜정보 (400)</p>" +
            "<p>DBE - 데이터베이스 에러 (500)</p>"
            ;

}

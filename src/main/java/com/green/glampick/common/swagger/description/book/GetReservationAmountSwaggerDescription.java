package com.green.glampick.common.swagger.description.book;

public class GetReservationAmountSwaggerDescription {

    public static final String GET_RESERVATION_AMOUNT_DESCRIPTION =

            "<strong>최종 결제가격 정보를 불러옵니다.</strong>" +
            "<p>-------------------------------------------------</p>" +
            "<p>roomId : 객실 PK</p>" +
            "<p>personnel : 예약 인원</p>" +
            "<p>glampId : 글램핑 PK</p>" +
            "<p>-------------------------------------------------</p>" +
            "<p>roomPrice : 객실 가격</p>" +
            "<p>extraChargePrice : 추가 인원당 요금</p>" +
            "<p>payAmount : 총 결제 금액 (객실가격 + (기준인원 - 예약인원) * 추가 인원당 요금)</p>"
            ;

    public static final String GET_RESERVATION_AMOUNT_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
            "<p>DBE - 데이터베이스 에러 (500)</p>"
            ;

}

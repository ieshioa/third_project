package com.green.glampick.common.swagger.description.login;

public class PostSendSmsSwaggerDescription {

    public static final String SEND_SMS_DESCRIPTION =

            "<strong>입력한 휴대폰 번호로 인증번호가 발송됩니다.</strong>" +
            "<p>인증번호는 6자리의 랜덤 숫자 코드로 발송됩니다.</p>" +
            "<p>-------------------------------------------------</p>" +
            "<p><strong>전화번호 정규표현식</strong></p>" +
            "<p>01. 휴대전화 번호 패턴: '010', '011', '016', '017', '018', '019'로 시작</p>" +
            "<p>02. 중간과 마지막에 3~4자리 숫자, 하이픈(-)은 선택 사항</p>" +
            "<p>03. 일반 전화번호 패턴: '02'에서 '09' 사이의 지역번호로 시작</p>" +
            "<p>04. 중간과 마지막에 3~4자리 숫자, 하이픈(-)은 선택 사항</p>" +
            "<p>예시: 010-1234-5678, 02-123-4567</p>"
            ;

    public static final String SEND_SMS_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
            "<p>IPH - 전화번호 정규표현식 에러 (400)</p>" +
            "<p>DBE - 데이터베이스 에러 (500)</p>"
            ;

}

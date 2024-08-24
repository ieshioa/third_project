package com.green.glampick.common.swagger.description.login;

public class PostSendMailSwaggerDescription {

    public static final String SEND_MAIL_DESCRIPTION =

            "<strong>입력한 이메일로 인증번호가 발송됩니다.</strong>" +
            "<p>인증번호는 6자리의 랜덤 숫자 코드로 발송됩니다.</p>" +
            "<p>-------------------------------------------------</p>" +
            "<p><strong>이메일 정규표현식</strong></p>" +
            "<p>01. 최소 하나 이상의 문자, 숫자, 언더스코어(_), 하이픈(-), 마침표(.)로 시작</p>" +
            "<p>02. '@' 문자 포함</p>" +
            "<p>03. 최소 하나 이상의 문자, 숫자, 언더스코어(_), 하이픈(-)과 마침표(.)가 반복</p>" +
            "<p>04. 마지막에 2~4자의 문자, 숫자, 언더스코어(_), 하이픈(-)으로 끝남</p>" +
            "<p>예시: example@mail.com, user.name@example.co.kr</p>"
            ;

    public static final String SEND_MAIL_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
            "<p>DE - 중복 이메일 (400)</p>" +
            "<p>EE - 비어있는 이메일 값 (400)</p>" +
            "<p>IE - 이메일 정규표현식 에러 (400)</p>" +
            "<p>DBE - 데이터베이스 에러 (500)</p>"
            ;

}

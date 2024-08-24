package com.green.glampick.common.swagger.description.login;

public class PostCheckMailSwaggerDescription {

    public static final String CHECK_MAIL_DESCRIPTION =

            "<strong>이메일에 발송된 코드를 전화번호와 함께 입력합니다.</strong>" +
            "<p>인증번호가 맞다면 True 값 / 맞지 않다면 \"IC\" 코드와 함께 False 값을 응답합니다.</p>"
            ;

    public static final String CHECK_MAIL_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
            "<p>EF - 유효시간이 지남 (400)</p>" +
            "<p>IC - 인증번호가 맞지 않음 (400)</p>" +
            "<p>DBE - 데이터베이스 에러 (500)</p>"
            ;

}

package com.green.glampick.common.swagger.description.user;

public class PostUserPasswordCheckedSwaggerDescription {

    public static final String USER_PASSWORD_CHECK_DESCRIPTION =

            "<strong>비밀번호 재확인을 진행합니다.</strong>" +
            "<p>로그인한 유저의 비밀번호를 다시 확인합니다.</p>" +
            "<p>-------------------------------------------------</p>" +
            "<p>해당 기능은 유저페이지 진입 시, 사용하는 API 입니다.</p>"
            ;

    public static final String USER_PASSWORD_CHECK_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
            "<p>NMP - 비밀번호 미일치 (400)</p>" +
            "<p>NU - 존재하지 않는 유저 (400)</p>" +
            "<p>CU - 미 로그인 (400)</p>" +
            "<p>DBE - 데이터베이스 에러 (500)</p>"
            ;

}

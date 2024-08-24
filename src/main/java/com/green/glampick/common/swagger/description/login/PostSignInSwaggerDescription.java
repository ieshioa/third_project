package com.green.glampick.common.swagger.description.login;

public class PostSignInSwaggerDescription {

    public static final String SIGN_IN_DESCRIPTION =

            "<strong>이메일을 통한 로그인을 진행합니다.</strong>" +
            "<p>회원가입 시 기입했던 이메일과 비밀번호를 입력하여 로그인 합니다.</p>" +
            "<p>로그인 성공 시, AccessToken 값을 응답합니다.</p>"
            ;

    public static final String SIGN_IN_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
            "<p>VF - 유효성 검사 실패 (400)</p>" +
            "<p>SF - 로그인 실패 (401)</p>" +
            "<p>DBE - 데이터베이스 에러 (500)</p>"
            ;

}

package com.green.glampick.common.swagger.description.admin;

public class PatchAccessSignUpSwaggerDescription {

    public static final String ACCESS_SIGN_UP_DESCRIPTION =

            "<strong>가입을 심사중인 사장님의 아이디에 권한을 부여합니다.</strong>" +
            "<p>관리자 로그인이 필요한 기능입니다. 상단 Authorize 에 토큰값을 입력 후 이용해주세요.</p>"



            ;

    public static final String ACCESS_SIGN_UP_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
            "<p>NP - 권한 없음 (403)</p>" +
            "<p>DBE - 데이터베이스 에러 (500)</p>"
            ;

}

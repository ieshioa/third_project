package com.green.glampick.common.swagger.description.admin;

public class DeleteExclutionSignUpSwaggerDescription {

    public static final String EXCLUTION_SIGN_UP_DESCRIPTION =

            "<strong>가입을 심사중인 사장님의 아이디를 반려(삭제) 합니다.</strong>" +
            "<p>관리자 로그인이 필요한 기능입니다. 상단 Authorize 에 토큰값을 입력 후 이용해주세요.</p>"
            ;

    public static final String EXCLUTION_SIGN_UP_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
            "<p>NEP - 이미 처리된 권한 (400)</p>" +
            "<p>NP - 권한 없음 (403)</p>" +
            "<p>DBE - 데이터베이스 에러 (500)</p>"
            ;

}

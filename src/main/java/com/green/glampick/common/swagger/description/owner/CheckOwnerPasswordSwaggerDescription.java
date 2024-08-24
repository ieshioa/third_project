package com.green.glampick.common.swagger.description.owner;

public class CheckOwnerPasswordSwaggerDescription {
    public static final String CHECK_OWNER_PASSWORD_DESCRIPTION =

            "<strong>로그인 된 사장님의 비밀번호를 체크합니다.</strong>" +
                    "<p>로그인이 필요한 기능입니다. 상단 Authorize 에 토큰값을 입력 후 이용해주세요.</p>" +
                    "<p>-------------------------------------------------</p>" +
                    "<p>password : 비밀번호 (필수)</p>"
            ;

    public static final String CHECK_OWNER_PASSWORD_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
                    "<p>MNF - 찾을 수 없는 회원정보 (400)</p>" +
                    "<p>NMP - 비밀번호 불일치 (400)</p>" +
                    "<p>DBE - 데이터베이스 에러 (500)</p>"
            ;
}

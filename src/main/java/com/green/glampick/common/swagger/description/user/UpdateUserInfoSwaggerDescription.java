package com.green.glampick.common.swagger.description.user;

public class UpdateUserInfoSwaggerDescription {

    public static final String USER_INFO_UPDATE_DESCRIPTION =

            "<strong>로그인한 유저의 정보를 수정합니다.</strong>" +
            "<p>로그인이 필요한 기능입니다. 상단 Authorize 에 토큰값을 입력 후 이용해주세요.</p>" +
            "<p>-------------------------------------------------</p>" +
            "<p>userNickname : 유저 닉네임</p>" +
            "<p>userPw : 유저 패스워드</p>" +
            "<p>userPhone : 유저 휴대폰번호</p>" +
            "<p>-------------------------------------------------</p>" +
            "<p>이미지는 \"mf\" 부분에 파일 선택하여 업로드가 가능합니다.</p>" +
            "<p>유저 휴대폰번호는 회원가입 시, 사용한 API 를 이용하여 검증절차를 가지고 수정이 진행됩니다.</p>"
            ;

    public static final String USER_INFO_UPDATE_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
            "<p>CU - 미 로그인 (400)</p>" +
            "<p>NU - 존재하지 않는 유저 (400)</p>" +
            "<p>NP - 권한 없음 (403)</p>" +
            "<p>DBE - 데이터베이스 에러 (500)</p>"
            ;

}

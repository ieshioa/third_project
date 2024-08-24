package com.green.glampick.common.swagger.description.user;

public class getUserInfoSwaggerDescription {

    public static final String USER_INFO_DESCRIPTION =

            "<strong>로그인한 유저의 정보를 불러옵니다.</strong>" +
            "<p>로그인이 필요한 기능입니다. 상단 Authorize 에 토큰값을 입력 후 이용해주세요.</p>" +
            "<p>-------------------------------------------------</p>" +
            "<p>userEmail : 유저 이메일</p>" +
            "<p>userName : 유저 성함</p>" +
            "<p>userNickname : 유저 성함</p>" +
            "<p>userPw : 유저 비밀번호</p>" +
            "<p>userPhone : 유저 휴대폰번호</p>" +
            "<p>userProfileImage : 유저 프로필 이미지</p>"
            ;

    public static final String USER_INFO_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
            "<p>CU - 미 로그인 (400)</p>" +
            "<p>NU - 존재하지 않는 유저 (400)</p>" +
            "<p>NP - 권한 없음 (403)</p>" +
            "<p>DBE - 데이터베이스 에러 (500)</p>"
            ;

}

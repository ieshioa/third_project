package com.green.glampick.common.swagger.description.user;

public class GetUserFavoriteGlampingSwaggerDescription {

    public static final String USER_FAVORITE_LIST_DESCRIPTION =

            "<strong>로그인한 유저의 관심 글램핑 리스트를 모두 불러옵니다.</strong>" +
            "<p>로그인이 필요한 기능입니다. 상단 Authorize 에 토큰값을 입력 후 이용해주세요.</p>"
            ;

    public static final String USER_FAVORITE_LIST_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
            "<p>CU - 미 로그인 (400)</p>" +
            "<p>NG - 존재하지 않는 글램핑 (400)</p>" +
            "<p>NP - 권한 없음 (403)</p>" +
            "<p>DBE - 데이터베이스 에러 (500)</p>"
            ;

}

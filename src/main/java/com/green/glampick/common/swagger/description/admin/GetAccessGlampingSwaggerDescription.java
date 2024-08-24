package com.green.glampick.common.swagger.description.admin;

public class GetAccessGlampingSwaggerDescription {

    public static final String ACCESS_GLAMPING_DESCRIPTION =

            "<strong></strong>" +
            "<p>관리자 로그인이 필요한 기능입니다. 상단 Authorize 에 토큰값을 입력 후 이용해주세요.</p>"
            ;

    public static final String ACCESS_GLAMPING_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
            "<p>NG - 존재하지 않는 글램핑 정보 (400)" +
            "<p>NP - 권한 없음 (403)</p>" +
            "<p>DBE - 데이터베이스 에러 (500)</p>"
            ;

}

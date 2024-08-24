package com.green.glampick.common.swagger.description.owner;

public class GetOwnerInfoSwaggerDescription {
    public static final String GET_OWNER_INFO_DESCRIPTION =

            "<strong>로그인한 사장의 회원 정보를 불러옵니다.</strong>" +
                    "<p>로그인이 필요한 기능입니다. 상단 Authorize 에 토큰값을 입력 후 이용해주세요.</p>"
            ;

    public static final String GET_OWNER_INFO_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
                    "<p>MNF - 찾을 수 없는 회원정보 (400)</p>" +
                    "<p>DBE - 데이터베이스 에러 (500)</p>"
            ;
}

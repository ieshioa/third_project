package com.green.glampick.common.swagger.description.owner;

public class PutGlampingSwaggerDescription {

    public static final String UPDATE_GLAMPING_DESCRIPTION =

            "<strong>로그인한 사장이 글램핑 정보를 수정합니다.</strong>" +
            "<p>로그인이 필요한 기능입니다. 상단 Authorize 에 토큰값을 입력 후 이용해주세요.</p>" +
            "<p>-------------------------------------------------</p>" +
            "<p>glampId : 글램핑 PK (필수)</p>" +
            "<p>glampName : 글램핑 이름</p>" +
            "<p>glampCall : 글램핑 전화번호</p>" +
            "<p>glampLocation : 글램핑 주소</p>" +
            "<p>region : 지역 분류</p>" +
            "<p>extraCharge : 추가 인원에 대한 추가 요금 (선택 입력)</p>" +
            "<p>intro : 글램핑 소개</p>" +
            "<p>basic : 글램핑 기본정보</p>" +
            "<p>notice : 글램핑 이용안내</p>" +
            "<p>traffic : 글램핑 추가 위치 정보</p>"
            ;

    public static final String UPDATE_GLAMPING_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
            "<p>MNF - 찾을 수 없는 회원정보 (400)</p>" +
            "<p>NMG - 찾을 수 없는 글램핑 정보 (400)</p>" +
            "<p>DL - 중복된 글램핑 위치 (400)</p>" +
            "<p>NP - 권한 없음 (403)</p>" +
            "<p>DBE - 데이터베이스 에러 (500)</p>" +
            "<p>INVALID_ENTITY - PK 입력 오류 (400)</p>"
            ;

}

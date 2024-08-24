package com.green.glampick.common.swagger.description.glamping;

public class GetSearchGlampingSwaggerDescription {

    public static final String SEARCH_GLAMPING_DESCRIPTION =

            "<strong>검색 조건에 맞는 글램핑 검색 결과리스트를 가져옵니다.</strong>" +
            "<p>-------------------------------------------------</p>" +
            "<p>region : 지역이름</p> " +
            "<p>inDate : 체크인 날짜</p> " +
            "<p>ourDate : 체크아웃 날짜</p>" +
            "<p>people : 인원수</p> " +
            "<p>-------------------------------------------------</p>" +
            "<p>searchWord : 검색어 (필수값이 아닙니다.)</p> " +
            "<p>sortType : 검색 결과 정렬 기준 (필수값이 아닙니다.)</p>" +
            "<p>filter : 선택된 서비스 (필수값이 아닙니다.)</p>" +
            "<p>page : 선택한 PAGE (필수값이 아닙니다.)</p>"
            ;

    public static final String SEARCH_GLAMPING_RESPONSE_ERROR_CODE =

            "<strong>해당 코드는 성공코드 입니다.</strong>" +
            "<p>RN - 검색 결과 존재하지 않음 (200)</p>" +
            "<p>-------------------------------------------------</p>" +
            "<strong>발생 가능한 에러코드</strong>" +
            "<p>VF - 유효성 검사 실패 (400)</p>" +
            "<p>WP - 인원 정보 오입력 (400)</p>" +
            "<p>WD - 날짜 정보 오입력 (400)</p>" +
            "<p>DBE - 데이터베이스 에러 (500)</p>"
            ;

}

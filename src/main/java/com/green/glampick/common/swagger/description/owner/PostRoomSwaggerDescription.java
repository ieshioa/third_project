package com.green.glampick.common.swagger.description.owner;

public class PostRoomSwaggerDescription {

    public static final String POST_ROOM_DESCRIPTION =

            "<strong>로그인한 사장이 객실 정보를 등록합니다.</strong>" +
            "<p>로그인이 필요한 기능입니다. 상단 Authorize 에 토큰값을 입력 후 이용해주세요.</p>" +
            "<p>-------------------------------------------------</p>" +
            "<p>glampId : 글램핑 PK (필수)</p>" +
            "<p>roomName : 객실명 (필수)</p>" +
            "<p>price : 객실 가격 (필수)</p>" +
            "<p>peopleNum : 기준 인원 (필수)</p>" +
            "<p>peopleMax : 최대 인원 (필수)</p>" +
            "<p>inTime : 입실 시간 (필수)</p>" +
            "<p>outTime : 퇴실 시간 (필수)</p>" +
            "<p>service : 객실 서비스</p>" +
            "<p>roomImg : 객실 이미지 (필수)</p>" +
            "<p>-------------------------------------------------</p>" +
            "<strong>객실 서비스 항목은 [1,2,3] 형태로 작성해야 합니다.</strong>" +
            "<p>[1] : 물놀이</p>" +
            "<p>[2] : 오션뷰</p>" +
            "<p>[3] : 마운틴뷰</p>" +
            "<p>[4] : 반려동물 동반가능</p>" +
            "<p>[5] : 바베큐</p>" +
            "<p>[6] : 개인 화장실</p>" +
            "<p>[7] : 와이파이</p>" +
            "<p>-------------------------------------------------</p>" +
            "<strong> 시간 입력 형식 = 시:분:초  ex) 12:00:00 </strong>"
            ;

    public static final String POST_ROOM_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
            "<p>MNF - 찾을 수 없는 회원정보 (400)</p>" +
            "<p>NMG - 찾을 수 없는 글램핑 정보 (400)</p>" +
            "<p>NF - 사진 첨부 오류 (400)</p>" +
            "<p>PE - 잘못된 인원 선택 (400)</p>" +
            "<p>FE - 사진 등록 오류 (400)</p>" +
            "<p>DBE - 데이터베이스 오류 (500)</p>" +
            "<p>INVALID_PARAMETER - 입력 오류 (500)</p>" +
            "<p>INVALID_ENTITY - PK 입력 오류 (400)</p>"
            ;

}

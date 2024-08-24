package com.green.glampick.common.swagger.description.owner;

public class DeleteRoomSwaggerDescription {
    public static final String DELETE_ROOM_DESCRIPTION =

            "<strong>객실을 삭제합니다.</strong>" +
                    "<p>로그인이 필요한 기능입니다. 상단 Authorize 에 토큰값을 입력 후 이용해주세요.</p>" +
                    "<p>-------------------------------------------------</p>" +
                    "<p>roomId : 객실 PK (필수)</p>"
            ;

    public static final String DELETE_ROOM_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
                    "<p>MNF - 찾을 수 없는 회원정보 (400)</p>" +
                    "<p>NMR - 객실 PK 입력이 잘못됨 (400)</p>" +
                    "<p>NP - 권한 없음 (403)</p>" +
                    "<p>DBE - 데이터베이스 에러 (500)</p>" +
                    "<p>INVALID_ENTITY - PK 입력 오류 (400)</p>"
            ;
}

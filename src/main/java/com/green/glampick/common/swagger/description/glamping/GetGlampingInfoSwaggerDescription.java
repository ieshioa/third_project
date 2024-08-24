package com.green.glampick.common.swagger.description.glamping;

public class GetGlampingInfoSwaggerDescription {

    public static final String GLAMPING_INFO_DESCRIPTION =

            "<strong>글램핑 상세 정보를 가져옵니다.</strong>" +
            "<p>-------------------------------------------------</p>" +
            "<p>변수명 glampId : 글램프 PK  //  ex)23 </p>"  +
            "<p>변수명 inDate : 체크인 일자  //  ex)2024-06-10 </p>"  +
            "<p>변수명 outDate : 체크아웃 일자  //  ex)2024-06-15 </p>"  +
            "<p>변수명 status : 상태 코드 </p>" +
            "<p> 0 -> 처음 5개보기 </p> " +
            "<p>-------------------------------------------------</p>" +
            "<strong>[글램핑 정보]</strong>" +
            "<p> glampId: 글램핑 PK</p>" +
            "<p> glampName:  글램핑장 이름 </p>" +
            "<p> glampPic: 글램핑 사진 </p>" +
            "<p> starPointAvg: 별점 </p>" +
            "<p> glampLocation: 글램핑장 주소</p>" +
            "<p> glampIntro: 글램핑 소개글</p>" +
            "<p> infoBasic: 기본 정보</p>" +
            "<p> infoParking: 주차장 정보</p>" +
            "<p> infoNotice: 유의 사항</p>" +
            "<p> isFav: 값 0 -> 좋아요x 값 1 -> 좋아요등록o</p>" +
            "<strong>[리뷰 정보]</strong>" +
            "<p> userName: 유저닉네임</p>" +
            "<p> content: 리뷰내용</p>" +
            "<p> countReviewUsers: 리뷰인원수</p>" +
            "<strong>[객실정보]</strong>" +
            "<p> roomPics: 객실 사진</p>" +
            "<p> roomId: 객실 PK</p>" +
            "<p> roomName: 객실 명</p>" +
            "<p> roomPrice: 객실 가격</p>" +
            "<p> roomNumPeople: 객실 기본인원 수</p>" +
            "<p> roomMaxPeople: 객실 최대인원 수</p>" +
            "<p> checkInTime: 체크인 시간</p>" +
            "<p> checkOutTime: 체크아웃 시간</p>"
            ;

    public static final String GLAMPING_INFO_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
            "<p>DBE - 데이터베이스 에러 (500)</p>"
            ;

}

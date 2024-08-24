package com.green.glampick.common.swagger.description.user;

public class PostUserReviewSwaggerDescription {

    public static final String USER_REVIEW_DESCRIPTION =

            "<strong>로그인한 유저가 이용한 글램핑에 대한 리뷰를 작성합니다.</strong>" +
            "<p>로그인이 필요한 기능입니다. 상단 Authorize 에 토큰값을 입력 후 이용해주세요.</p>" +
            "<p>-------------------------------------------------</p>" +
            "<p>glampId : 글램핑 PK</p>" +
            "<p>roomId : 객실 PK</p>" +
            "<p>reviewContent : 작성할 리뷰 내용</p>" +
            "<p>reviewStarPoint : 작성할 별점</p>" +
            "<p>-------------------------------------------------</p>" +
            "<p>이미지 업로드는 포스트맨을 이용해주세요.</p>" +
            "<p>Image Key : mf</p>"

            ;

    public static final String USER_REVIEW_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
            "<p>CU - 미 로그인 (400)</p>" +
            "<p>NU - 존재하지 않는 유저 (400)</p>" +
            "<p>NP - 권한 없음 (403)</p>" +
            "<p>DBE - 데이터베이스 에러 (500)</p>"
            ;

}

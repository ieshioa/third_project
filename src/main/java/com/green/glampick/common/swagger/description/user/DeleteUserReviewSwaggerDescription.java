package com.green.glampick.common.swagger.description.user;

public class DeleteUserReviewSwaggerDescription {

    public static final String USER_REVIEW_REMOVE_DESCRIPTION =

            "<strong>로그인한 유저가 작성한 리뷰를 선택하여 삭제합니다.</strong>" +
            "<p>로그인이 필요한 기능입니다. 상단 Authorize 에 토큰값을 입력 후 이용해주세요.</p>" +
            "<p>-------------------------------------------------</p>" +
            "<p>reviewId : 리뷰 PK</p>"
            ;

    public static final String USER_REVIEW_REMOVE_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
            "<p>CU - 미 로그인 (400)</p>" +
            "<p>NU - 존재하지 않는 유저 (400)</p>" +
            "<p>NR - 존재하지 않는 리뷰 (400)</p>" +
            "<p>NP - 권한 없음 (403)</p>" +
            "<p>DBE - 데이터베이스 에러 (500)</p>"
            ;

}

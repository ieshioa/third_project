package com.green.glampick.common.swagger.description.owner;

public class PostOwnerReviewSwaggerDescription {

    public static final String POST_OWNER_REVIEW_DESCRIPTION =

            "<strong>로그인한 사장이 유저가 작성한 리뷰에 대한 답글을 작성합니다.</strong>" +
            "<p>-------------------------------------------------</p>" +
            "<p> glampId : 글램프 PK   ex)35 </p>" +
            "<p> reviewId : 리뷰 PK </p>" +
            "<p> reviewComment : 사장님 작성 리뷰 내용 ex)잘 이용하셨쎄요? </p>";

    public static final String POST_OWNER_REVIEW_RESPONSE_ERROR_CODE =

            "<strong>발생 가능한 에러코드</strong>" +
            "<p>DBE - 데이터베이스 에러 (500)</p>" +
            "<p>INVALID_ENTITY - PK 입력 오류 (400)</p>"
            ;

}

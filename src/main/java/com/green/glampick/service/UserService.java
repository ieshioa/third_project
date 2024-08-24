package com.green.glampick.service;

import com.green.glampick.dto.request.user.*;
import com.green.glampick.dto.response.user.*;
import com.green.glampick.entity.ReviewImageEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    //  마이페이지 - 예약 내역 불러오기  //
    ResponseEntity<?super GetBookResponseDto> getBook(GetBookRequestDto dto);

    //  마이페이지 - 예약 취소하기  //
    ResponseEntity<?super CancelBookResponseDto> cancelBook(CancelBookRequestDto dto);

    //  마이페이지 - 리뷰 작성하기  //
    ResponseEntity<?super PostReviewResponseDto> postReview(PostReviewRequestDto dto, List<MultipartFile> mf);

    //  마이페이지 - 리뷰 삭제하기  //
    ResponseEntity<?super DeleteReviewResponseDto> deleteReview(DeleteReviewRequestDto dto);

    //  마이페이지 - 리뷰 불러오기  //
    ResponseEntity<?super GetReviewResponseDto> getReview(GetReviewRequestDto dto);

    //  마이페이지 - 관심 글램핑 불러오기  //
    ResponseEntity<?super GetFavoriteGlampingResponseDto> getFavoriteGlamping(GetFavoriteGlampingRequestDto dto);

    //  마이페이지 - 내 정보 불러오기  //
    ResponseEntity<?super GetUserResponseDto> getUser(GetUserRequestDto dto);

    //  마이페이지 - 내 정보 수정하기  //
    ResponseEntity<?super UpdateUserResponseDto> updateUser(UpdateUserRequestDto dto,  MultipartFile mf);

    //  마이페이지 - 회원 탈퇴  //
    ResponseEntity<?super DeleteUserResponseDto> deleteUser(DeleteUserRequestDto dto);

    //  마이페이지 - 비밀번호 체크  //
    ResponseEntity<?super PostUserPasswordResponseDto> postUserPassword(PostUserPasswordRequestDto dto);

}

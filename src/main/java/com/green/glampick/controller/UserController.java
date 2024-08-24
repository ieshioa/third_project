package com.green.glampick.controller;

import com.green.glampick.dto.request.user.*;
import com.green.glampick.dto.response.login.PostSignUpResponseDto;
import com.green.glampick.dto.response.user.*;
import com.green.glampick.entity.ReviewImageEntity;
import com.green.glampick.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.green.glampick.common.swagger.description.login.PostSignUpSwaggerDescription.SIGN_UP_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.user.DeleteUserReviewSwaggerDescription.USER_REVIEW_REMOVE_DESCRIPTION;
import static com.green.glampick.common.swagger.description.user.DeleteUserReviewSwaggerDescription.USER_REVIEW_REMOVE_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.user.DeleteUserSwaggerDescription.USER_LEAVE_DESCRIPTION;
import static com.green.glampick.common.swagger.description.user.DeleteUserSwaggerDescription.USER_LEAVE_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.user.GetUserBookSwaggerDescription.USER_BOOK_DESCRIPTION;
import static com.green.glampick.common.swagger.description.user.GetUserBookSwaggerDescription.USER_BOOK_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.user.GetUserFavoriteGlampingSwaggerDescription.USER_FAVORITE_LIST_DESCRIPTION;
import static com.green.glampick.common.swagger.description.user.GetUserFavoriteGlampingSwaggerDescription.USER_FAVORITE_LIST_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.user.GetUserReviewSwaggerDescription.USER_REVIEW_VIEW_DESCRIPTION;
import static com.green.glampick.common.swagger.description.user.GetUserReviewSwaggerDescription.USER_REVIEW_VIEW_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.user.PostUserBookCancelSwaggerDescription.USER_BOOK_CANCEL_DESCRIPTION;
import static com.green.glampick.common.swagger.description.user.PostUserBookCancelSwaggerDescription.USER_BOOK_CANCEL_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.user.PostUserPasswordCheckedSwaggerDescription.USER_PASSWORD_CHECK_DESCRIPTION;
import static com.green.glampick.common.swagger.description.user.PostUserPasswordCheckedSwaggerDescription.USER_PASSWORD_CHECK_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.user.PostUserReviewSwaggerDescription.USER_REVIEW_DESCRIPTION;
import static com.green.glampick.common.swagger.description.user.PostUserReviewSwaggerDescription.USER_REVIEW_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.user.UpdateUserInfoSwaggerDescription.USER_INFO_UPDATE_DESCRIPTION;
import static com.green.glampick.common.swagger.description.user.UpdateUserInfoSwaggerDescription.USER_INFO_UPDATE_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.user.getUserInfoSwaggerDescription.USER_INFO_DESCRIPTION;
import static com.green.glampick.common.swagger.description.user.getUserInfoSwaggerDescription.USER_INFO_RESPONSE_ERROR_CODE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "유저 컨트롤러")
public class UserController {
    private final UserService service;

    //  유저 페이지 - 예약 내역 불러오기  //
    @GetMapping("/book")
    @Operation(summary = "예약내역 불러오기 (이진현, 김수찬)", description = USER_BOOK_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = USER_BOOK_RESPONSE_ERROR_CODE,
        content = @Content(
                mediaType = "application/json", schema = @Schema(implementation = GetBookResponseDto.class)))
    public ResponseEntity<?super GetBookResponseDto> getBook(@ParameterObject GetBookRequestDto dto) {
        return service.getBook(dto);
    }

    //  유저 페이지 - 예약 내역 취소하기  //
    @PostMapping("/book-cancel")
    @Operation(summary = "예약내역 취소하기 (이진현, 김수찬)", description = USER_BOOK_CANCEL_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = USER_BOOK_CANCEL_RESPONSE_ERROR_CODE,
        content = @Content(
                mediaType = "application/json", schema = @Schema(implementation = CancelBookResponseDto.class)))
    public ResponseEntity<?super CancelBookResponseDto> cancelBook(@RequestBody CancelBookRequestDto dto) {
        return service.cancelBook(dto);
    }

    //  유저 페이지 - 리뷰 작성하기  //
    @PostMapping(value = "/review", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "리뷰 작성하기 (이진현)", description = USER_REVIEW_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = USER_REVIEW_RESPONSE_ERROR_CODE,
        content = @Content(
                mediaType = "application/json", schema = @Schema(implementation = PostReviewResponseDto.class)))
    public ResponseEntity<?super PostReviewResponseDto> postReview(@RequestPart PostReviewRequestDto dto, @RequestPart(required = false) List<MultipartFile> mf) {
        return service.postReview(dto, mf);
    }

    //  유저 페이지 - 리뷰 삭제하기  //
    @DeleteMapping("/delete")
    @Operation(summary = "리뷰 삭제하기 (이진현)", description = USER_REVIEW_REMOVE_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = USER_REVIEW_REMOVE_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = DeleteReviewResponseDto.class)))
    public ResponseEntity<?super DeleteReviewResponseDto> deleteReview(@ParameterObject DeleteReviewRequestDto dto) {
        return service.deleteReview(dto);

    }

    //  유저 페이지 - 리뷰 불러오기  //
    @GetMapping("/review")
    @Operation(summary = "리뷰 불러오기 (김수찬)", description = USER_REVIEW_VIEW_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = USER_REVIEW_VIEW_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetReviewResponseDto.class)))
    public ResponseEntity<?super GetReviewResponseDto> getReview(@ParameterObject @ModelAttribute GetReviewRequestDto dto) {
        return service.getReview(dto);
    }

    //  유저 페이지 - 관심 글램핑 리스트 불러오기  //
    @GetMapping("/favorite-glamping")
    @Operation(summary = "관심 글램핑 리스트 불러오기 (이진현)", description = USER_FAVORITE_LIST_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = USER_FAVORITE_LIST_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetFavoriteGlampingResponseDto.class)))
    public ResponseEntity<?super GetFavoriteGlampingResponseDto> getFavoriteGlamping(@ParameterObject @ModelAttribute GetFavoriteGlampingRequestDto dto) {
        return service.getFavoriteGlamping(dto);
    }

    // 유저 페이지 - 유저 정보 불러오기  //
    @GetMapping
    @Operation(summary = "유저 정보 불러오기 (이진현, 김수찬)", description = USER_INFO_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = USER_INFO_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetUserResponseDto.class)))
    public ResponseEntity<?super GetUserResponseDto> getUser(@ParameterObject GetUserRequestDto dto) {
        return service.getUser(dto);
    }

    //  유저 페이지 - 유저 정보 수정하기  //
    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "유저 정보 수정하기 (이진현 김수찬)", description = USER_INFO_UPDATE_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = USER_INFO_UPDATE_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = UpdateUserResponseDto.class)))
    public ResponseEntity<?super UpdateUserResponseDto> updateUser
            (@RequestPart UpdateUserRequestDto dto, @RequestPart(required = false) MultipartFile mf)
    {
        return service.updateUser(dto, mf);
    }

    //  유저 페이지 - 회원 탈퇴  //
    @DeleteMapping
    @Operation(summary = "회원 탈퇴 (이진현)", description = USER_LEAVE_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = USER_LEAVE_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = DeleteUserResponseDto.class)))
    public ResponseEntity<?super DeleteUserResponseDto> deleteUser(@ParameterObject DeleteUserRequestDto dto) {
        return service.deleteUser(dto);
    }

    //  유저 페이지 - 비밀번호 재확인  //
    @PostMapping("password-check")
    @Operation(summary = "비밀번호 확인 (김수찬)", description = USER_PASSWORD_CHECK_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = USER_PASSWORD_CHECK_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PostUserPasswordResponseDto.class)))
    public ResponseEntity<? super PostUserPasswordResponseDto> postUserPassword(@RequestBody PostUserPasswordRequestDto dto) {
        return service.postUserPassword(dto);
    }

}


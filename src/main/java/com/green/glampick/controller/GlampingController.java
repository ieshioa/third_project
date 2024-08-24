package com.green.glampick.controller;

import com.green.glampick.dto.request.glamping.*;
import com.green.glampick.dto.response.glamping.*;
import com.green.glampick.dto.response.glamping.favorite.GetFavoriteGlampingResponseDto;
import com.green.glampick.dto.response.login.PostSignUpResponseDto;
import com.green.glampick.service.GlampingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.green.glampick.common.swagger.description.glamping.GetGlampingInfoSwaggerDescription.GLAMPING_INFO_DESCRIPTION;
import static com.green.glampick.common.swagger.description.glamping.GetGlampingInfoSwaggerDescription.GLAMPING_INFO_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.glamping.GetSearchGlampingSwaggerDescription.SEARCH_GLAMPING_DESCRIPTION;
import static com.green.glampick.common.swagger.description.glamping.GetSearchGlampingSwaggerDescription.SEARCH_GLAMPING_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.user.GetUserBookSwaggerDescription.USER_BOOK_RESPONSE_ERROR_CODE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/glamping")
@Tag(name = "글램핑 컨트롤러")
public class GlampingController {
    private final GlampingService service;


// 민지 =================================================================================================================

    //  지도 결과 불러오기  //
    @GetMapping("search/map")
    @Operation(summary = "지도 검색 결과 가져오기 (김민지)", description = "")
    @ApiResponse(responseCode = "200", description = "",
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetSearchMapListResponseDto.class)))
    public ResponseEntity<? super GetSearchMapListResponseDto> mapSearchList(@RequestParam String region) {
        return service.searchMapList(region);
    }

    //  글램핑 검색 결과 불러오기  //
    @GetMapping("search")
    @Operation(summary = "글램핑 검색 결과 가져오기 (김민지)", description = SEARCH_GLAMPING_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = SEARCH_GLAMPING_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetSearchGlampingListResponseDto.class)))
    public ResponseEntity<? super GetSearchGlampingListResponseDto> searchGlamping(@ParameterObject @ModelAttribute @Valid GlampingSearchRequestDto searchReq) {
        return service.searchGlamping(searchReq);
    }


// 강국 =================================================================================================================

    //  글램핑 상세 페이지 불러오기  //
    @GetMapping("info")
    @Operation(summary = "글램핑 상세 페이지 불러오기 (배강국)", description = GLAMPING_INFO_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = GLAMPING_INFO_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetGlampingInformationResponseDto.class)))
    public ResponseEntity<? super GetGlampingInformationResponseDto> infoGlampingDetail(@ParameterObject @ModelAttribute GetInfoRequestDto p) {
        return service.infoGlampingDetail(p);
    }
    /*
    @Operation(summary = "남은 객실 리스트 출력",
            description =
                    "<strong> 변수명 glampId : 글램프 PK </strong> <p>  ex)23 </p>"  +
                    "<strong> 변수명 status : 상태 코드 </strong>" +
                    "<p> 1 -> 남은 객실리스트 출력 </p>",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공에 대한 반환 값 입니다." +
                                    "<p> [객실정보] </p>" +
                                    "<p> roomPics: 객실 사진</p>" +
                                    "<p> roomId: 객실 PK</p>" +
                                    "<p> roomName: 객실 명</p>" +
                                    "<p> roomPrice: 객실 가격</p>" +
                                    "<p> roomNumPeople: 객실 기본인원 수</p>" +
                                    "<p> roomMaxPeople: 객실 최대인원 수</p>" +
                                    "<p> checkInTime: 체크인 시간</p>" +
                                    "<p> checkOutTime: 체크아웃 시간</p>"
                            ,
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetMoreRoomItemResponseDto.class)
                            ))})
    @GetMapping("info/moreRooms")//객실추가보기
    public ResponseEntity<? super GetMoreRoomItemResponseDto> moreDetailsRoom(@ParameterObject @ModelAttribute GetInfoRequestDto p) {
        return service.moreDetailsRoom(p);
    }
     */
    @Operation(
            summary = "관심 글램핑 등록 (배강국)" ,
            description = "<strong> 변수명 glampId :  글램프 PK </strong> <p>  ex)23 </p>",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공에 대한 반환 값 입니다." +
                                    "<p> [글램핑 관심등록] </p>" +
                                    "<p> result: 0 >> 글램핑 관심 등록 </p>" +
                                    "<p> result: 1 >> 글램핑 관심 취소</p>",

                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetFavoriteGlampingResponseDto.class)))})
    @GetMapping("favorite")// 관심글램핑등록
    public ResponseEntity<? super GetFavoriteGlampingResponseDto> favoriteGlamping(@ParameterObject @ModelAttribute GetFavoriteRequestDto p) {
        return service.favoriteGlamping(p);
    }
    @Operation(summary = "글램핑 리뷰 페이지 (배강국)",
            description = "<strong> 변수명 glampId : 글램프 PK </strong> <p>  ex)35 </p>" +
                          "<strong> 변수명 page : 페이지 값 </strong> <p> ex) 1 </p>",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공에 대한 반환 값 입니다." +
                            "<p> avgStarPoint: 글램핑 전체 평점 </p>" +
                            "<p> [객실 정보]" +
                            "<p> userProfileImage: 유저 프사 </p>" +
                            "<p> userNickName: 유저 닉네임</p>" +
                            "<p> starPoint: 별점 </p>" +
                            "<p> createdAt: 리뷰 작성 날짜</p>" +
                            "<p> userReviewContent: 유저가 작성한 리뷰내용 </p>" +
                            "<p> ownerReviewContent: 사장이 작성한 리뷰내용 </p>" +
                            "<p> reviewImages:  유저가 올린 리뷰사진들 </p>" +
                            "<p> roomNames: 글램핑장의 객실 이름들 </p>" ,

                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetGlampingReviewInfoResponseDto.class)
                            ))})
    @GetMapping("{glamp_id}/review")//리뷰페이지
    public ResponseEntity<? super GetGlampingReviewInfoResponseDto> infoReviewList(@ParameterObject @ModelAttribute ReviewInfoRequestDto p) {
        return service.infoReviewList(p);
    }
    @Operation(summary = "리뷰이미지 더보기 (배강국)",
            description =
                    "<strong> 변수명 glampId : 글램프 PK </strong> <p>  ex)35 </p>" +
                    "<strong> 변수명 page : 페이지 값 </strong> <p> ex) 1 </p>",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공에 대한 반환 값 입니다." +
                                          "<p> moreReviewImage:  유저가 올린 리뷰사진들 </p>" ,
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetMoreReviewImgageResponseDto.class)
                            ))})
    @GetMapping
    public ResponseEntity<? super GetMoreReviewImgageResponseDto> moreReviewImage(@ParameterObject @ModelAttribute GetMoreReviewImageRequestDto p) {
        return service.moreReviewImage(p);
    }

    @Operation(summary = "객실 사진 더보기 (배강국)",
            description =
                    "<strong> 변수명 glampId : 글램프 PK </strong> <p>  ex)3 </p>" +
                    "<strong> 변수명 page : 페이지 값 </strong> <p> ex) 1 </p>",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "성공에 대한 반환 값 입니다." +
                                    "<p> moreReviewImage:  유저가 올린 리뷰사진들 </p>" ,
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetMoreRoomImageResponseDto.class)
                            ))})
    @GetMapping("info/moreRoomImages")
    public ResponseEntity<? super GetMoreRoomImageResponseDto> moreRoomImage(@ParameterObject @ModelAttribute GetMoreRoomImageRequestDto p) {
        return service.moreRoomImage(p);
    }

}

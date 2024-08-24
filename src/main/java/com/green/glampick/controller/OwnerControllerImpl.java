package com.green.glampick.controller;

import com.green.glampick.controller.controllerInterface.OwnerController;
import com.green.glampick.dto.object.owner.OwnerBookCountListItem;
import com.green.glampick.dto.object.owner.OwnerBookDetailListItem;
import com.green.glampick.dto.request.owner.*;
import com.green.glampick.dto.request.ReviewPatchRequestDto;
import com.green.glampick.dto.response.owner.patch.PatchOwnerPeakResponseDto;
import com.green.glampick.dto.response.owner.patch.PatchOwnerReviewInfoResponseDto;
import com.green.glampick.dto.request.owner.ReviewGetCancelRequestDto;
import com.green.glampick.dto.request.owner.ReviewGetRevenueRequestDto;
import com.green.glampick.dto.request.owner.ReviewGetRoomRequestDto;
import com.green.glampick.dto.request.owner.ReviewGetStarRequestDto;
import com.green.glampick.dto.response.owner.get.GetGlampingCancelResponseDto;
import com.green.glampick.dto.response.owner.get.GetOwnerPopularRoomResponseDto;
import com.green.glampick.dto.response.owner.get.GetOwnerRevenueResponseDto;
import com.green.glampick.dto.response.owner.get.GetOwnerStarResponseDto;
import com.green.glampick.module.GlampingModule;
import com.green.glampick.dto.request.user.GetReviewRequestDto;
import com.green.glampick.dto.response.owner.*;
import com.green.glampick.dto.response.owner.get.*;
import com.green.glampick.dto.response.owner.post.PostRoomInfoResponseDto;
import com.green.glampick.dto.response.owner.put.PatchOwnerInfoResponseDto;
import com.green.glampick.dto.response.user.GetReviewResponseDto;
import com.green.glampick.security.AuthenticationFacade;
import com.green.glampick.service.OwnerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.green.glampick.common.swagger.description.owner.CheckOwnerPasswordSwaggerDescription.CHECK_OWNER_PASSWORD_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.CheckOwnerPasswordSwaggerDescription.CHECK_OWNER_PASSWORD_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.DeleteRoomSwaggerDescription.DELETE_ROOM_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.DeleteRoomSwaggerDescription.DELETE_ROOM_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.GetBookFromUserSwaggerDescription.BOOK_FROM_USER_REVIEW_VIEW_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.GetBookFromUserSwaggerDescription.BOOK_FROM_USER_REVIEW_VIEW_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.GetCancelDescription.OWNER_CANCEL_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.GetCancelDescription.OWNER_CANCEL_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.GetGlampingBookCountDescription.BOOK_COUNT_FROM_OWNER_GLAMPING_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.GetGlampingBookCountDescription.BOOK_COUNT_RESPONSE_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.GetGlampingFromUserReviewSwaggerDescription.GLAMPING_FROM_USER_REVIEW_VIEW_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.GetGlampingFromUserReviewSwaggerDescription.GLAMPING_FROM_USER_REVIEW_VIEW_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.GetGlampingInfoSwaggerDescription.GET_GLAMPING_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.GetGlampingInfoSwaggerDescription.GET_GLAMPING_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.GetOwnerInfoSwaggerDescription.GET_OWNER_INFO_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.GetOwnerInfoSwaggerDescription.GET_OWNER_INFO_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.GetOwnerPopularRoomDescription.OWNER_ROOM_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.GetOwnerPopularRoomDescription.OWNER_ROOM_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.GetOwnerStarDescription.OWNER_STAR_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.GetOwnerStarDescription.OWNER_STAR_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.GetRevenueDescription.OWNER_REVENUE_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.GetRevenueDescription.OWNER_REVENUE_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.GetRoomInfoSwaggerDescription.GET_ROOM_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.GetRoomInfoSwaggerDescription.GET_ROOM_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.GetRoomListSwaggerDescription.GET_ROOM_LIST_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.GetRoomListSwaggerDescription.GET_ROOM_LIST_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.PatchOwnerInfoSwaggerDescription.PATCH_OWNER_INFO_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.PatchOwnerInfoSwaggerDescription.PATCH_OWNER_INFO_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.PostGlampingSwaggerDescription.POST_GLAMPING_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.PostGlampingSwaggerDescription.POST_GLAMPING_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.PostOwnerReviewSwaggerDescription.POST_OWNER_REVIEW_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.PostOwnerReviewSwaggerDescription.POST_OWNER_REVIEW_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.PostRoomSwaggerDescription.POST_ROOM_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.PostRoomSwaggerDescription.POST_ROOM_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.PutGlampingImageSwaggerDescription.UPDATE_GLAMPING_IMAGE_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.PutGlampingImageSwaggerDescription.UPDATE_GLAMPING_IMAGE_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.PutGlampingSwaggerDescription.UPDATE_GLAMPING_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.PutGlampingSwaggerDescription.UPDATE_GLAMPING_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.PutRoomSwaggerDescription.PUT_ROOM_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.PutRoomSwaggerDescription.PUT_ROOM_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.owner.WithdrawOwnerSwaggerDescription.WITHDRAW_OWNER_DESCRIPTION;
import static com.green.glampick.common.swagger.description.owner.WithdrawOwnerSwaggerDescription.WITHDRAW_OWNER_RESPONSE_ERROR_CODE;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owner")
@Tag(name = "사장님 컨트롤러")
public class OwnerControllerImpl implements OwnerController {

    private final OwnerService service;
    private final AuthenticationFacade authenticationFacade;

// 민지 =================================================================================================================

    //  사장님 페이지 - 글램핑 정보 등록하기  //
    @PostMapping(value = "glamping", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "글램핑 정보 등록 (김민지)", description = POST_GLAMPING_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = POST_GLAMPING_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = OwnerSuccessResponseDto.class)))
    public ResponseEntity<? super OwnerSuccessResponseDto> createGlamping(@RequestPart @Valid GlampingPostRequestDto req
            , @RequestPart(required = false) MultipartFile glampImg) {
        return service.postGlampingInfo(req, glampImg);
    }

    //  사장님 페이지 - 글램핑 정보 수정하기  //
    @PutMapping("glamping")
    @Operation(summary = "글램핑 정보 수정 (김민지)", description = UPDATE_GLAMPING_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = UPDATE_GLAMPING_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = OwnerSuccessResponseDto.class)))
    public ResponseEntity<? super OwnerSuccessResponseDto> updateGlamping(@RequestBody GlampingPutRequestDto req) {
        return service.updateGlampingInfo(req);
    }

    //  사장님 페이지 - 글램핑 대표 이미지 수정하기  //
    @PatchMapping(value ="glamping/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "글램핑 대표 이미지 수정 (김민지)", description = UPDATE_GLAMPING_IMAGE_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = UPDATE_GLAMPING_IMAGE_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = OwnerSuccessResponseDto.class)))
    public ResponseEntity<? super OwnerSuccessResponseDto> updateGlampingImage(@RequestPart MultipartFile image, @RequestPart @Schema(example = "1") long glampId) {
        return service.changeGlampingImage(image, glampId);
    }

    //  사장님 페이지 - 객실 정보 등록하기  //
    @PostMapping(value = "room", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "객실 정보 등록 (김민지)", description = POST_ROOM_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = POST_ROOM_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PostRoomInfoResponseDto.class)))
    public ResponseEntity<? super OwnerSuccessResponseDto> createRoom(@RequestPart @Valid RoomPostRequestDto req
            , @RequestPart List<MultipartFile> roomImg) {
        return service.postRoomInfo(req, roomImg);
    }

    //  사장님 페이지 - 객실 정보 수정하기  //
    @PutMapping(value = "room", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "객실 정보 수정 (김민지)", description = PUT_ROOM_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = PUT_ROOM_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = OwnerSuccessResponseDto.class)))
    public ResponseEntity<? super OwnerSuccessResponseDto> updateRoom(@RequestPart(required = false) List<MultipartFile> addImg
            , @RequestPart(required = false) RoomPutRequestDto req) {
        return service.updateRoomInfo(addImg, req);
    }

    //  사장님 페이지 - 객실 삭제하기  //
    @DeleteMapping("room/{room_id}")
    @Operation(summary = "객실 삭제 (김민지)", description = DELETE_ROOM_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = DELETE_ROOM_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = OwnerSuccessResponseDto.class)))
    public ResponseEntity<? super OwnerSuccessResponseDto> deleteRoom(@PathVariable("room_id") Long roomId) {
        return service.deleteRoom(roomId);
    }

    // 글램핑 get
    @GetMapping("glamping")
    @Operation(summary = "글램핑 정보 불러오기 (김민지)", description = GET_GLAMPING_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = GET_GLAMPING_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetGlampingInfoResponseDto.class)))
    public ResponseEntity<? super GetGlampingInfoResponseDto> getGlamping() {
        return service.getGlamping();
    }

    // 객실 list get
    @GetMapping("room/{glamp_id}")
    @Operation(summary = "객실 정보 리스트 불러오기 (김민지)", description = GET_ROOM_LIST_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = GET_ROOM_LIST_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetRoomListResponseDto.class)))
    public ResponseEntity<? super GetRoomListResponseDto> getRoomList(@PathVariable("glamp_id") Long glampId) {
        return service.getRoomList(glampId);
    }

    // 객실 상세보기
    @GetMapping("room/{glamp_id}/{room_id}")
    @Operation(summary = "객실 정보 상세 불러오기 (김민지)", description = GET_ROOM_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = GET_ROOM_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetRoomInfoResponseDto.class)))
    public ResponseEntity<? super GetRoomInfoResponseDto> getRoomOne(@PathVariable("glamp_id") Long glampId
            , @PathVariable("room_id") Long roomId) {
        return service.getRoomOne(glampId, roomId);
    }

    // 비밀번호 확인
    @PostMapping("info")
    @Operation(summary = "사장님 비밀번호 확인 (김민지)", description = CHECK_OWNER_PASSWORD_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = CHECK_OWNER_PASSWORD_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = OwnerSuccessResponseDto.class)))
    public ResponseEntity<? super OwnerSuccessResponseDto> checkOwnerPassword(@RequestBody @Valid CheckPasswordRequestDto dto) {
        return service.checkOwnerPassword(dto);
    }

    // get - 사장님 정보 불러오기
    @GetMapping("info")
    @Operation(summary = "사장님 정보 불러오기 (김민지)", description = GET_OWNER_INFO_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = GET_OWNER_INFO_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = OwnerInfoResponseDto.class)))
    public ResponseEntity<? super OwnerInfoResponseDto> getOwnerInfo() {
        return service.getOwnerInfo();
    }

    // patch - 사장님 정보 수정
    @PatchMapping("info")
    @Operation(summary = "사장님 정보 수정 (김민지)", description = PATCH_OWNER_INFO_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = PATCH_OWNER_INFO_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PatchOwnerInfoResponseDto.class)))
    public ResponseEntity<? super PatchOwnerInfoResponseDto> updateOwnerInfo(@ModelAttribute @ParameterObject @Valid PatchOwnerInfoRequestDto dto) {
        return service.patchOwnerInfo(dto);
    }

    // patch - 탈퇴 승인 요청
    @PatchMapping("withdraw")
    @Operation(summary = "사장님 탈퇴 승인 요청 (김민지)", description = WITHDRAW_OWNER_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = WITHDRAW_OWNER_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = OwnerSuccessResponseDto.class)))
    public ResponseEntity<? super OwnerSuccessResponseDto> withdrawOwner() {
        return service.withdrawOwner();
    }

// 강국 =================================================================================================================

    /*  사장님 페이지 - 리뷰 답글 작성하기  */
    @PatchMapping("/review")
    @Operation(summary = "리뷰 답글 작성하기 (배강국)", description = POST_OWNER_REVIEW_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = POST_OWNER_REVIEW_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PatchOwnerReviewInfoResponseDto.class)))
    public ResponseEntity<? super PatchOwnerReviewInfoResponseDto> patchReview(@RequestBody @Valid ReviewPatchRequestDto p) {
        log.info("controller {}", p);
        p.setOwnerId(GlampingModule.ownerId(authenticationFacade));
        GlampingModule.roleCheck(authenticationFacade.getLoginUser().getRole());
        return service.patchReview(p);
    }

    /*  사장님 페이지 - 리뷰 불러오기  */
    @GetMapping("/review")
    @Operation(summary = "리뷰 불러오기 (배강국)", description = GLAMPING_FROM_USER_REVIEW_VIEW_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = GLAMPING_FROM_USER_REVIEW_VIEW_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetReviewResponseDto.class)))
    public ResponseEntity<? super GetReviewResponseDto> getReview(@ParameterObject @ModelAttribute GetReviewRequestDto dto) {
        log.info("controller p: {}", dto);

        long ownerId = GlampingModule.ownerId(authenticationFacade);
        GlampingModule.roleCheck(authenticationFacade.getLoginUser().getRole());
        dto.setOwnerId(ownerId);
        return service.getReview(dto);
    }
    /* 사장님 페이지 - 성수기 등록 */
    @PatchMapping("/room/{glampId}/peak")
    @Operation(summary = "성수기기간 & 가격 등록(배강국)", description = "작성필요")
    @ApiResponse(responseCode = "200", description = "작성필요",
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PatchOwnerPeakResponseDto.class)))
    public ResponseEntity<? super PatchOwnerPeakResponseDto> patchPeak(@PathVariable Long glampId, @ParameterObject @ModelAttribute PatchOwnerPeakRequestDto p) {

        GlampingModule.ownerId(authenticationFacade);
        GlampingModule.roleCheck(authenticationFacade.getLoginUser().getRole());

        return service.patchPeak(glampId, p);
    }
    /* 성수기 초기화 */
    @Operation(summary = "성수기 초기화 (배강국)",
            description =
                    "<strong> 변수명 </strong> glampId : 글램프 PK <p>  ex)2 </p>",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description =
                                    "<p> result: 수정실패 0 수정성공 1 </p>",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OwnerSuccessResponseDto.class)
                            ))})
    @DeleteMapping("room/{glampId}/peak")
    public ResponseEntity<? super OwnerSuccessResponseDto> delGlampingPeakPeriod(@PathVariable Long glampId) {
        GlampingModule.ownerId(authenticationFacade);
        GlampingModule.roleCheck(authenticationFacade.getLoginUser().getRole());
        return service.delGlampingPeakPeriod(glampId);
    }


    /* 사장님 페이지 - 성수기 기간 불러오기 */
    @Operation(summary = "성수기 기간 불러오기 (배강국)",
            description =
                    "<strong> 변수명 </strong> glampId : 글램프 PK <p>  ex)2 </p>",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description =
                                    "<p> startPeakDate: 성수기 시작 날 </p>" +
                                    "<p> endPeakDate: 성수기 끝나는 날 </p>" +
                                    "<p> percent: 가격인상 퍼센트 </p>",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GetGlampingPeakPeriodResponseDto.class)
                            ))})
    @GetMapping("room/{glampId}/peak")
    public ResponseEntity<? super GetGlampingPeakPeriodResponseDto> getGlampingPeakPeriod(@PathVariable Long glampId) {
        GlampingModule.roleCheck(authenticationFacade.getLoginUser().getRole());
        return service.getGlampingPeakPeriod(glampId);
    }

    /* 사장님 페이지 - 예약 내역 불러오기 */
    @GetMapping("/book")
    @Operation(summary = "디테일한 예약 내역 불러오기 (배강국)", description = BOOK_FROM_USER_REVIEW_VIEW_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = BOOK_FROM_USER_REVIEW_VIEW_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetOwnerBookListResponseDto.class)))
    public ResponseEntity<? super GetOwnerBookListResponseDto> getOwnerReservation(@ParameterObject @ModelAttribute ReservationGetRequestDto p) {

        long ownerId = GlampingModule.ownerId(authenticationFacade);
        GlampingModule.roleCheck(authenticationFacade.getLoginUser().getRole());
        p.setOwnerId(ownerId);
        log.info("controller p: ", p);
        List<OwnerBookDetailListItem> before = service.getReservationBeforeList(p);
        List<OwnerBookDetailListItem> complete = service.getReservationCompleteList(p);

        GetOwnerBookListResponseDto dto = new GetOwnerBookListResponseDto(before, complete);

        return dto.success(before, complete);
    }

    /* 예약 카운트 */
    @GetMapping("/book/count")
    @Operation(summary = "예약 건수 불러오기 (배강국)", description = BOOK_COUNT_FROM_OWNER_GLAMPING_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = BOOK_COUNT_RESPONSE_DESCRIPTION,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetOwnerBookListResponseDto.class)))
    public ResponseEntity<? super GetOwnerBookListResponseDto> getOwnerReservationCount(@ParameterObject @ModelAttribute ReservationGetRequestDto p) {

        long ownerId = GlampingModule.ownerId(authenticationFacade);
        GlampingModule.roleCheck(authenticationFacade.getLoginUser().getRole());
        p.setOwnerId(ownerId);

        List<OwnerBookCountListItem> totalCount = service.getTotalCount(p.getDate(),p.getOwnerId());

        GetOwnerBookListResponseDto dto = new GetOwnerBookListResponseDto(totalCount);

        return dto.success(totalCount);
    }
// 진현 =================================================================================================================

    // 이용 완료된 객실별 예약수
    @GetMapping("/poproom")
    @Operation(summary = "이용 완료된 객실별 예약수 (이진현)", description = OWNER_ROOM_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = OWNER_ROOM_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetOwnerPopularRoomResponseDto.class)))
    public ResponseEntity<? super GetOwnerPopularRoomResponseDto> getPopRoom(@ParameterObject ReviewGetRoomRequestDto dto) {
        return service.getPopRoom(dto);
    }

    // 평균 별점 및 관심 수
    @GetMapping("/starheart")
    @Operation(summary = "평균 별점, 관심 수 (이진현)", description = OWNER_STAR_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = OWNER_STAR_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetOwnerStarResponseDto.class)))
    public ResponseEntity<? super GetOwnerStarResponseDto> getStarRoom(@ParameterObject ReviewGetStarRequestDto dto) {
        return service.getStarRoom(dto);
    }

    // 예약 취소율
    @GetMapping("/glampingcancel")
    @Operation(summary = "예약 취소율 (이진현)", description = OWNER_CANCEL_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = OWNER_CANCEL_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetGlampingCancelResponseDto.class)))
    public ResponseEntity<? super GetGlampingCancelResponseDto> getGlampingCancelRoom(@ParameterObject ReviewGetCancelRequestDto dto) {
        return service.getGlampingCancelRoom(dto);
    }

    //매출
    @GetMapping("/revenue")
    @Operation(summary = "매출 (이진현)", description = OWNER_REVENUE_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = OWNER_REVENUE_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetOwnerRevenueResponseDto.class)))
    public ResponseEntity<? super GetOwnerRevenueResponseDto> getPopRoom(@ParameterObject ReviewGetRevenueRequestDto dto) {
        return service.getRevenue(dto);
    }


}


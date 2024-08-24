package com.green.glampick.controller;

import com.green.glampick.dto.request.admin.DeleteBannerRequestDto;
import com.green.glampick.dto.request.admin.exclusionGlampingRequestDto;
import com.green.glampick.dto.request.admin.exclusionSignUpRequestDto;
import com.green.glampick.dto.response.admin.*;
import com.green.glampick.service.AdminService;
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

import static com.green.glampick.common.swagger.description.admin.DeleteBannerSwaggerDescription.DELETE_BANNER_DESCRIPTION;
import static com.green.glampick.common.swagger.description.admin.DeleteBannerSwaggerDescription.DELETE_BANNER_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.admin.DeleteExclutionSignUpSwaggerDescription.EXCLUTION_SIGN_UP_DESCRIPTION;
import static com.green.glampick.common.swagger.description.admin.DeleteExclutionSignUpSwaggerDescription.EXCLUTION_SIGN_UP_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.admin.GetAccessGlampingSwaggerDescription.ACCESS_GLAMPING_DESCRIPTION;
import static com.green.glampick.common.swagger.description.admin.GetAccessGlampingSwaggerDescription.ACCESS_GLAMPING_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.admin.PatchAccessSignUpSwaggerDescription.ACCESS_SIGN_UP_DESCRIPTION;
import static com.green.glampick.common.swagger.description.admin.PatchAccessSignUpSwaggerDescription.ACCESS_SIGN_UP_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.admin.PostBannerSwaggerDescription.POST_BANNER_DESCRIPTION;
import static com.green.glampick.common.swagger.description.admin.PostBannerSwaggerDescription.POST_BANNER_RESPONSE_ERROR_CODE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "관리자 컨트롤러")
public class AdminController {
    private final AdminService service;

    //  관리자 페이지 - 사장님 회원가입 정보 불러오기  //
    @GetMapping("/business/owner")
    @Operation(summary = "사장님 회원가입 정보 불러오기 (김수찬)", description = "")
    @ApiResponse(responseCode = "200", description = "",
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetOwnerSignUpResponseDto.class)
            ))
    public ResponseEntity<? super GetOwnerSignUpResponseDto> getOwnerSignUpInfo (@RequestParam Long ownerId) {
        return service.getOwnerSignUpInfo(ownerId);
    }

    //  관리자 페이지 - 대기중인 사장님 회원가입 리스트 불러오기  //
    @GetMapping("/business/owner-list")
    @Operation(summary = "대기중인 사장님 회원가입 리스트 불러오기 (김수찬)", description = "")
    @ApiResponse(responseCode = "200", description = "",
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetOwnerSignUpResponseDto.class)
            ))
    public ResponseEntity<? super GetAccessOwnerSignUpListResponseDto> accessSignUpList () {
        return service.accessSignUpList();
    }

    //  관리자 페이지 - 사장님 회원가입 승인 처리하기  //
    @PatchMapping("/access/owner/sign-up")
    @Operation(summary = "사장님 회원가입 승인 처리하기 (김수찬)", description = ACCESS_SIGN_UP_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = ACCESS_SIGN_UP_RESPONSE_ERROR_CODE,
        content = @Content(
                mediaType = "application/json", schema = @Schema(implementation = PatchAccessOwnerSignUpResponseDto.class)
        ))
    public ResponseEntity<? super PatchAccessOwnerSignUpResponseDto> accessSignUp
            (@RequestParam Long ownerId) {
        return service.accessSignUp(ownerId);
    }

    //  관리자 페이지 - 사장님 회원가입 반려 처리하기 - 완료  //
    @DeleteMapping("/exclution/owner/sign-up")
    @Operation(summary = "사장님 회원가입 반려 처리하기 (김수찬) - 완료", description = EXCLUTION_SIGN_UP_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = EXCLUTION_SIGN_UP_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PatchAccessOwnerSignUpResponseDto.class)
            ))
    public ResponseEntity<? super DeleteExclusionOwnerSignUpResponseDto> exclusionSignUp(@RequestBody exclusionSignUpRequestDto dto) {
        return service.exclutionSignUp(dto);
    }

    //  관리자 페이지 - 메인 화면 배너 추가하기  //
    @PostMapping(value = "/banner", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "메인 배너 추가하기 (김수찬)", description = POST_BANNER_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = POST_BANNER_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PostBannerResponseDto.class)
            ))
    public ResponseEntity<? super PostBannerResponseDto> postBanner(@RequestPart List<MultipartFile> file) {
        return service.postBanner(file);
    }

    //  관리자 페이지 - 메인 화면 배너 삭제하기 - 완료  //
    @DeleteMapping("/banner")
    @Operation(summary = "메인 배너 삭제하기 (김수찬) - 완료", description = DELETE_BANNER_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = DELETE_BANNER_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = DeleteBannerResponseDto.class)
            ))
    public ResponseEntity<? super DeleteBannerResponseDto> deleteBanner(@RequestParam Long bannerId) {
        return service.deleteBanner(bannerId);
    }

    //  관리자 페이지 - 메인 화면 배너 불러오기  //
    @GetMapping("banner")
    @Operation(summary = "메인 배너 불러오기 (김수찬)", description = "")
    @ApiResponse(responseCode = "200", description = "",
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetBannerResponseDto.class)
            ))
    public ResponseEntity<? super GetBannerResponseDto> getBanner() {
        return service.getBanner();
    }

    //  관리자 페이지 - 승인 대기중인 글램핑장 리스트 불러오기  //
    @GetMapping("/glamping-list/owner")
    @Operation(summary = "승인 대기중인 글램핑장 리스트 불러오기 (김수찬)", description = "")
    @ApiResponse(responseCode = "200", description = "",
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetAccessGlampingListResponseDto.class)
            ))
    public ResponseEntity<? super GetAccessGlampingListResponseDto> getAccessGlampingList() {
        return service.getAccessGlampingList();
    }

    //  관리자 페이지 - 사장님 글램핑 등록 상세 정보 불러오기 - 완료  //
    @GetMapping("/glamping/owner")
    @Operation(summary = "사장님 글램핑 등록 정보 불러오기 (김수찬)", description = ACCESS_GLAMPING_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = ACCESS_GLAMPING_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetAccessGlampingInfoResponseDto.class)
            ))
    public ResponseEntity<? super GetAccessGlampingInfoResponseDto> getAccessGlamping(@RequestParam Long glampId) {
        return service.getAccessGlamping(glampId);
    }

    //  관리자 페이지 - 글램핑 등록 승인 처리하기  //
    @PatchMapping("/access/owner/glamping")
    @Operation(summary = "글램핑 등록 승인 처리하기 (김수찬)", description = "")
    @ApiResponse(responseCode = "200", description = "",
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PatchGlampingAccessResponseDto.class)
            ))
    public ResponseEntity<? super PatchGlampingAccessResponseDto> accessGlamping(@RequestParam Long glampId) {
        return service.accessGlamping(glampId);
    }

    //  관리자 페이지 - 글램핑 등록 반려 처리하기  //
    @PatchMapping("/exclution/owner/glamping")
    @Operation(summary = "글램핑 등록 반려 처리하기 (김수찬)", description = "")
    @ApiResponse(responseCode = "200", description = "",
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GlampingExclutionResponseDto.class)
            ))
    public ResponseEntity<? super GlampingExclutionResponseDto> exclutionGlamping(@RequestBody exclusionGlampingRequestDto dto) {
        return service.exclutionGlamping(dto);
    }

    //  관리자 페이지 - 회원탈퇴 대기 사장님 리스트 불러오기  //
    @GetMapping("/get-delete-list/owner")
    @Operation(summary = "회원탈퇴 대기 사장님 리스트 불러오기 (김수찬)", description = "")
    @ApiResponse(responseCode = "200", description = "",
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = getDeleteOwnerListResponseDto.class)
            ))
    public ResponseEntity<? super getDeleteOwnerListResponseDto> deleteOwnerList() {
        return service.deleteOwnerList();
    }

    //  관리자 페이지 - 사장님 회원탈퇴 승인 처리하기 - 완료  //
    @PatchMapping("/delete/owner")
    @Operation(summary = "사장님 회원탈퇴 처리하기 (김수찬) - 완료", description = "")
    @ApiResponse(responseCode = "200", description = "",
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PatchDeleteOwnerResponseDto.class)
            ))
    public ResponseEntity<? super PatchDeleteOwnerResponseDto> deleteOwner(@RequestParam Long ownerId) {
        return service.deleteOwner(ownerId);
    }
}

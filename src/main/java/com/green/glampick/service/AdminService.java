package com.green.glampick.service;

import com.green.glampick.dto.request.admin.DeleteBannerRequestDto;
import com.green.glampick.dto.request.admin.exclusionGlampingRequestDto;
import com.green.glampick.dto.request.admin.exclusionSignUpRequestDto;
import com.green.glampick.dto.response.admin.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminService {

    //  관리자 페이지 - 사장님 가입정보 확인하기  //
    ResponseEntity<? super GetOwnerSignUpResponseDto> getOwnerSignUpInfo (Long ownerId);

    //  관리자 페이지 - 대기중인 사장님 회원가입 리스트 불러오기  //
    ResponseEntity<? super GetAccessOwnerSignUpListResponseDto> accessSignUpList ();

    //  관리자 페이지 - 사장님 회원가입 승인 처리하기  //
    ResponseEntity<? super PatchAccessOwnerSignUpResponseDto> accessSignUp(Long ownerId);

    //  관리자 페이지 - 사장님 회원가입 반려 처리하기  //
    ResponseEntity<? super DeleteExclusionOwnerSignUpResponseDto> exclutionSignUp(exclusionSignUpRequestDto dto);

    //  관리자 페이지 - 메인 화면 배너 추가하기  //
    ResponseEntity<? super PostBannerResponseDto> postBanner(List<MultipartFile> file);

    //  관리자 페이지 - 메인 화면 배너 삭제하기  //
    ResponseEntity<? super DeleteBannerResponseDto> deleteBanner(Long bannerId);

    //  관리자 페이지 - 메인 화면 배너 불러오기  //
    ResponseEntity<? super GetBannerResponseDto> getBanner();

    //  관리자 페이지 - 승인 대기중인 글램핑장 리스트 불러오기  //
    ResponseEntity<? super GetAccessGlampingListResponseDto> getAccessGlampingList();

    //  관리자 페이지 - 사장님 글램핑 등록 정보 불러오기  //
    ResponseEntity<? super GetAccessGlampingInfoResponseDto> getAccessGlamping(Long glampId);

    //  관리자 페이지 - 글램핑 등록 승인 처리하기  //
    ResponseEntity<? super PatchGlampingAccessResponseDto> accessGlamping(Long glampId);

    //  관리자 페이지 - 글램핑 등록 반려 처리하기  //
    ResponseEntity<? super GlampingExclutionResponseDto> exclutionGlamping(exclusionGlampingRequestDto dto);

    //  관리자 페이지 - 회원탈퇴 대기 사장님 리스트 불러오기  //
    ResponseEntity<? super getDeleteOwnerListResponseDto> deleteOwnerList();

    //  관리자 페이지 - 사장님 회원탈퇴 승인 처리하기 - 완료  //
    ResponseEntity<? super PatchDeleteOwnerResponseDto> deleteOwner(Long ownerId);

}

package com.green.glampick.controller.controllerInterface;

import com.green.glampick.dto.request.ReviewPatchRequestDto;
import com.green.glampick.dto.request.owner.*;
import com.green.glampick.dto.request.user.GetReviewRequestDto;
import com.green.glampick.dto.response.owner.OwnerSuccessResponseDto;
import com.green.glampick.dto.response.owner.get.*;
import com.green.glampick.dto.response.owner.patch.PatchOwnerPeakResponseDto;
import com.green.glampick.dto.response.owner.patch.PatchOwnerReviewInfoResponseDto;
import com.green.glampick.dto.response.owner.post.PostRoomInfoResponseDto;
import com.green.glampick.dto.response.owner.put.PatchOwnerInfoResponseDto;
import com.green.glampick.dto.response.user.GetReviewResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface OwnerController {

    ResponseEntity<? super OwnerSuccessResponseDto> createGlamping(GlampingPostRequestDto req, MultipartFile glampImg);

    ResponseEntity<? super OwnerSuccessResponseDto> updateGlamping(GlampingPutRequestDto req);

    ResponseEntity<? super OwnerSuccessResponseDto> updateGlampingImage(MultipartFile image, long glampId);

    ResponseEntity<? super OwnerSuccessResponseDto> createRoom(RoomPostRequestDto req, List<MultipartFile> roomImg);

    ResponseEntity<? super OwnerSuccessResponseDto> updateRoom(List<MultipartFile> addImg, RoomPutRequestDto req);

    ResponseEntity<? super OwnerSuccessResponseDto> deleteRoom(Long roomId);

    ResponseEntity<? super GetGlampingInfoResponseDto> getGlamping();

    ResponseEntity<? super GetRoomListResponseDto> getRoomList(Long glampId);

    ResponseEntity<? super GetRoomInfoResponseDto> getRoomOne(Long glampId, Long roomId);

    ResponseEntity<? super OwnerSuccessResponseDto> checkOwnerPassword(CheckPasswordRequestDto dto);

    ResponseEntity<? super OwnerInfoResponseDto> getOwnerInfo();

    ResponseEntity<? super PatchOwnerInfoResponseDto> updateOwnerInfo(PatchOwnerInfoRequestDto dto);

    ResponseEntity<? super OwnerSuccessResponseDto> withdrawOwner();


// 강국 =================================================================================================================

   ResponseEntity<? super PatchOwnerReviewInfoResponseDto> patchReview(ReviewPatchRequestDto p);

   ResponseEntity<? super GetReviewResponseDto> getReview( GetReviewRequestDto dto);

   ResponseEntity<? super PatchOwnerPeakResponseDto> patchPeak( Long glampId, PatchOwnerPeakRequestDto p);

   ResponseEntity<? super OwnerSuccessResponseDto> delGlampingPeakPeriod(Long glampId);

   ResponseEntity<? super GetGlampingPeakPeriodResponseDto> getGlampingPeakPeriod(Long glampId);

   ResponseEntity<? super GetOwnerBookListResponseDto> getOwnerReservation(ReservationGetRequestDto p);

   ResponseEntity<? super GetOwnerBookListResponseDto> getOwnerReservationCount(ReservationGetRequestDto p);

// 진현 =================================================================================================================

    ResponseEntity<? super GetOwnerPopularRoomResponseDto> getPopRoom( ReviewGetRoomRequestDto dto);
        
    ResponseEntity<? super GetOwnerStarResponseDto> getStarRoom( ReviewGetStarRequestDto dto);
    
    ResponseEntity<? super GetGlampingCancelResponseDto> getGlampingCancelRoom( ReviewGetCancelRequestDto dto);
    
    ResponseEntity<? super GetOwnerRevenueResponseDto> getPopRoom( ReviewGetRevenueRequestDto dto);
        
}

package com.green.glampick.service;

import com.green.glampick.dto.request.glamping.*;
import com.green.glampick.dto.response.glamping.*;
import com.green.glampick.dto.response.glamping.favorite.GetFavoriteGlampingResponseDto;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;

public interface GlampingService {
//    ResponseEntity<? super GetSearchMapListResponseDto> searchMapList(GetSearchMapRequestDto dto);
    ResponseEntity<? super GetSearchMapListResponseDto> searchMapList(String region);
    ResponseEntity<? super GetSearchGlampingListResponseDto> searchGlamping(GlampingSearchRequestDto searchReq);

    ResponseEntity<? super GetFavoriteGlampingResponseDto> favoriteGlamping(GetFavoriteRequestDto p);
    ResponseEntity<? super GetGlampingInformationResponseDto> infoGlampingDetail(GetInfoRequestDto p);

    ResponseEntity<? super GetMoreReviewImgageResponseDto> moreReviewImage(GetMoreReviewImageRequestDto p);

    ResponseEntity<? super GetMoreRoomImageResponseDto> moreRoomImage(@ParameterObject @ModelAttribute GetMoreRoomImageRequestDto p);

    ResponseEntity<? super GetGlampingReviewInfoResponseDto> infoReviewList(ReviewInfoRequestDto p);

//    int getIsFavData(GetInfoRequestDto dto);
    //    public ResponseEntity<? super GetMoreRoomItemResponseDto> moreDetailsRoom(GetInfoRequestDto p);
}

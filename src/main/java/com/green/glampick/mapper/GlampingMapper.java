package com.green.glampick.mapper;

import com.green.glampick.dto.object.ReviewListItem;
import com.green.glampick.dto.object.glamping.*;
import com.green.glampick.dto.request.glamping.*;
import com.green.glampick.dto.response.glamping.GetGlampingInformationResponseDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GlampingMapper {
    // 민지
    List<Long> searchGlamping1(GlampingSearchRequestDto p);
    List<GlampingListItem> searchGlamping2(GlampingSearchRequestDto p);
    int searchCount(GlampingSearchRequestDto p);

    // 강국
    // 관심등록
    int deleteFavorite(GetFavoriteRequestDto p);
    int insertFavorite(GetFavoriteRequestDto p);
    Long getIsFavData(GetInfoRequestDto p);
    //글램핑 상세정보
    GetGlampingInformationResponseDto selGlampingInfo(GetInfoRequestDto p); //글램핑정보
    // 룸 정보
    List<GlampingRoomListItem> selRoomInfo(GetInfoRequestDto p); //객실정보

    List<GlampingDateItem> selDate(GetInfoRequestDto p);
    List<String> selRoomPics(long roomId);//객실이미지
    List<GlampingDetailReviewItem> selReviewInfoInGlamping(long glampId);//리뷰정보
    int selCount(long glampId);//리뷰유저수
    List<String> selRoomService(long roomId); // 룸서비스
    List<GlampingRoomNameAndImage> selRoomNameAndImages(GetMoreRoomImageRequestDto p);
    //리뷰
    List<String> thumbnailReviewImage(ReviewInfoRequestDto p); // 모든 리뷰이미지
    List<ReviewListItem> selReviewInfo(ReviewInfoRequestDto p); // 리뷰페이지 리뷰정보
    List<String> selReviewImage(long reviewId); // 리뷰 이미지
    List<GlampingRoomNameAndImage> selRoomNames(long glampId); // 글랭핑 보유 객실이름들

    List<String> allReviewImages(GetMoreReviewImageRequestDto p);

}

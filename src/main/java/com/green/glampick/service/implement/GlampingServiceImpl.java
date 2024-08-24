package com.green.glampick.service.implement;

import com.green.glampick.dto.object.Repository;
import com.green.glampick.dto.object.ReviewListItem;
import com.green.glampick.dto.object.glamping.*;
import com.green.glampick.dto.request.glamping.*;
import com.green.glampick.entity.RoomEntity;
import com.green.glampick.module.DateModule;
import com.green.glampick.dto.response.glamping.*;
import com.green.glampick.dto.response.glamping.favorite.GetFavoriteGlampingResponseDto;
import com.green.glampick.entity.GlampingEntity;
import com.green.glampick.exception.CustomException;
import com.green.glampick.exception.errorCode.CommonErrorCode;
import com.green.glampick.exception.errorCode.GlampingErrorCode;
import com.green.glampick.mapper.GlampingMapper;
import com.green.glampick.module.GlampingModule;
import com.green.glampick.repository.*;
import com.green.glampick.repository.resultset.GetBookDateResultSet;
import com.green.glampick.repository.resultset.GetMapListResultSet;
import com.green.glampick.repository.resultset.GetPeakDateResultSet;
import com.green.glampick.security.AuthenticationFacade;
import com.green.glampick.service.GlampingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;
import java.util.*;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;
import static com.green.glampick.module.DateModule.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GlampingServiceImpl implements GlampingService {
    private final GlampingMapper mapper;
    private final GlampingRepository glampingRepository;
    private final FavoriteGlampingRepository favoriteGlampingRepository;
    private final AuthenticationFacade facade;
    private final GlampPeakRepository glampPeakRepository;
    private final RoomPriceRepository roomPriceRepository;
    private final RoomRepository roomRepository;
    private final ReservationBeforeRepository reservationBeforeRepository;

    private Repository repository() {
        return Repository.builder().glampingRepository(glampingRepository)
                .glampPeakRepository(glampPeakRepository)
                .roomRepository(roomRepository)
                .roomPriceRepository(roomPriceRepository).build();
    }

    // 민지 =================================================================================================================
    @Override
    @Transactional
//    public ResponseEntity<? super GetSearchMapListResponseDto> searchMapList(GetSearchMapRequestDto dto) {
//        List<GetMapListResultSet> resultSets = new ArrayList<>();
//        resultSets = dto.getRegion().equals("all") ? glampingRepository.getMapList() : glampingRepository.getMapList(dto.getRegion());
//        List<MapListItem> result = new ArrayList<>();
//
//        List<RoomEntity> roomEntities = new ArrayList<>();
//
//        for (GetMapListResultSet resultSet : resultSets) {
//            List<RoomEntity> rooms =  roomRepository.findByGlampId(resultSet.getGlampId());
//            if(rooms == null || rooms.isEmpty()) continue;  // 등록된 룸정보가 없으면 x
//            for (RoomEntity room : rooms) {
//                roomEntities.add(room);
//                List<GetBookDateResultSet> roomDateList = reservationBeforeRepository.getBookDateByRoom(room);
//                for (GetBookDateResultSet dateItem : roomDateList) {
//                    HashMap<String, LocalDate> dateHashMap = DateModule.parseDate(dto.getIn(), dto.getOut(), dateItem.getCheckInDate().toString(), dateItem.getCheckOutDate().toString());
//                    if (DateModule.checkOverlap(dateHashMap)) { // 겹치면
//                        roomEntities.remove(room);
//                        break;
//                    }
//                }
//            }
//            MapListItem item = new MapListItem(resultSet.getGlampName(), resultSet.getGlampPic(), resultSet.getStarPoint()
//            , resultSet.getReviewCount(), resultSet.getLat(), resultSet.getLng());
//            item.setGlampId(resultSet.getGlampId());
//            result.add(item);
//        }
//        // 가격 넣기
//        result = GlampingModule.setRoomPrice(result, DateModule.isWeekend(), repository(), roomEntities);
//        System.out.println(result.size() + "+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//        return GetSearchMapListResponseDto.success(result);
//    }
    public ResponseEntity<? super GetSearchMapListResponseDto> searchMapList(String region) {
        List<GetMapListResultSet> resultSets = new ArrayList<>();
        resultSets = region.equals("all") ? glampingRepository.getMapList() : glampingRepository.getMapList(region);
        List<MapListItem> result = new ArrayList<>();
        for (GetMapListResultSet resultSet : resultSets) {
            List<RoomEntity> room =  roomRepository.findByGlampId(resultSet.getGlampId());
            if(room == null || room.isEmpty()) continue;  // 등록된 룸정보가 없으면 x
            MapListItem item = new MapListItem(resultSet.getGlampName(), resultSet.getGlampPic(), resultSet.getStarPoint()
                    , resultSet.getReviewCount(), resultSet.getLocation());
            item.setGlampId(resultSet.getGlampId());
            result.add(item);
        }
        result = GlampingModule.setRoomPrice(result, DateModule.isWeekend(), repository());
        return GetSearchMapListResponseDto.success(result);
    }

    @Override
    @Transactional
    public ResponseEntity<? super GetSearchGlampingListResponseDto> searchGlamping(GlampingSearchRequestDto req) {

        // 날짜가 올바르게 들어가있나?
        if (DateModule.checkDate(req.getInDate(), req.getOutDate())) {
            throw new CustomException(GlampingErrorCode.WD);
        }

        // 정렬 기준이 있는지
        if (req.getSortType() == null) {
            req.setSortType(1);
        }

        // 필터가 있는지
        if (req.getFilter() != null && !req.getFilter().isEmpty()) {
            req.setFilterSize(req.getFilter().size());
        }

        // searchGlamping1 : 필터, 날짜, 인원수
        List<Long> roomList = mapper.searchGlamping1(req);
        if (roomList == null || roomList.isEmpty()) {
            // 검색결과 없음
            return GetSearchGlampingListResponseDto.isNull();
        }

        req.setRoomList(roomList);
        List<GlampingListItem> result = mapper.searchGlamping2(req);
        if (result == null || result.isEmpty()) {
            // 검색결과 없음
            return GetSearchGlampingListResponseDto.isNull();
        }

        // 객실 가격 넣기
        boolean week = DateModule.isWeekend(req.getInDate());
        result = GlampingModule.setRoomPrice(result, week, repository());

        // 가격순 정렬하기
        if (req.getSortType() == 4) {    // 낮은 가격순
//            Collections.sort(result);
            result = mergeSort(result, true);
        } else if (req.getSortType() == 5) {    // 높은 가격순
            result = mergeSort(result, false);

        }
        int searchCount = mapper.searchCount(req);

        return GetSearchGlampingListResponseDto.success(searchCount, result);
    }


    // 강국 =================================================================================================================
    @Override
    @Transactional
    public ResponseEntity<? super GetFavoriteGlampingResponseDto> favoriteGlamping(GetFavoriteRequestDto p) {

        try {
            p.setUserId(facade.getLoginUserId());
            if (p.getUserId() <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.MNF);
        }

        try {
            //관심글램픽
            int result = mapper.deleteFavorite(p);
            if (result == 0) {
                // 관심등록
                mapper.insertFavorite(p);
                return GetFavoriteGlampingResponseDto.success(result);
            } else {
                return GetFavoriteGlampingResponseDto.success(result);
            }
        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
    }

    @Override
    public ResponseEntity<? super GetGlampingInformationResponseDto> infoGlampingDetail(GetInfoRequestDto p) {

        //글램핑 정보,객실 정보 리스트, 리뷰 리스트, 리뷰 유저수
        GetGlampingInformationResponseDto glampInfoDto = mapper.selGlampingInfo(p);
        //  1 -> 평일 비수기 2 -> 주말 비수기 3 -> 평일 성수기 4 -> 주말 성수기
        Long priceStatus = getPriceData(p);
        p.setPriceType(priceStatus);

        List<GlampingRoomListItem> rooms = mapper.selRoomInfo(p);

        List<GlampingDetailReviewItem> reviews = mapper.selReviewInfoInGlamping(p.getGlampId());

        // 로그인 중 이면 관심 등록 데이터 판단
        if (facade.getLoginUserId() != 0) {
            log.info("p: {}", p);
            log.info("p: {} ", facade.getLoginUserId());
            int isFavData = getIsFavData(p, facade.getLoginUserId());
            glampInfoDto.setIsFav(isFavData);
        }
        //리뷰 한 유저 수
        int userCount = mapper.selCount(p.getGlampId());
        boolean isReservationAvailable = true;


        //중복데이터 방지를 위한 HashSet
        HashSet<String> hashServices = new HashSet<>();
        //  서비스 & 예약가능여부 데이터 세팅
        for (GlampingRoomListItem item : rooms) {
            item.setReservationAvailable(isReservationAvailable);
            item.setRoomServices(mapper.selRoomService(item.getRoomId()));

            p.setRoomId(item.getRoomId());
            //객실 별 
            List<GlampingDateItem> dateItems = mapper.selDate(p);
            for (GlampingDateItem dateItem : dateItems) {
                //검색된 날짜와 예약상태의 객실들 데이터
                HashMap<String, LocalDate> dateHashMap = DateModule.parseDate(p.getInDate(), p.getOutDate(), dateItem.getCheckInDate(), dateItem.getCheckOutDate());
                boolean k = DateModule.checkOverlap(dateHashMap);

                if (k) {
                    item.setReservationAvailable(false);
                    break;
                }

            }

            List<String> roomServices = item.getRoomServices();
            //데이터 중복 방지
            for (String s : roomServices) {
                if (!roomServices.isEmpty()) {
                    hashServices.add(s);
                }
            }

        }

        Collections.sort(rooms);

        // dto 데이터 세팅
        glampInfoDto.setRoomService(hashServices);
        glampInfoDto.setCountReviewUsers(userCount);
        glampInfoDto.setReviewItems(reviews);
        glampInfoDto.setRoomItems(rooms);

        return new ResponseEntity<>(glampInfoDto, HttpStatus.OK);
    }

    /*
    @Override
    public ResponseEntity<? super GetMoreRoomItemResponseDto> moreDetailsRoom(GetInfoRequestDto p) {
        // 객실 정보 리스트
        List<GlampingRoomListItem> rooms = mapper.selRoomInfo(p);
        boolean isReservationAvailable = true;

        // 추가 객실 정보 리스트
        rooms.subList(0, GlobalConst.PAGING_SIZE).clear();
        HashSet<String> hashServices = new HashSet<>();

        //객실 서비스 세팅
        for (GlampingRoomListItem item : rooms) {
            item.setReservationAvailable(isReservationAvailable);
            item.setRoomServices(mapper.selRoomService(item.getRoomId()));

            p.setRoomId(item.getRoomId());

            List<GlampingDateItem> dateItems = mapper.selDate(p);

            for (GlampingDateItem dateItem : dateItems) {
                HashMap<String, LocalDate> dateHashMap = parseDate(p.getInDate(), p.getOutDate(), dateItem.getCheckInDate(), dateItem.getCheckOutDate());
                boolean k = checkOverlap(dateHashMap) ;

                if (k) {
                    item.setReservationAvailable(false);
                    break;
                }
            }
        }

        GetMoreRoomItemResponseDto glampInfoDto = GetMoreRoomItemResponseDto.builder().roomItems(rooms).build();

        return new ResponseEntity<>(glampInfoDto,HttpStatus.OK);
    }
     */
    @Override
    public ResponseEntity<? super GetGlampingReviewInfoResponseDto> infoReviewList(ReviewInfoRequestDto p) {

        // Data Get
        List<ReviewListItem> reviews = mapper.selReviewInfo(p);
        List<GlampingRoomNameAndImage> roomNameDto = mapper.selRoomNames(p.getGlampId());
        List<String> reviewImage = mapper.thumbnailReviewImage(p);

        //리뷰사진 가져오기
        for (int i = 0; i < reviews.size(); i++) {
            List<String> inputImageList = mapper.selReviewImage(reviews.get(i).getReviewId());
            reviews.get(i).setReviewImages(inputImageList);
        }
        List<String> roomNameList = new ArrayList<>();
        for (int i = 0; i < roomNameDto.size(); i++) {
            String roomName = roomNameDto.get(i).getRoomName();
            roomNameList.add(roomName);
        }

        //input ResponseDto
        GetGlampingReviewInfoResponseDto dto = GetGlampingReviewInfoResponseDto.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .reviewListItems(reviews)
                .roomNames(roomNameList)
                .allReviewImage(reviewImage).build();

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<? super GetMoreReviewImgageResponseDto> moreReviewImage(GetMoreReviewImageRequestDto p) {
        //페이징 리뷰 이미지
        List<String> images = mapper.allReviewImages(p);

        GetMoreReviewImgageResponseDto dto = GetMoreReviewImgageResponseDto.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .moreReviewImage(images).build();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    public ResponseEntity<? super GetMoreRoomImageResponseDto> moreRoomImage(@ParameterObject @ModelAttribute GetMoreRoomImageRequestDto p) {

        //dto 에 넣을 HashMap
        HashMap<String, List<String>> hashMapImages = new HashMap<>();

        List<GlampingRoomNameAndImage> roomNameAndImages = mapper.selRoomNameAndImages(p);

        for (GlampingRoomNameAndImage item : roomNameAndImages) {
            String roomName = item.getRoomName();
            String roomImage = item.getRoomImages();

            if (!hashMapImages.containsKey(roomName)) {
                hashMapImages.put(roomName, new ArrayList<>());
            }
            hashMapImages.get(roomName).add(roomImage);
        }

        for (String roomName : hashMapImages.keySet()) {
            System.out.println("Room Name: " + roomName);
            List<String> imageNames = hashMapImages.get(roomName);
            for (String imageName : imageNames) {
                System.out.println("  Image: " + imageName);
            }
        }

        GetMoreRoomImageResponseDto dto = GetMoreRoomImageResponseDto.builder()
                .code(SUCCESS_CODE)
                .message(SUCCESS_MESSAGE)
                .moreRoomImages(hashMapImages).build();

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    // =================================================================================================================
    private Long getPriceData(GetInfoRequestDto p) {
        Long typeNumber = 0L;

        GetPeakDateResultSet peak = glampPeakRepository.getPeak(p.getGlampId());
        boolean getPeakStatus = false;

        if (peak != null) {
            getPeakStatus = isPeak(p.getInDate(), peak);// true -> 성수기 false -> 비수기

        }
        boolean getWeekendStatus = isWeekend(p.getInDate());// true -> 주말 false -> 평일

        /*  1 -> 평일 비수기
            2 -> 주말 비수기
            3 -> 평일 성수기
            4 -> 주말 성수기
         */
        if (getPeakStatus && getWeekendStatus) { //주말 성수기
            typeNumber = 4L;
        } else if (getPeakStatus) { //평일 성수기
            typeNumber = 3L;
        } else if (getWeekendStatus) { //주말 비수기
            typeNumber = 2L;
        } else { //평일 비수기
            typeNumber = 1L;
        }

        return typeNumber;
    }

    private int getIsFavData(GetInfoRequestDto dto, long loginUserId) {
        if (loginUserId != 0) {
            dto.setUserId(loginUserId);
            if (mapper.getIsFavData(dto) == null) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }


    @Scheduled(cron = "0 1 0 * * *")
    public void updateGlamping() {

        List<GlampingEntity> glampingEntities = glampingRepository.findAll();

        for (GlampingEntity glampingEntity : glampingEntities) {

            Double starAvg = glampingRepository.findStarPointAvgByGlampId(glampingEntity.getGlampId());
            Long favoriteCount = favoriteGlampingRepository.countByGlamping(glampingEntity);

            if (starAvg == null) {
                starAvg = 0.0;
            }
            if (favoriteCount == null) {
                favoriteCount = 0L;
            }

            Double recommendScore = (starAvg * 0.7) + (favoriteCount + 0.3);

            glampingEntity.setRecommendScore(recommendScore);
            glampingRepository.save(glampingEntity);

        }

    }


    private List<GlampingListItem> mergeSort(List<GlampingListItem> glampings, boolean ascending) {
        if (glampings.size() <= 1) {
            return glampings;
        }
        int mid = glampings.size() / 2;
        List<GlampingListItem> leftHalf = mergeSort(glampings.subList(0, mid), ascending);
        List<GlampingListItem> rightHalf = mergeSort(glampings.subList(mid, glampings.size()), ascending);

        return merge(leftHalf, rightHalf, ascending);
    }

    private List<GlampingListItem> merge(List<GlampingListItem> left, List<GlampingListItem> right, boolean ascending) {
        List<GlampingListItem> merged = new ArrayList<>();
        int leftIndex = 0, rightIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if ((ascending && left.get(leftIndex).getPrice() <= right.get(rightIndex).getPrice()) ||
                    (!ascending && left.get(leftIndex).getPrice() >= right.get(rightIndex).getPrice())) {
                merged.add(left.get(leftIndex));
                leftIndex++;
            } else {
                merged.add(right.get(rightIndex));
                rightIndex++;
            }
        }

        while (leftIndex < left.size()) {
            merged.add(left.get(leftIndex));
            leftIndex++;
        }

        while (rightIndex < right.size()) {
            merged.add(right.get(rightIndex));
            rightIndex++;
        }

        return merged;
    }
}




package com.green.glampick.service.implement;

import com.green.glampick.common.CustomFileUtils;
import com.green.glampick.dto.object.Repository;
import com.green.glampick.dto.object.UserReviewListItem;
import com.green.glampick.dto.object.owner.*;
import com.green.glampick.dto.object.room.RoomImageItem;
import com.green.glampick.dto.request.owner.*;
import com.green.glampick.dto.request.ReviewPatchRequestDto;
import com.green.glampick.dto.response.owner.patch.PatchOwnerPeakResponseDto;
import com.green.glampick.dto.response.owner.patch.PatchOwnerReviewInfoResponseDto;
import com.green.glampick.dto.object.owner.GetCancelDto;
import com.green.glampick.dto.object.owner.GetPopularRoom;
import com.green.glampick.dto.object.owner.GetRevenue;
import com.green.glampick.dto.object.owner.GetStarHeart;
import com.green.glampick.dto.request.owner.ReviewGetCancelRequestDto;
import com.green.glampick.dto.request.owner.ReviewGetRevenueRequestDto;
import com.green.glampick.dto.request.owner.ReviewGetRoomRequestDto;
import com.green.glampick.dto.request.owner.ReviewGetStarRequestDto;
import com.green.glampick.dto.response.owner.get.GetGlampingCancelResponseDto;
import com.green.glampick.dto.response.owner.get.GetOwnerPopularRoomResponseDto;
import com.green.glampick.dto.response.owner.get.GetOwnerRevenueResponseDto;
import com.green.glampick.dto.response.owner.get.GetOwnerStarResponseDto;
import com.green.glampick.module.GlampingModule;
import com.green.glampick.module.RoomModule;
import com.green.glampick.dto.request.user.GetReviewRequestDto;
import com.green.glampick.dto.response.owner.*;
import com.green.glampick.dto.response.owner.get.*;
import com.green.glampick.dto.response.owner.post.PostRoomInfoResponseDto;
import com.green.glampick.dto.response.owner.put.PatchOwnerInfoResponseDto;
import com.green.glampick.dto.response.user.GetReviewResponseDto;
import com.green.glampick.entity.*;
import com.green.glampick.exception.CustomException;
import com.green.glampick.exception.errorCode.CommonErrorCode;
import com.green.glampick.exception.errorCode.OwnerErrorCode;
import com.green.glampick.exception.errorCode.UserErrorCode;

import com.green.glampick.repository.*;
import com.green.glampick.repository.resultset.*;
import com.green.glampick.security.AuthenticationFacade;
import com.green.glampick.service.OwnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.green.glampick.exception.errorCode.GlampingErrorCode.NEGP;
import static com.green.glampick.module.DateModule.getPeriod;
import static com.green.glampick.module.DateModule.parseToLocalDate;


@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {
    private final AuthenticationFacade authenticationFacade;
    private final CustomFileUtils customFileUtils;
    private final PasswordEncoder passwordEncoder;
    private final GlampingWaitRepository glampingWaitRepository;
    private final GlampingRepository glampingRepository;
    private final OwnerRepository ownerRepository;
    private final RoomRepository roomRepository;
    private final RoomImageRepository roomImageRepository;
    private final ServiceRepository serviceRepository;
    private final RoomServiceRepository roomServiceRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ReservationBeforeRepository reservationBeforeRepository;
    private final ReservationCancelRepository reservationCancelRepository;
    private final ReservationCompleteRepository reservationCompleteRepository;
    private final RoomPriceRepository roomPriceRepository;
    private final GlampPeakRepository glampPeakRepository;

    private Repository repository() {
        return new Repository(authenticationFacade, customFileUtils, passwordEncoder,
                glampingWaitRepository, glampingRepository, ownerRepository, roomRepository,
                roomImageRepository, serviceRepository, roomServiceRepository, reviewRepository,
                reviewImageRepository, reservationBeforeRepository, reservationCancelRepository,
                reservationCompleteRepository, roomPriceRepository, glampPeakRepository);
    }

// 민지 =================================================================================================================

    // 글램핑 등록
    @Transactional
    public ResponseEntity<? super OwnerSuccessResponseDto> postGlampingInfo(GlampingPostRequestDto req
            , MultipartFile glampImg) {

        // 오너 PK 불러오기
        long ownerId = GlampingModule.ownerId(authenticationFacade);
        // 권한 체크
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);
        GlampingModule.roleCheck(owner.getRole());
        GlampingWaitEntity entity = null;
        String location = null;
        boolean state = false;   // 글램핑 재등록 = true
        boolean pic = glampImg != null && !glampImg.isEmpty(); // 사진 있으면 true
        try {  // 오류가 난다 > 존재하지 않는다 > 처음 등록한다
            // 오류가 안난다 > 존재한다 > 반려 후 수정
            entity = glampingWaitRepository.findByOwner(owner);
            if (entity == null) throw new RuntimeException();
            location = entity.getGlampLocation();
            state = true;
        } catch (Exception e) {
            entity = new GlampingWaitEntity();
            entity.setOwner(owner);
            // 사장님이 글램핑을 이미 가지고 있는가?
            GlampingModule.hasGlamping(repository(), owner);
            // 이미지가 들어있는가?
            GlampingModule.imgExist(glampImg);
        }

        // 글램핑 위치가 중복되는가?
        if(!req.getGlampLocation().equals(location)) {
            GlampingModule.existingLocation(repository(), req.getGlampLocation());
        }
        // 글램핑 아이디 받아오기
        entity.setGlampName(req.getGlampName());
        if (req.getGlampCall() != null && !req.getGlampCall().isEmpty()) {
            entity.setGlampCall(GlampingModule.glampingCall(req.getGlampCall()));
        }
        if(pic) entity.setGlampImage("img");
        entity.setExclusionStatus(0);
        entity.setGlampLocation(req.getGlampLocation());
        entity.setRegion(req.getRegion());
        entity.setExtraCharge(0);
        if (req.getExtraCharge() != null && req.getExtraCharge() > 0) {
            entity.setExtraCharge(req.getExtraCharge());
        }
        entity.setGlampIntro(req.getIntro());
        entity.setInfoBasic(req.getBasic());
        entity.setInfoNotice(req.getNotice());
        entity.setTraffic(req.getTraffic());
        glampingWaitRepository.save(entity);
        long glampId = entity.getGlampId();


        // 좌표, 이미지 저장하기
//        String point = String.format("POINT(%s %s)", req.getLat(), req.getLng());
        if(pic && state) { // 재등록인데 이미지를 수정한다.
            String folderPath = String.format("glampingWait/%d/glamp", glampId);
            customFileUtils.deleteFolder(folderPath);
        }
        if(pic) {
            String fileName = GlampingModule.imageUpload(customFileUtils, glampImg, glampId, "glampingWait");
            glampingWaitRepository.updateGlampImageByGlampId(fileName, glampId);
        }

        return OwnerSuccessResponseDto.postInformation();
    }

    // 글램핑 수정
    @Transactional
    public ResponseEntity<? super OwnerSuccessResponseDto> updateGlampingInfo(GlampingPutRequestDto p) {

        long ownerId = GlampingModule.ownerId(authenticationFacade);

        // 권한 체크
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);
        GlampingModule.roleCheck(owner.getRole());

        // 로그인 유저와 글램핑 PK가 매치되는가?
        GlampingModule.isGlampIdOk(glampingRepository, owner, p.getGlampId());

        GlampingPostRequestDto dto = p.getRequestDto();

        // 전화번호 형식 맞추기
        if (dto.getGlampCall() != null && !dto.getGlampCall().isEmpty()) {
            dto.setGlampCall(GlampingModule.glampingCall(dto.getGlampCall()));
        }

        // 위치정보 중복되는지 확인하기
        if (dto.getGlampLocation() != null && !dto.getGlampLocation().isEmpty()) {
            GlampingModule.locationUpdate(dto.getGlampLocation(), repository(), p.getGlampId());
        }

        // 입력되지 않은 데이터에는 기존 값 넣어주기
        GlampingEntity entity = glampingRepository.findByGlampId(p.getGlampId());
        dto = GlampingModule.dtoNull(dto, entity);

        glampingRepository.updateGlampingInformation(dto.getGlampName(), dto.getGlampCall()
                , dto.getGlampLocation(), dto.getRegion(), dto.getExtraCharge()
                , dto.getIntro(), dto.getBasic(), dto.getNotice(), dto.getTraffic(), p.getGlampId());
//        String point = String.format("POINT(%s %s)", dto.getLat(), dto.getLng());
//        glampingRepository.updateGlampLocation(p.getGlampId(), point);

        return OwnerSuccessResponseDto.updateInformation();
    }

    // 글램핑 사진 수정
    @Transactional
    public ResponseEntity<? super OwnerSuccessResponseDto> changeGlampingImage(MultipartFile image, long glampId) {

        long ownerId = GlampingModule.ownerId(authenticationFacade);

        // 권한 체크
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);
        GlampingModule.roleCheck(owner.getRole());

        // 로그인 유저와 글램핑 PK가 매치되는가?
        GlampingModule.isGlampIdOk(glampingRepository, owner, glampId);

        if (image == null || image.isEmpty()) {
            throw new CustomException(OwnerErrorCode.NF);
        }

        String folderPath = String.format("glampingWait/%d/glamp", glampId);
        customFileUtils.deleteFolder(folderPath);
        String fileName = GlampingModule.imageUpload(customFileUtils, image, glampId, "glamping");
        glampingRepository.updateGlampImageByGlampId(fileName, glampId);

        return OwnerSuccessResponseDto.updateInformation();
    }

    // 객실 등록
    @Transactional
    public ResponseEntity<? super OwnerSuccessResponseDto> postRoomInfo(RoomPostRequestDto req
            , List<MultipartFile> image) {

        // 오너 PK 불러오기
        long ownerId = GlampingModule.ownerId(authenticationFacade);
        // 권한 체크
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);
        GlampingModule.roleCheck(owner.getRole());
        // 로그인 유저와 글램핑 PK가 매치되는가?
        GlampingModule.isGlampIdOk(glampingRepository, owner, req.getGlampId());

        // 사진이 들어있나?
        RoomModule.imgExist(image);

        // 인원 정보가 올바른가?
        RoomModule.personnel(req.getPeopleNum(), req.getPeopleMax());

        // room 테이블 insert
        GlampingEntity glamping = glampingRepository.getReferenceById(req.getGlampId());
        RoomEntity room = new RoomEntity(null, glamping, req.getRoomName()
                , req.getPeopleNum(), req.getPeopleMax(), req.getInTime(), req.getOutTime());
        roomRepository.save(room);

        // 성수기 설정이 되어있는지 확인
        GetPeakDateResultSet peakData = RoomModule.checkPeak(repository(), glamping);

        // room price 테이블 insert
        RoomPriceEntity price = new RoomPriceEntity();
        price.setRoom(roomRepository.getReferenceById(room.getRoomId()));
        price.setWeekdayPrice(req.getWeekdayPrice());
        price.setWeekendPrice(req.getWeekendPrice());
        if (peakData != null) {
            double ratio = peakData.getPercent() * 0.01;
            int weekday = (int) (req.getWeekdayPrice() + req.getWeekdayPrice() * ratio);
            int weekend = (int) (req.getWeekendPrice() + req.getWeekendPrice() * ratio);
            price.setPeakWeekdayPrice(weekday);
            price.setPeakWeekendPrice(weekend);
        }
        roomPriceRepository.save(price);

        // 이미지 저장
        List<String> roomImgName = RoomModule.imgInsert(image, req.getGlampId(), room.getRoomId(), customFileUtils);
        List<RoomImageEntity> saveImage = RoomModule.saveImage(roomImgName, roomRepository.findByRoomId(room.getRoomId()));
        roomImageRepository.saveAll(saveImage);

        // 서비스 저장
        if (req.getService() != null) {
            List<RoomServiceEntity> service = RoomModule.saveService(req.getService(), roomRepository.findByRoomId(room.getRoomId()), serviceRepository);
            roomServiceRepository.saveAll(service);
        }

//        return OwnerSuccessResponseDto.postInformation();
        return OwnerSuccessResponseDto.postInformation();
    }

    // 객실 수정
    @Transactional
    public ResponseEntity<? super OwnerSuccessResponseDto> updateRoomInfo(List<MultipartFile> addImg, RoomPutRequestDto p) {

        RoomPostRequestDto dto = p.getRequestDto();

        long ownerId = GlampingModule.ownerId(authenticationFacade);
        // 권한 체크
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);
        GlampingModule.roleCheck(owner.getRole());

        // 로그인 유저와 룸 Id가 매치되는가?
        RoomModule.isRoomIdOk(repository(), owner, p.getRoomId());

        // 입력된 인원 정보가 올바른지 확인
        RoomModule.personnelUpdate(dto.getPeopleNum(), dto.getPeopleMax());

        // 시간이 올바른지 확인
        if (dto.getInTime() != null && !dto.getInTime().isEmpty()) {
            RoomModule.isValidTime(dto.getInTime());
        }
        if (dto.getOutTime() != null && !dto.getOutTime().isEmpty()) {
            RoomModule.isValidTime(dto.getOutTime());
        }

        // null 인 경우 기존값 넣어주기
        RoomEntity room = roomRepository.getReferenceById(p.getRoomId());
        dto = RoomModule.dtoNull(dto, room, roomPriceRepository.findByRoom(room));

        // room 업데이트
        GlampingEntity glamping = glampingRepository.getReferenceById(dto.getGlampId());
        RoomEntity roomUpdate = new RoomEntity(p.getRoomId(), glamping
                , dto.getRoomName(), dto.getPeopleNum()
                , dto.getPeopleMax(), dto.getInTime(), dto.getOutTime());
        roomRepository.save(roomUpdate);

        // room price 없데이트
        RoomPriceEntity priceUpdate = roomPriceRepository.findByRoom(roomUpdate);
        priceUpdate.setWeekdayPrice(dto.getWeekdayPrice());
        priceUpdate.setWeekendPrice(dto.getWeekendPrice());
        // 성수기 설정이 되어있는지 확인
        GetPeakDateResultSet peakData = RoomModule.checkPeak(repository(), glamping);
        if (peakData != null) {
            double ratio = peakData.getPercent() * 0.01;
            int weekday = (int) (dto.getWeekdayPrice() + dto.getWeekdayPrice() * ratio);
            int weekend = (int) (dto.getWeekendPrice() + dto.getWeekendPrice() * ratio);
            priceUpdate.setPeakWeekdayPrice(weekday);
            priceUpdate.setPeakWeekendPrice(weekend);
        }
        roomPriceRepository.save(priceUpdate);

        // 서비스 수정
        List<Long> roomService = serviceRepository.findRoomServiceIdByRoom(room);
        List<Long> inputService = dto.getService();
        RoomModule.updateService(roomService, inputService, room, repository());

        // 삭제되는 사진이 있다면 삭제
        if (p.getRemoveImg() != null && !p.getRemoveImg().isEmpty()) {
            for (Long img : p.getRemoveImg()) {
                // 해당 객실의 사진이 맞는지 확인
                RoomImageEntity imageEntity = roomImageRepository.getReferenceById(img);
                RoomModule.checkImgId(room, imageEntity);
                // 삭제
                RoomModule.deleteImageOne(img, repository());
            }
        }

        // 추가되는 사진이 있다면 추가
        if (addImg != null && !addImg.isEmpty() && !addImg.get(0).isEmpty()) {
            List<String> roomImgName = RoomModule.imgInsert(addImg, dto.getGlampId(), room.getRoomId(), customFileUtils);
            List<RoomImageEntity> saveImage = RoomModule.saveImage(roomImgName, roomRepository.findByRoomId(room.getRoomId()));
            roomImageRepository.saveAll(saveImage);
        }

        return OwnerSuccessResponseDto.updateInformation();
    }

    // 객실 삭제
    @Transactional
    public ResponseEntity<? super OwnerSuccessResponseDto> deleteRoom(Long roomId) {
        // PK 불러오기
        long ownerId = GlampingModule.ownerId(authenticationFacade);

        // 권한 체크
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);
        GlampingModule.roleCheck(owner.getRole());
        // 사장님이 해당 객실을 가지고있는지 확인
        RoomModule.isRoomIdOk(repository(), owner, roomId);

        // 삭제
        RoomEntity entity = roomRepository.getReferenceById(roomId);
        roomRepository.delete(entity);

        return OwnerSuccessResponseDto.deleteInformation();
    }

    // 글램핑 정보 불러오기
    @Transactional
    public ResponseEntity<? super GetGlampingInfoResponseDto> getGlamping() {
        /*
            state : owner table 에 glamping status 가 1이면 true 0이면 false
                    true - glamping table 에서 get (정상 등록 완료)
                    false -	glamping wait table 에서 get (최초 등록 or 심사 중)
         */

        // 사장님 PK 불러오기
        long ownerId = GlampingModule.ownerId(authenticationFacade);
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);
        GlampingModule.roleCheck(owner.getRole());
        GetGlampingInfoItem result = null;
        GetGlampingInfoResultSet resultSet = null;
        if (owner.getGlampingStatus() == 0) {
            resultSet = glampingWaitRepository.getGlampingInfo(owner);
            if(resultSet == null) {
                result = new GetGlampingInfoItem(); // 최초 등록 > 불러올 데이터가 없음
                result.setState(false);
            } else { // 불러오기 (반려 후 수정)
                result = new GetGlampingInfoItem(false, resultSet.getGlampId(), resultSet.getName()
                , resultSet.getCall(), resultSet.getImage(), resultSet.getLocation(),
                        resultSet.getRegion(), resultSet.getCharge(), resultSet.getIntro()
                , resultSet.getBasic(), resultSet.getNotice(), resultSet.getTraffic(),
                        resultSet.getExclusionStatus());
            }
            return GetGlampingInfoResponseDto.successWait(result);
        }
        resultSet = glampingRepository.getGlampingInfo(owner);
        return GetGlampingInfoResponseDto.success(true, resultSet);
    }

    // 객실 정보 미리보기
    @Transactional
    public ResponseEntity<? super GetRoomListResponseDto> getRoomList(Long glampId) {
        // PK 불러오기
        long ownerId = GlampingModule.ownerId(authenticationFacade);

        // 권한 체크
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);
        GlampingModule.roleCheck(owner.getRole());
        // 사장님이 해당 글램핑을 가지고있는지 확인
        GlampingModule.isGlampIdOk(glampingRepository, owner, glampId);

        GlampingEntity glamping = glampingRepository.getReferenceById(glampId);
        List<GetRoomListResultSet> resultSet = roomRepository.getRoomList(glamping);
        List<GetRoomItem> result = new ArrayList<>();
        for (GetRoomListResultSet item : resultSet) {
            GetRoomItem room = new GetRoomItem(item.getRoomId(), item.getRoomName(), item.getRoomImageName());
            result.add(room);
        }

        return GetRoomListResponseDto.success(result);
    }

    // 객실 정보 상세보기
    @Transactional
    public ResponseEntity<? super GetRoomInfoResponseDto> getRoomOne(Long glampId, Long roomId) {
        // PK 불러오기
        long ownerId = GlampingModule.ownerId(authenticationFacade);

        // 권한 체크
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);
        GlampingModule.roleCheck(owner.getRole());
        // 사장님이 해당 글램핑을 가지고있는지 확인
        GlampingModule.isGlampIdOk(glampingRepository, owner, glampId);

        // 정보 불러오기
        GetRoomInfoResultSet resultSet = roomRepository.getRoomInfo(roomId);
        RoomEntity room = roomRepository.getReferenceById(roomId);
        List<GetRoomImgInfo> imgResultSet = roomImageRepository.getRoomImg(room);
        List<RoomImageItem> roomImage = new ArrayList<>();
        for (GetRoomImgInfo res : imgResultSet) {
            RoomImageItem item = new RoomImageItem(res.getId(), res.getName());
            roomImage.add(item);
        }

        List<Long> service = serviceRepository.findRoomServiceIdByRoom(room);
        RoomPriceEntity price = roomPriceRepository.findByRoom(room);

        return GetRoomInfoResponseDto.success(resultSet, roomImage, service, price);
    }

    // 비밀번호 확인
    @Transactional
    public ResponseEntity<? super OwnerSuccessResponseDto> checkOwnerPassword(CheckPasswordRequestDto dto) {

        long ownerId = GlampingModule.ownerId(authenticationFacade);
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);

        if (!passwordEncoder.matches(dto.getPassword(), owner.getOwnerPw())) {
            throw new CustomException(UserErrorCode.NMP);
        }
        return OwnerSuccessResponseDto.passwordTrue();
    }

    // 정보 불러오기
    @Transactional
    public ResponseEntity<? super OwnerInfoResponseDto> getOwnerInfo() {
        long ownerId = GlampingModule.ownerId(authenticationFacade);
        OwnerInfoResultSet result = ownerRepository.getOwnerInfo(ownerId);
        return OwnerInfoResponseDto.success(result);
    }

    // 사장님 정보 수정
    @Transactional
    public ResponseEntity<? super PatchOwnerInfoResponseDto> patchOwnerInfo(PatchOwnerInfoRequestDto dto) {

        long ownerId = GlampingModule.ownerId(authenticationFacade);
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);
        if (dto.getOwnerPw() == null || dto.getOwnerPw().isEmpty()) {
            if (dto.getPhoneNum() == null || dto.getPhoneNum().isEmpty()) {
                // 변경된 내용이 없음
                return PatchOwnerInfoResponseDto.noUpdate();
            }
            // 폰번호만 바뀜
            owner.setOwnerPhone(GlampingModule.glampingCall(dto.getPhoneNum()));
            ownerRepository.save(owner);
            return PatchOwnerInfoResponseDto.success();
        }
        if (dto.getPhoneNum() != null && !dto.getPhoneNum().isEmpty()) {
            owner.setOwnerPhone(GlampingModule.glampingCall(dto.getPhoneNum()));
        }
        owner.setOwnerPw(passwordEncoder.encode(dto.getOwnerPw()));
        ownerRepository.save(owner);
        return PatchOwnerInfoResponseDto.success();
    }

    // 사장님 탈퇴 승인 요청
    @Transactional
    public ResponseEntity<? super OwnerSuccessResponseDto> withdrawOwner() {

        long ownerId = GlampingModule.ownerId(authenticationFacade);
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);


        GlampingEntity glamping = glampingRepository.findByOwner(owner);
        if (glamping != null) {  // 글램핑을 등록한 회원인지 확인
            // 예약 된 글램핑이 있는지 확인
            GlampingModule.existReservation(glamping, reservationBeforeRepository);
            // 글램핑
            glamping.setActivateStatus(0);
            glampingRepository.save(glamping);
        }

        // 탈퇴 요청
        owner.setActivateStatus(0);
        ownerRepository.save(owner);


        return OwnerSuccessResponseDto.withdraw();
    }


// 강국 ====================================================================================================================

    @Override
    @Transactional // 사장님 답글달기
    public ResponseEntity<? super PatchOwnerReviewInfoResponseDto> patchReview(ReviewPatchRequestDto p) {

        // 로그인 유저와 글램핑 PK가 매치되는가?
        GlampingModule.isGlampIdOk(glampingRepository, ownerRepository.getReferenceById(p.getOwnerId()), p.getGlampId());

        try {
            ReviewEntity review = reviewRepository.findReviewById(p.getReviewId());
            review.setReviewComment(p.getReviewOwnerContent());
            reviewRepository.save(review);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return PatchOwnerReviewInfoResponseDto.success();
    }

    @Override // 예약 중 리스트 불러오기
    @Transactional
    public List<OwnerBookDetailListItem> getReservationBeforeList(ReservationGetRequestDto p) {

        try {
            log.info("p: {}", p);
            //data setting
            List<OwnerBookItem> reservationBeforeResultSetList;
            Long ownerId = p.getOwnerId();
            int limit = p.getLimit(); // size
            int offset = p.getOffset(); // startIdx
            String date = p.getDate(); // Date

            Pageable pageable = PageRequest.of(offset, limit);
            LocalDate localDate = parseToLocalDate(date);

            List<OwnerBookDetailListItem> bookDetailListItems = new ArrayList<>();

            reservationBeforeResultSetList = reservationBeforeRepository.getReservationBeforeByOwnerId(ownerId, pageable, localDate);
            setBookDetailList(reservationBeforeResultSetList, bookDetailListItems);
            return bookDetailListItems;
        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

    }

    @Override // 예약취소 리스트 불러오기
    @Transactional
    public List<OwnerBookDetailListItem> getReservationCancelList(ReservationGetRequestDto p) {
        try {
            List<OwnerBookItem> reservationCancelResultSetList;
            Long ownerId = p.getOwnerId();
            int limit = p.getLimit();
            int offset = p.getOffset();
            String date = p.getDate();

            Pageable pageable = PageRequest.of(offset, limit);

            LocalDate localDate = parseToLocalDate(date);

            List<OwnerBookDetailListItem> bookDetailListItems = new ArrayList<>();


            reservationCancelResultSetList = reservationCancelRepository.getReservationCancelByOwnerId(ownerId, pageable, localDate);
            setBookDetailList(reservationCancelResultSetList, bookDetailListItems);

            return bookDetailListItems;

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
    }

    @Override // 예약완료 리스트 불러오기
    @Transactional
    public List<OwnerBookDetailListItem> getReservationCompleteList(ReservationGetRequestDto p) {
        try {
            List<OwnerBookItem> reservationCompleteResultSetList;
            Long ownerId = p.getOwnerId();
            int limit = p.getLimit();
            int offset = p.getOffset();
            String date = p.getDate();

            Pageable pageable = PageRequest.of(offset, limit);

            LocalDate localDate = parseToLocalDate(date);

            List<OwnerBookDetailListItem> bookDetailListItems = new ArrayList<>();

            reservationCompleteResultSetList = reservationCompleteRepository.getReservationCompleteByOwnerId(ownerId, pageable, localDate);
            setBookDetailList(reservationCompleteResultSetList, bookDetailListItems);

            return bookDetailListItems;

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

    }

    @Override //리뷰 리스트
    @Transactional
    public ResponseEntity<? super GetReviewResponseDto> getReview(GetReviewRequestDto p) {

        //리뷰 데이터 추출
        try {

            //setting
            Long ownerId = p.getOwnerId();
            int limit = p.getLimit();
            int offset = p.getOffset();
            long typeNum = p.getTypeNum();
            List<GetUserReviewResultSet> reviewInfo = new ArrayList<>();
            Long totalCount = 0L;

            if (typeNum == 0) {
                reviewInfo = reviewRepository.getReviewForOwner(ownerId, limit, offset);
                totalCount = reviewRepository.getTotalOwnersReviewsCount(ownerId);

            } else if (typeNum == 1) {
                reviewInfo = reviewRepository.getReviewForOwnerExcludeComment(ownerId, limit, offset);
                totalCount = reviewRepository.getTotalOwnersNoReviewsCount(ownerId);
            }

            //review PK 세팅
            List<ReviewEntity> reviewEntityList = getReviewEntities(reviewInfo);

            //image Entity 추출
            List<ReviewImageEntity> imageEntities = reviewImageRepository.findByReviewEntityIn(reviewEntityList);

            //dto 생성
            List<UserReviewListItem> reviewListItem = new ArrayList<>();

            //reviewItem List Setting
            setReviewItem(reviewInfo, imageEntities, reviewListItem);

            //reviewTotalCount
            return GetReviewResponseDto.success(reviewListItem, totalCount);

        } catch (CustomException e) {
            e.printStackTrace();
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
    }

    @Override // 날짜별 예약건수
    @Transactional
    public List<OwnerBookCountListItem> getTotalCount(String date, Long ownerId) {
        LocalDate localDate = parseToLocalDate(date);
        int month = localDate.getMonth().getValue();
        try {
            List<GetOwnerBookBeforeCountResponseDto> countBefore = reservationBeforeRepository.getCountFromReservationBefore(month, ownerId);
            List<GetOwnerBookCancelCountResponseDto> countCancel = reservationCancelRepository.getCountFromReservationCancel(month, ownerId);
            List<GetOwnerBookCompleteCountResponseDto> countComplete = reservationCompleteRepository.getCountFromReservationComplete(month, ownerId);

            List<OwnerBookCountListItem> bookCountListItems = new ArrayList<>();

            for (GetOwnerBookBeforeCountResponseDto bookCount : countBefore) {
                OwnerBookCountListItem item = new OwnerBookCountListItem();
                item.setCheckInDate(bookCount.getCheckInDate());
                item.setIngCount(bookCount.getCountBefore());
                bookCountListItems.add(item);
            }

            List<OwnerBookCountListItem> plusBookCountListItems = new LinkedList<>(bookCountListItems);
            for (GetOwnerBookCancelCountResponseDto bookCount : countCancel) {
//                for (OwnerBookCountListItem ownerItem : bookCountListItems) {
                if (bookCountListItems.isEmpty()) {
                    OwnerBookCountListItem item = new OwnerBookCountListItem();
                    item.setCheckInDate(bookCount.getCheckInDate());
                    item.setCancelCount(bookCount.getCountCancel());
                    plusBookCountListItems.add(item);
                } else {
                    for (Integer i = 0; i < bookCountListItems.size(); i++) {

                        String checkInDate = bookCountListItems.get(i).getCheckInDate();
                        String cancelDate = bookCount.getCheckInDate().toString();
                        Long cancelCount = bookCount.getCountCancel();
                        if (checkInDate.equals(cancelDate)) {
                            plusBookCountListItems.get(i).setCancelCount(cancelCount); //
                            break;
                        }
                        if (i == bookCountListItems.size() - 1) {
                            OwnerBookCountListItem newItem = new OwnerBookCountListItem();
                            newItem.setCheckInDate(bookCount.getCheckInDate());
                            newItem.setCancelCount(cancelCount);
                            plusBookCountListItems.add(newItem);
                        }
                    }
                }
            }

            List<OwnerBookCountListItem> plusBookCountListItems2 = new LinkedList<>(plusBookCountListItems);
            for (GetOwnerBookCompleteCountResponseDto bookCount : countComplete) {
//                for (OwnerBookCountListItem ownerItem : bookCountListItems) {
                if (plusBookCountListItems.isEmpty()) {
                    OwnerBookCountListItem item = new OwnerBookCountListItem();
                    item.setCheckInDate(bookCount.getCheckInDate());
                    item.setCompleteCount(bookCount.getCountComplete());
                    plusBookCountListItems2.add(item);
                } else {
                    for (Integer i = 0; i < plusBookCountListItems.size(); i++) {

                        String checkInDate = plusBookCountListItems.get(i).getCheckInDate();
                        String completeDate = bookCount.getCheckInDate().toString();
                        Long completeCount = bookCount.getCountComplete();
                        if (checkInDate.equals(completeDate)) {
                            plusBookCountListItems2.get(i).setCompleteCount(completeCount); //
                            break;
                        }
                        if (i == plusBookCountListItems.size() - 1) {
                            OwnerBookCountListItem newItem = new OwnerBookCountListItem();
                            newItem.setCheckInDate(bookCount.getCheckInDate());
                            newItem.setCompleteCount(completeCount);
                            plusBookCountListItems2.add(newItem);
                        }
                    }
                }
            }
            return plusBookCountListItems2;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
    }

    @Override // 성수기 비수기 등록
    @Transactional
    public ResponseEntity<? super PatchOwnerPeakResponseDto> patchPeak(Long glampId, PatchOwnerPeakRequestDto p) {
        long ownerId = GlampingModule.ownerId(authenticationFacade);
        // 로그인 유저와 글램핑 PK가 매치되는가?
        GlampingModule.isGlampIdOk(glampingRepository, ownerRepository.getReferenceById(ownerId), glampId);
        log.info("service: {}", p);
        //가격정보 찾아오기
        try {
            //리스트 그릇 세팅
            List<OwnerRoomPriceItem> priceResultSet = roomPriceRepository.getRoomPriceList(glampId);
            List<GetRoomPriceItem> priceItems = new ArrayList<>();

            double percent = p.getPeakCost() * 0.01; // percent 값 추출
            setPriceList(priceResultSet, priceItems); // 프록시 데이터를 실물 데이터 세팅(새그릇으로 옮김)
            putPriceData(glampId, p, priceItems, percent); // 데이터 insert or update

            return PatchOwnerPeakResponseDto.success();

        } catch (CustomException e) {
            e.printStackTrace();
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
    }

    @Override // 성수기 기간 불러오기
    @Transactional
    public ResponseEntity<? super GetGlampingPeakPeriodResponseDto> getGlampingPeakPeriod(Long glampId) {
        long ownerId = GlampingModule.ownerId(authenticationFacade);
        // 로그인 유저와 글램핑 PK가 매치되는가?
        GlampingModule.isGlampIdOk(glampingRepository, ownerRepository.getReferenceById(ownerId), glampId);

        try {

            GetPeakDateResultSet peakDateResultSet = glampPeakRepository.getPeak(glampId);
            if (peakDateResultSet == null) {
                throw new CustomException(NEGP);
            }
            return GetGlampingPeakPeriodResponseDto.success(peakDateResultSet.getStartDate().toString(), peakDateResultSet.getEndDate().toString(), peakDateResultSet.getPercent());
        } catch (CustomException e) {
            e.printStackTrace();
            throw new CustomException(e.getErrorCode());
        }
    }

    @Override // 성수기 초기화
    @Transactional
    public ResponseEntity<? super OwnerSuccessResponseDto> delGlampingPeakPeriod(Long glampId) {
        long ownerId = GlampingModule.ownerId(authenticationFacade);
        // 로그인 유저와 글램핑 PK가 매치되는가?
        GlampingModule.isGlampIdOk(glampingRepository, ownerRepository.getReferenceById(ownerId), glampId);

        Optional<GlampPeakEntity> peakEntity = glampPeakRepository.findByGlamp(glampingRepository.getReferenceById(glampId));
//        glampPeakRepository.deleteById(glampId);
        glampPeakRepository.delete(peakEntity.get());
        return OwnerSuccessResponseDto.deleteInformation();

    }

// 진현 ====================================================================================================================

    @Override// 이용 완료된 객실별 예약수
    @Transactional
    public ResponseEntity<? super GetOwnerPopularRoomResponseDto> getPopRoom(ReviewGetRoomRequestDto dto) {
        try {
            dto.setOwnerId(authenticationFacade.getLoginUserId());
            if (dto.getOwnerId() <= 0) {
                throw new CustomException(CommonErrorCode.MNF);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.MNF);
        }

        Long total = 0L;
        List<GetPopularRoom> popRoom = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String formStart = dto.getStartDayId().format(formatter);
        String formEnd = dto.getEndDayId().format(formatter);
        try {

            total = reservationCompleteRepository.findTotal(dto.getOwnerId(), formStart, formEnd);
            popRoom = reservationCompleteRepository.findPopularRoom(dto.getOwnerId(), formStart, formEnd);
            if (dto.getOwnerId() == 0) {
                throw new CustomException(OwnerErrorCode.NMG);
            }
        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return GetOwnerPopularRoomResponseDto.success(total, popRoom);
    }

    @Override// 별점
    @Transactional
    public ResponseEntity<? super GetOwnerStarResponseDto> getStarRoom(ReviewGetStarRequestDto dto) {
        try {
            dto.setOwnerId(authenticationFacade.getLoginUserId());
            if (dto.getOwnerId() <= 0) {
                throw new CustomException(CommonErrorCode.MNF);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.MNF);
        }
        List<GetStarHeart> starHearts = null;
        try {
            starHearts = ownerRepository.findByIdStarPoint(dto.getOwnerId());
            if (dto.getOwnerId() == 0) {
                throw new CustomException(OwnerErrorCode.NMG);
            }
        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();

            throw new CustomException(CommonErrorCode.DBE);
        }

        return GetOwnerStarResponseDto.success(starHearts);
    }


    @Override// 예약 취소율
    @Transactional
    public ResponseEntity<? super GetGlampingCancelResponseDto> getGlampingCancelRoom(ReviewGetCancelRequestDto dto) {
        try {
            dto.setOwnerId(authenticationFacade.getLoginUserId());
            if (dto.getOwnerId() <= 0) {
                throw new CustomException(CommonErrorCode.MNF);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.MNF);
        }

        String formattedResult = null;
        List<GetCancelDto> room = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formStart = dto.getStartDayId().format(formatter);
        String formEnd = dto.getEndDayId().format(formatter);
        try {
            room = ownerRepository.findRoomCount(dto.getOwnerId(), formStart, formEnd);
            Long total = ownerRepository.findTotalCount(dto.getOwnerId(), formStart, formEnd);
            total = total == null ? 0L : total;
            Long cancel = ownerRepository.findCancelCount(dto.getOwnerId(), formStart, formEnd);
            cancel = cancel == null ? 0L : cancel;
            if (dto.getOwnerId() == 0) {
                throw new CustomException(OwnerErrorCode.NMG);
            }
            double result = (double) cancel / total * 100;
            formattedResult = String.format("%.2f", result);


        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
        return GetGlampingCancelResponseDto.success(room, formattedResult);
    }

    @Override//매출
    @Transactional
    public ResponseEntity<? super GetOwnerRevenueResponseDto> getRevenue(ReviewGetRevenueRequestDto dto) {
        try {
            dto.setOwnerId(authenticationFacade.getLoginUserId());
            if (dto.getOwnerId() <= 0) {
                throw new CustomException(CommonErrorCode.MNF);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.MNF);
        }

        List<GetRevenue> revenue = new ArrayList<>();
        Long totalPay = 0L;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formStart = dto.getStartDayId().format(formatter);
        String formEnd = dto.getEndDayId().format(formatter);
        try {
            totalPay = ownerRepository.findTotalPay(dto.getOwnerId(), formStart, formEnd);
            revenue = ownerRepository.findRevenue(dto.getOwnerId(), formStart, formEnd);
            if (dto.getOwnerId() == 0) {
                throw new CustomException(OwnerErrorCode.NMG);
            }
        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return GetOwnerRevenueResponseDto.success(totalPay, revenue);
    }

    //========================================= < PRIVATE METHOD > =================================================//
    private void putPriceData(Long glampId, PatchOwnerPeakRequestDto p, List<GetRoomPriceItem> priceItems, double percent) {
        for (GetRoomPriceItem item : priceItems) {
            // 리스트에서 가격정보 받아옴
            Integer weekdayPrice = item.getWeekdayPrice();
            Integer weekendPrice = item.getWeekendPrice();
            //가격 인상 적용
            weekdayPrice += (int) (weekdayPrice * percent);
            weekendPrice += (int) (weekendPrice * percent);
            //세팅 된 가격 DB insert
            RoomPriceEntity entity = roomPriceRepository.findRoomPriceByRoomId(item.getRoomId());
            if (entity != null) {
                entity.setPeakWeekdayPrice(weekdayPrice);
                entity.setPeakWeekendPrice(weekendPrice);
                roomPriceRepository.save(entity);
            } else {
                throw new CustomException(CommonErrorCode.DBE);
            }


//            GlampPeakEntity gp = new GlampPeakEntity();
            GlampingEntity glampEntity = glampingRepository.findByGlampId(glampId);
            Optional<GlampPeakEntity> gp = glampPeakRepository.findByGlamp(glampEntity);

            LocalDate startDay = parseToLocalDate(p.getPeakStartDay());
            LocalDate endDay = parseToLocalDate(p.getPeakEndDay());

            if (gp.isEmpty()) { // 값이 존재하지 않으면 insert
                GlampPeakEntity g = new GlampPeakEntity();
                g.setPeakStart(startDay);
                g.setPeakEnd(endDay);
                g.setPercent(p.getPeakCost());
                g.setGlamp(glampEntity);

                glampPeakRepository.save(g);
            } else { // 있다면 update
                gp.get().setPeakStart(startDay);
                gp.get().setPeakEnd(endDay);
                gp.get().setPercent(p.getPeakCost());

                glampPeakRepository.save(gp.get());
            }
        }
    }

    private static void setPriceList(List<OwnerRoomPriceItem> priceResultSet, List<GetRoomPriceItem> priceItems) {
        for (OwnerRoomPriceItem resultSet : priceResultSet) {
            GetRoomPriceItem item = new GetRoomPriceItem();

            item.setRoomId(resultSet.getRoomId());
            item.setWeekdayPrice(resultSet.getWeekdayPrice());
            item.setWeekendPrice(resultSet.getWeekendPrice());

            priceItems.add(item);
        }
    }

    private static void setBookDetailList(List<OwnerBookItem> reservationResultSetList, List<OwnerBookDetailListItem> bookDetailListItems) {
        for (OwnerBookItem resultSet : reservationResultSetList) {
            OwnerBookDetailListItem item = new OwnerBookDetailListItem();
            item.setReservationId(resultSet.getReservationId());
            item.setInputName(resultSet.getInputName());
            item.setRoomName(resultSet.getRoomName());
            item.setPersonnel(resultSet.getPersonnel());
            item.setPayAmount(resultSet.getPayAmount());
            item.setUserPhone(resultSet.getUserPhone());
            item.setPg(resultSet.getPg());
            item.setCheckInDate(resultSet.getCheckInDate().toString());
            item.setCheckOutDate(resultSet.getCheckOutDate().toString());
            item.setCreatedAt(resultSet.getCreatedAt().toString());

            /*연박데이터 세팅*/
            int period = getPeriod(item.getCheckInDate(), item.getCheckOutDate());
            item.setPeriod(period);
            bookDetailListItems.add(item);
        }
    }

    private static void setReviewItem(List<GetUserReviewResultSet> reviewInfo, List<ReviewImageEntity> imageEntities, List<UserReviewListItem> reviewListItem) {
        for (GetUserReviewResultSet resultSet : reviewInfo) {
            UserReviewListItem item = new UserReviewListItem();
            item.setGlampName(resultSet.getGlampName());
            item.setRoomName(resultSet.getRoomName());
            item.setUserNickName(resultSet.getUserNickname());
            item.setUserProfileImage(resultSet.getUserProfileImage());
            item.setReviewId(resultSet.getReviewId());
            item.setReservationId(resultSet.getReservationId());
            item.setUserReviewContent(resultSet.getReviewContent());
            item.setStarPoint(resultSet.getReviewStarPoint());
            item.setOwnerReviewContent(resultSet.getOwnerReviewComment());
            item.setCreatedAt(resultSet.getCreatedAt().toString());
            item.setGlampId(resultSet.getGlampId());

            List<String> imageUrls = imageEntities.stream()
                    .filter(entity -> Objects.equals(entity.getReviewEntity().getReviewId(), resultSet.getReviewId()))
                    .map(ReviewImageEntity::getReviewImageName) // 경로를 파일명으로 구성
                    .collect(Collectors.toList());

            item.setReviewImages(imageUrls);

            reviewListItem.add(item);

        }
    }

    @NotNull
    private static List<ReviewEntity> getReviewEntities(List<GetUserReviewResultSet> reviewInfo) {
        List<ReviewEntity> reviewEntityList = reviewInfo.stream().map(item -> { // 1)리스트 스트림변환, 2)reviewId 값 들을 세팅해서 ReviewEntity 객체로 추출 3)추출한 값을 List 로 반환
            ReviewEntity entity = new ReviewEntity();
            entity.setReviewId(item.getReviewId());
            return entity;
        }).toList();
        return reviewEntityList;
    }


}

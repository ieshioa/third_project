package com.green.glampick.module;

import com.green.glampick.common.CustomFileUtils;
import com.green.glampick.common.Role;
import com.green.glampick.dto.object.GlampingPriceItem;
import com.green.glampick.dto.object.Repository;
import com.green.glampick.dto.object.glamping.GlampingListItem;
import com.green.glampick.dto.object.main.MainGlampingItem;
import com.green.glampick.dto.request.owner.GlampingPostRequestDto;
import com.green.glampick.entity.*;
import com.green.glampick.exception.CustomException;
import com.green.glampick.exception.errorCode.CommonErrorCode;
import com.green.glampick.exception.errorCode.OwnerErrorCode;
import com.green.glampick.repository.*;
import com.green.glampick.repository.resultset.GetPeakDateResultSet;
import com.green.glampick.security.AuthenticationFacade;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GlampingModule {

    // 오너 PK 불러오기
    public static long ownerId(AuthenticationFacade facade) {
        long loginedId = 0;
        loginedId = facade.getLoginUserId();
        if (loginedId <= 0) {
            throw new CustomException(CommonErrorCode.MNF);
        }
        return loginedId;
    }

    //사장 권한 체크
    public static void roleCheck(Role role) {
        if (role != Role.ROLE_OWNER) {
            throw new CustomException(CommonErrorCode.NP);
        }
    }

    // 글램핑을 이미 가지고 있는가?
    public static void hasGlamping(Repository repository, OwnerEntity owner) {
        GlampingWaitEntity glamping1 = repository.getGlampingWaitRepository().findByOwner(owner);
        GlampingEntity glamping2 = repository.getGlampingRepository().findByOwner(owner);

        if (glamping1 != null || glamping2 != null) {
            throw new CustomException(OwnerErrorCode.AH);
        }
    }

    // 이미지가 들어있는가?
    public static void imgExist(MultipartFile img) {
        if (img == null || img.isEmpty()) {
            throw new CustomException(OwnerErrorCode.NF);
        }
    }

    // 글램핑 위치가 중복되는가?
    public static void existingLocation(Repository repository, String location) {
        GlampingWaitEntity glamping1 = repository.getGlampingWaitRepository().findByGlampLocation(location);
        GlampingEntity glamping2 = repository.getGlampingRepository().findByGlampLocation(location);
        if (glamping1 != null || glamping2 != null) {
            throw new CustomException(OwnerErrorCode.DL);
        }
    }

    // 글램핑 위치가 중복되는가? (위치정보 수정할 때)
    public static void locationUpdate(String location, Repository repository, long glampId) {
        GlampingWaitEntity glamping1 = repository.getGlampingWaitRepository().findByGlampLocation(location);
        GlampingEntity glamping2 = repository.getGlampingRepository().findByGlampLocation(location);
        if ((glamping1 != null && glamping1.getGlampId() != glampId) ||
                (glamping2 != null && glamping2.getGlampId() != glampId)) {
            throw new CustomException(OwnerErrorCode.DL);
        }
    }

    // 전화번호 형식 변경하기 (053-000-0000 으로 왔을 때 0530000000 으로 바꾸기)
    public static String glampingCall(String call) {
        if (Pattern.compile("^[0-9]*?").matcher(call).matches()) {
            return call;
        }
        StringBuilder sb = new StringBuilder();
        Matcher matcher = Pattern.compile("[0-9]+").matcher(call);

        while (matcher.find()) {
            sb.append(matcher.group());
        }
        return sb.toString();
    }

    // 이미지 업로드
    public static String imageUpload(CustomFileUtils customFileUtils, MultipartFile img, long glampId, String table) {
        // 이미지 파일명 만들기
        String glmapImgName = customFileUtils.makeRandomFileName(img);

        // 이미지 url로 저장하기
        String picNameUrl = String.format("/pic/%s/%d/glamp/%s", table, glampId, glmapImgName);

        // 글램핑 대표 이미지 넣기
        try {
            // 폴더 : /glamping/{glampId}
            String glampPath = String.format("%s/%s/glamp", table, glampId);
            customFileUtils.makeFolders(glampPath);
            // 파일을 저장한다
            String target = String.format("/%s/%s", glampPath, glmapImgName);
            customFileUtils.transferTo(img, target);
        } catch (Exception e) {
            throw new CustomException(OwnerErrorCode.FE);
        }

        return picNameUrl;
    }

    // 사용자가 가진 글램핑과 입력받은 pk 가 일치하는지 확인
    public static void isGlampIdOk(GlampingRepository glampingRepository,
                                   OwnerEntity owner, long glampId) {
        Long readGlampId = null;
        try {
            GlampingEntity glamp = glampingRepository.findByOwner(owner);
            readGlampId = glamp.getGlampId();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (readGlampId == null || readGlampId != glampId) {
                throw new CustomException(OwnerErrorCode.NMG);
            }
        }
    }

    public static GlampingPostRequestDto dtoNull(GlampingPostRequestDto dto, GlampingEntity entity) {
        if (dto.getGlampName() == null || dto.getGlampName().isEmpty()) {
            dto.setGlampName(entity.getGlampName());
        }
        if (dto.getGlampCall() == null || dto.getGlampCall().isEmpty()) {
            dto.setGlampCall(entity.getGlampCall());
        }
        if (dto.getGlampLocation() == null || dto.getGlampLocation().isEmpty()) {
            dto.setGlampLocation(entity.getGlampLocation());
        }
        if (dto.getRegion() == null || dto.getRegion().isEmpty()) {
            dto.setRegion(entity.getRegion());
        }
        if (dto.getExtraCharge() == null || dto.getExtraCharge() < 0) {
            dto.setExtraCharge(entity.getExtraCharge());
        }
        if (dto.getIntro() == null || dto.getIntro().isEmpty()) {
            dto.setIntro(entity.getGlampIntro());
        }
        if (dto.getBasic() == null || dto.getBasic().isEmpty()) {
            dto.setBasic(entity.getInfoBasic());
        }
        if (dto.getNotice() == null || dto.getNotice().isEmpty()) {
            dto.setNotice(entity.getInfoNotice());
        }
        if (dto.getTraffic() == null || dto.getTraffic().isEmpty()) {
            dto.setTraffic(entity.getTraffic());
        }
        return dto;
    }

    // 예약 된 글램핑이 있는지 확인
    public static void existReservation(GlampingEntity glamp, ReservationBeforeRepository reservationBeforeRepository) {
        List<ReservationBeforeEntity> reservation = reservationBeforeRepository.findByGlamping(glamp);
        if (reservation != null && !reservation.isEmpty()) {
            throw new CustomException(OwnerErrorCode.CD);
        }
    }

    // 객실 가격 넣기
    // <T extends GlampingPriceItem> : GlampingPriceItem 를 상속받은 모든 타입
    public static <T extends GlampingPriceItem> List<T> setRoomPrice(List<T> items, boolean weekend, Repository repository) {
        for (T item : items) {
            // 성수기 날짜 가져옴
            GetPeakDateResultSet peakResult = repository.getGlampPeakRepository().getPeak(item.getGlampId());
            // 해당 글램핑에 해당하는 모든 객실
            List<RoomEntity> room = RoomModule.getRoomEntity(item.getGlampId(), repository.getRoomRepository());
            // 성수기인지, 주말인지 판단하고 그 중 최저가를 골라와서 set 해줌
            int price = RoomModule.getRoomPrice(room, weekend,
                    !(peakResult == null || !DateModule.isPeak(peakResult)), repository.getRoomPriceRepository());
            item.setPrice(price);
        }
        return items;
    }
    public static <T extends GlampingPriceItem> List<T> setRoomPrice(List<T> items, boolean weekend
            , Repository repository, List<RoomEntity> room) {
        for (T item : items) {
            GetPeakDateResultSet peakResult = repository.getGlampPeakRepository().getPeak(item.getGlampId());
            int price = RoomModule.getRoomPrice(room, weekend,
                    !(peakResult == null || !DateModule.isPeak(peakResult)), repository.getRoomPriceRepository());
            item.setPrice(price);
        }
        return items;
    }


}

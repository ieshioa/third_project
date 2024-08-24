package com.green.glampick.service.implement;

import com.green.glampick.dto.object.GlampingPriceItem;
import com.green.glampick.dto.object.Repository;
import com.green.glampick.dto.object.main.MainGlampingItem;
import com.green.glampick.dto.response.admin.GetBannerResponseDto;
import com.green.glampick.dto.response.main.GetMainGlampingListResponseDto;
import com.green.glampick.entity.BannerEntity;
import com.green.glampick.entity.RoomEntity;
import com.green.glampick.exception.CustomException;
import com.green.glampick.exception.errorCode.CommonErrorCode;
import com.green.glampick.mapper.MainMapper;
import com.green.glampick.module.DateModule;
import com.green.glampick.module.GlampingModule;
import com.green.glampick.module.RoomModule;
import com.green.glampick.repository.BannerRepository;
import com.green.glampick.repository.GlampPeakRepository;
import com.green.glampick.repository.RoomPriceRepository;
import com.green.glampick.repository.RoomRepository;
import com.green.glampick.repository.resultset.GetPeakDateResultSet;
import com.green.glampick.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {
    private final BannerRepository bannerRepository;
    private final GlampPeakRepository glampPeakRepository;
    private final RoomPriceRepository roomPriceRepository;
    private final RoomRepository roomRepository;
    private final MainMapper mapper;

    private Repository repository() {
        return Repository.builder().glampPeakRepository(glampPeakRepository)
                .roomPriceRepository(roomPriceRepository)
                .roomRepository(roomRepository).build();
    }
    //  메인 페이지 - 글램핑 리스트 불러오기  //
    @Override
    @Transactional
    public ResponseEntity<? super GetMainGlampingListResponseDto> mainGlampingList() {

        boolean weekend = DateModule.isWeekend();   // 현재 날짜가 주말이면 true

        try {

            //  메인페이지에 불러올 인기순, 반려동물 동반, 마운틴뷰에 대한 글램핑장 리스트를 불러온다.  //
            List<MainGlampingItem> popular = mapper.popular();
            List<MainGlampingItem> petFriendly = mapper.petFriendly();
            List<MainGlampingItem> mountainView = mapper.mountainView();
          
            // 가격 넣기
            popular = GlampingModule.setRoomPrice(popular, weekend, repository());
            petFriendly = GlampingModule.setRoomPrice(petFriendly, weekend, repository());
            mountainView = GlampingModule.setRoomPrice(mountainView, weekend, repository());


            return GetMainGlampingListResponseDto.success(popular, petFriendly, mountainView);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
    }

    //  메인 페이지 - 메인 화면 배너 불러오기  //
    @Override
    @Transactional
    public ResponseEntity<? super GetBannerResponseDto> getBanner() {

        List<BannerEntity> list = bannerRepository.findAll();

        return GetBannerResponseDto.success(list);

    }

}
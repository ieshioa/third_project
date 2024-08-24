package com.green.glampick.service;

import com.green.glampick.dto.response.admin.GetBannerResponseDto;
import com.green.glampick.dto.response.main.GetMainGlampingListResponseDto;
import org.springframework.http.ResponseEntity;

public interface MainService {

    //  메인 페이지 - 글램핑 리스트 불러오기  //
    ResponseEntity<? super GetMainGlampingListResponseDto> mainGlampingList();

    //  메인 페이지 - 메인 화면 배너 불러오기  //
    ResponseEntity<? super GetBannerResponseDto> getBanner();

}

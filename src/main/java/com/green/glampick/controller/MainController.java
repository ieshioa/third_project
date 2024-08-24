package com.green.glampick.controller;

import com.green.glampick.dto.response.admin.GetBannerResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.green.glampick.dto.response.main.GetMainGlampingListResponseDto;
import com.green.glampick.service.MainService;

import static com.green.glampick.common.swagger.description.main.GetMainGlampingSwaggerDescription.MAIN_GLAMPING_DESCRIPTION;
import static com.green.glampick.common.swagger.description.main.GetMainGlampingSwaggerDescription.MAIN_GLAMPING_RESPONSE_ERROR_CODE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
@Tag(name = "메인 컨트롤러")
public class MainController {

    private final MainService service;

    @GetMapping
    @Operation(summary = "인기 글램핑 리스트 (김민지)", description = MAIN_GLAMPING_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = MAIN_GLAMPING_RESPONSE_ERROR_CODE,
        content = @Content(
                mediaType = "application/json", schema = @Schema(implementation = GetMainGlampingListResponseDto.class)))
    public ResponseEntity<? super GetMainGlampingListResponseDto> mainGlampingList() {
        return service.mainGlampingList();
    }

    @GetMapping("banner")
    @Operation(summary = "메인 화면 배너 불러오기 (김수찬)", description = "")
    @ApiResponse(responseCode = "200", description = "",
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetBannerResponseDto.class)))
    ResponseEntity<? super GetBannerResponseDto> getBanner() {
        return service.getBanner();
    }
}
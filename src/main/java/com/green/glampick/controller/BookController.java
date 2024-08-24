package com.green.glampick.controller;

import com.green.glampick.dto.request.book.GetBookPayRequestDto;
import com.green.glampick.dto.request.book.PostBookRequestDto;
import com.green.glampick.dto.response.admin.PatchAccessOwnerSignUpResponseDto;
import com.green.glampick.dto.response.book.GetBookPayResponseDto;
import com.green.glampick.dto.response.book.PostBookResponseDto;
import com.green.glampick.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.green.glampick.common.swagger.description.admin.PatchAccessSignUpSwaggerDescription.ACCESS_SIGN_UP_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.book.GetReservationAmountSwaggerDescription.GET_RESERVATION_AMOUNT_DESCRIPTION;
import static com.green.glampick.common.swagger.description.book.GetReservationAmountSwaggerDescription.GET_RESERVATION_AMOUNT_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.book.PostBookSwaggerDescription.POST_BOOK_DESCRIPTION;
import static com.green.glampick.common.swagger.description.book.PostBookSwaggerDescription.POST_BOOK_RESPONSE_ERROR_CODE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
@Tag(name = "예약 컨트롤러")
public class BookController {
    private final BookService service;

    //  예약 페이지 - 글램핑 예약하기  //
    @PostMapping
    @Operation(summary = "글램핑 예약하기 (김수찬)", description = POST_BOOK_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = POST_BOOK_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PostBookResponseDto.class)
            ))
    public ResponseEntity<? super PostBookResponseDto> postBook(@RequestBody PostBookRequestDto dto) {
        return service.postBook(dto);
    }

    //  예약 페이지 - 최종 결제 가격정보  //
    @GetMapping("reservation")
    @Operation(summary = "최종 결제가격 정보 (김수찬)", description = GET_RESERVATION_AMOUNT_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = GET_RESERVATION_AMOUNT_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = GetBookPayResponseDto.class)
            ))
    public ResponseEntity<? super GetBookPayResponseDto> getReservationAmount (@ParameterObject @ModelAttribute GetBookPayRequestDto dto) {
        return service.getReservationAmount(dto);
    }

    // 강국 =================================================================================================================



}

package com.green.glampick.dto.request.owner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class RoomPostRequestDto {

    @JsonIgnore
    private long roomId;

    @NotNull(message = "글램핑 PK를 입력해주세요.")
    @Schema(example = "1", description = "글램핑 PK")
    private long glampId;

    @NotBlank(message = "객실 이름을 입력해주세요.")
    @Size(max = 30, message = "객실의 이름은 최대 30자 가능합니다.")
    @Schema(example = "카라반 102호", description = "객실 이름")
    private String roomName;

    @NotNull(message = "주중 객실의 가격을 입력해주세요.")
    @Schema(example = "65500", description = "주중 가격")
    private Integer weekdayPrice;

    @NotNull(message = "주말 객실의 가격을 입력해주세요.")
    @Schema(example = "65500", description = "주말 가격")
    private Integer weekendPrice;

    @NotNull(message = "기준 인원을 입력해주세요.")
    @Min(value = 2, message = "객실의 인원은 2명부터 6명까지 가능합니다.")
    @Schema(example = "2", description = "기준 인원")
    private Integer peopleNum;

    @NotNull(message = "최대 인원을 입력해주세요.")
    @Max(value = 6, message = "객실의 인원은 2명부터 6명까지 가능합니다.")
    @Schema(example = "6", description = "최대 인원")
    private Integer peopleMax;

    @NotBlank(message = "입실 시간을 입력해주세요.")
    @Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$",message = "시간 형식을 맞춰서 입력해주세요. ex)12:00:00")
    @Schema(example = "15:00:00", description = "입실 시간")
    private String inTime;

    @NotBlank(message = "퇴실 시간을 입력해주세요.")
    @Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$",message = "시간 형식을 맞춰서 입력해주세요. ex)12:00:00")
    @Schema(example = "12:00:00", description = "퇴실 시간")
    private String outTime;

    // 이미지
    @JsonIgnore
    private List<String> roomImgName;

    @Schema(example = "[1,2,3]", description = "객실 서비스")
    private List<Long> service;




}

package com.green.glampick.dto.request.glamping;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.glampick.common.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.List;

import static com.green.glampick.common.GlobalConst.PAGING_SIZE;

@Getter
@Setter
public class GlampingSearchRequestDto extends Paging {

    public GlampingSearchRequestDto(Integer page) {
        super(page, PAGING_SIZE);
    }

    @NotBlank(message = "지역 분류를 입력해주세요.")
    @Schema(example = "gyeongbuk", description = "지역")
    private String region;

    @NotBlank(message = "체크인 날짜를 입력해주세요.")
    @Schema(example = "2024-06-28", description = "체크인")
    private String inDate;

    @NotBlank(message = "체크아웃 날짜를 입력해주세요.")
    @Schema(example = "2024-06-29", description = "체크아웃")
    private String outDate;

    @NotNull(message = "인원수를 입력해주세요.")
    @Min(value = 2, message = "인원은 2명부터 6명까지 선책 가능합니다.")
    @Max(value = 6, message = "인원은 2명부터 6명까지 선책 가능합니다.")
    @Schema(example = "2", description = "인원수")
    private Integer people;

    @Schema(example = "그린", description = "검색어")
    private String searchWord;

    @Range(min = 1, max = 5, message = "1:추천순  2:별점순  3:리뷰수순  4:저가순  5:고가순")
    @Schema(example = "2", description = "검색 결과 정렬 기준")
    private Integer sortType;

    @Schema(example = "1, 2, 3", description = "선택된 서비스")
    private List<Integer> filter;

    @JsonIgnore
    private int filterSize = 0;

    @JsonIgnore
    private List<Long> roomList;
}

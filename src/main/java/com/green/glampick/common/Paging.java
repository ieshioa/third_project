package com.green.glampick.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

import static com.green.glampick.common.GlobalConst.PAGING_SIZE;


@Getter
@ToString
public class Paging {

    @Schema(example = "1", description = "선택한 page")
    private int page; //페이지 값

    @JsonIgnore
    private int size; //페이지 당 레코드 수

    @JsonIgnore
    private int startIdx;

    public Paging(Integer page, Integer size) {
        this.page = page == null ? 1 : page;
        this.size = size == null ? 10 : size;
        this.startIdx = (this.page - 1) * this.size;
    }

    public int getOffset() {
        return startIdx;
    }

    public int getLimit() {
        return size;
    }
}

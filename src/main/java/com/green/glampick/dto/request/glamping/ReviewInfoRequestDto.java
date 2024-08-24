package com.green.glampick.dto.request.glamping;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.glampick.common.GlobalConst;
import com.green.glampick.common.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewInfoRequestDto extends Paging {
 @Schema(example = "1", description = "글램핑PK")
 private long glampId;
 @JsonIgnore
 private long userId;
 public ReviewInfoRequestDto(Integer page) {
   super(page, GlobalConst.PAGING_SIZE);
 }
}

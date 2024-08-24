package com.green.glampick.dto.request.owner;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatchOwnerInfoRequestDto {
    @Schema(example = "qwert1234!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
            , message = "비밀번호는 8자 이상이어햐 하고, 하나 이상의 소문자, 숫자, 특수문자(@$!%*?&)가 필요합니다.")
    private String ownerPw;
    @Schema(example = "010-1234-5678")
    @Size(min = 10, max = 13, message = "휴대폰 번호가 올바르게 입력되지 않았습니다.")
    private String phoneNum;
}

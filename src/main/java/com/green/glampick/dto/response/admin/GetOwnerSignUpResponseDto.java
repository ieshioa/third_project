package com.green.glampick.dto.response.admin;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.entity.OwnerEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class GetOwnerSignUpResponseDto extends ResponseDto {

    private String businessNumber;
    private String ownerName;
    private String ownerPhone;
    private String businessPaperImage;

    private GetOwnerSignUpResponseDto(OwnerEntity ownerEntity) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.businessNumber = ownerEntity.getBusinessNumber();
        this.ownerName = ownerEntity.getOwnerName();
        this.ownerPhone = ownerEntity.getOwnerPhone();
        this.businessPaperImage = ownerEntity.getBusinessPaperImage();
    }

    public static ResponseEntity<GetOwnerSignUpResponseDto> success(OwnerEntity ownerEntity) {
        GetOwnerSignUpResponseDto result = new GetOwnerSignUpResponseDto(ownerEntity);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}

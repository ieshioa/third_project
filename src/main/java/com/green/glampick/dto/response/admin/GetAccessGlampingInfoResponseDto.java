package com.green.glampick.dto.response.admin;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.entity.GlampingEntity;
import com.green.glampick.entity.GlampingWaitEntity;
import com.green.glampick.entity.OwnerEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class GetAccessGlampingInfoResponseDto extends ResponseDto {

    private Long ownerId;
    private String glampName;
    private String glampCall;
    private String glampImage;
    private String glampLocation;
    private String region;
    private Integer extraCharge;
    private String glampIntro;
    private String infoBasic;
    private String infoNotice;
    private String traffic;

    private GetAccessGlampingInfoResponseDto(GlampingWaitEntity glampingEntity) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.ownerId = glampingEntity.getOwner().getOwnerId();
        this.glampName = glampingEntity.getGlampName();
        this.glampCall = glampingEntity.getGlampCall();
        this.glampImage = glampingEntity.getGlampImage();
        this.glampLocation = glampingEntity.getGlampLocation();
        this.region = glampingEntity.getRegion();
        this.extraCharge = glampingEntity.getExtraCharge();
        this.glampIntro = glampingEntity.getGlampIntro();
        this.infoBasic = glampingEntity.getInfoBasic();
        this.infoNotice = glampingEntity.getInfoNotice();
        this.traffic = glampingEntity.getTraffic();
    }

    public static ResponseEntity<GetAccessGlampingInfoResponseDto> success(GlampingWaitEntity glampingEntity) {
        GetAccessGlampingInfoResponseDto result = new GetAccessGlampingInfoResponseDto(glampingEntity);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}

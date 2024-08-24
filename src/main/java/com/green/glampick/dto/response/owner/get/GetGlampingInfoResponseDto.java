package com.green.glampick.dto.response.owner.get;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.owner.GetGlampingInfoItem;
import com.green.glampick.repository.resultset.GetGlampingInfoResultSet;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
public class GetGlampingInfoResponseDto extends ResponseDto {

    private GetGlampingInfoItem result;

    private GetGlampingInfoResponseDto(GetGlampingInfoItem result) {
        super(SUCCESS_CODE, "글램핑 정보 리스트를 불러왔습니다.");
        this.result = result;
    }

    public static ResponseEntity<GetGlampingInfoResponseDto> success(boolean state, GetGlampingInfoResultSet resultSet) {
        GetGlampingInfoResponseDto result = new GetGlampingInfoResponseDto(state, resultSet.getGlampId(), resultSet.getName(), resultSet.getCall()
                , resultSet.getImage(), resultSet.getLocation(), resultSet.getRegion(), resultSet.getCharge()
                , resultSet.getIntro(), resultSet.getBasic(), resultSet.getNotice(), resultSet.getTraffic());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public GetGlampingInfoResponseDto(boolean state, Long glampId, String glampName, String glampCall, String glampImage
            , String glampLocation, String region, Integer extraCharge, String glampIntro, String infoBasic
            , String infoNotice, String traffic){
        super(SUCCESS_CODE, "글램핑 정보 리스트를 불러왔습니다.");
        this.result = new GetGlampingInfoItem(state, glampId, glampName, glampCall, glampImage,
                glampLocation, region, extraCharge, glampIntro, infoBasic, infoNotice, traffic, null);
    }


    public static ResponseEntity<GetGlampingInfoResponseDto> successWait(GetGlampingInfoItem result) {
        GetGlampingInfoResponseDto response = new GetGlampingInfoResponseDto(result);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

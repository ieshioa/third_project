package com.green.glampick.dto.response.main;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.GlampingPriceItem;
import com.green.glampick.dto.object.main.MainGlampingItem;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class GetMainGlampingListResponseDto extends ResponseDto {

    private List<MainGlampingItem> popular;
    private List<MainGlampingItem> petFriendly;
    private List<MainGlampingItem> mountainView;

    public GetMainGlampingListResponseDto(List<MainGlampingItem> popular, List<MainGlampingItem> petFriendly
                                , List<MainGlampingItem> mountainView) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.popular = popular;
        this.petFriendly = petFriendly;
        this.mountainView = mountainView;
    }

    public static ResponseEntity<ResponseDto> success(List<MainGlampingItem> popular, List<MainGlampingItem> petFriendly
            , List<MainGlampingItem> mountainView) {
        GetMainGlampingListResponseDto result = new GetMainGlampingListResponseDto(popular, petFriendly, mountainView);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}

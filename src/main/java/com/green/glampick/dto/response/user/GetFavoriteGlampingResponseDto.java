package com.green.glampick.dto.response.user;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.user.GetFavoriteGlampingItem;
import com.green.glampick.repository.resultset.GetFavoriteGlampingResultSet;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;


@Setter
@Getter
public class GetFavoriteGlampingResponseDto extends ResponseDto {



    private List<GetFavoriteGlampingItem> favoritelist;

    private GetFavoriteGlampingResponseDto(List<GetFavoriteGlampingItem> favoritelist) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.favoritelist = favoritelist;
    }

    public static ResponseEntity<ResponseDto> success(List<GetFavoriteGlampingItem> favoritelist) {
        GetFavoriteGlampingResponseDto result = new GetFavoriteGlampingResponseDto(favoritelist);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}

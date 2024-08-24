package com.green.glampick.dto.response.user;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.repository.resultset.GetReservationBeforeResultSet;
import com.green.glampick.repository.resultset.GetReservationCancelResultSet;
import com.green.glampick.repository.resultset.GetReservationCompleteResultSet;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Setter
@Getter
public class GetBookResponseDto extends ResponseDto {

    private List<GetReservationBeforeResultSet> reservationBeforeResultSetList;
    private List<GetReservationCompleteResultSet> reservationCompleteResultSetList;
    private List<GetReservationCancelResultSet> reservationCancelResultSetList;

    private GetBookResponseDto(List<GetReservationBeforeResultSet> reservationBeforeResultSetList
                                , List<GetReservationCompleteResultSet> reservationCompleteResultSetList
                                , List<GetReservationCancelResultSet> reservationCancelResultSetList)
    {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.reservationBeforeResultSetList = reservationBeforeResultSetList;
        this.reservationCancelResultSetList = reservationCancelResultSetList;
        this.reservationCompleteResultSetList = reservationCompleteResultSetList;
    }

    public static ResponseEntity<ResponseDto> success
            (List<GetReservationBeforeResultSet> reservationBeforeResultSetList
            , List<GetReservationCompleteResultSet> reservationCompleteResultSetList
            , List<GetReservationCancelResultSet> reservationCancelResultSetList)
    {
        GetBookResponseDto result
                = new GetBookResponseDto
                (reservationBeforeResultSetList, reservationCompleteResultSetList, reservationCancelResultSetList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}

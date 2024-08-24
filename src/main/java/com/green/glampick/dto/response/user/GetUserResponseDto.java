package com.green.glampick.dto.response.user;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class GetUserResponseDto extends ResponseDto {

    private String userEmail;
    private String userName;
    private String userNickname;
    private String userPw;
    private String userPhone;
    private String userProfileImage;

    private GetUserResponseDto(UserEntity userEntity) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.userEmail = userEntity.getUserEmail();
        this.userName = userEntity.getUserName();
        this.userNickname = userEntity.getUserNickname();
        this.userPw = userEntity.getUserPw();
        this.userPhone = userEntity.getUserPhone();
        this.userProfileImage = userEntity.getUserProfileImage();
    }

    public static ResponseEntity<GetUserResponseDto> success(UserEntity userEntity) {
        GetUserResponseDto result = new GetUserResponseDto(userEntity);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}

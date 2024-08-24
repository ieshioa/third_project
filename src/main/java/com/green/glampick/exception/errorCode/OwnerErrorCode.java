package com.green.glampick.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OwnerErrorCode implements ErrorCode {

    // 400
    WG(HttpStatus.BAD_REQUEST, "글램핑 ID를 올바르게 입력해주세요."), // WRONG_GLAMP_ID
    AH(HttpStatus.BAD_REQUEST, "이미 회원님의 계정에 등록된 글램핑 정보가 있습니다."), // ALREADY_HAVE
    NF(HttpStatus.BAD_REQUEST, "사진을 찾지 못했습니다."), // NOT_FOUND_FILE
    TI(HttpStatus.BAD_REQUEST, "사진은 최대 10장 업로드 가능합니다."), // TOO_MANY_IMG
    DL(HttpStatus.BAD_REQUEST, "이미 같은 위치에 등록된 글램핑장이 존재합니다."), // DUPLICATED_LOCATION
    FE(HttpStatus.BAD_REQUEST, "파일을 업로드하는 과정에서 에러가 생겼습니다."),    // FILE_UPLOAD_ERROR
    NMG(HttpStatus.BAD_REQUEST, "등록된 글램핑 정보를 찾지 못했습니다. 글램핑 ID를 확인해주세요."),    // NOT_MATCH_GLAMP
    NMR(HttpStatus.BAD_REQUEST, "입력한 객실이 사용자가 가진 객실과 다릅니다. 룸 ID를 확인해주세요."),    // NOT_MATCH_ROOM
    IT(HttpStatus.BAD_REQUEST, "시간 형식이 잘못되었습니다. ( 시:분:초 12:00:00 )"),    // INVALID_TIME
    PE(HttpStatus.BAD_REQUEST, "최대 인원은 기준 인원보다 작을 수 없습니다."),    // PERSONNEL_ERROR
    PUE(HttpStatus.BAD_REQUEST, "객실 인원은 2명부터 6명까지 가능합니다."),    // PERSONNEL_UPDATE_ERROR
    CF(HttpStatus.BAD_REQUEST, "파일을 삭제하지 못했습니다."),    // CANT_DELETE_FILE
    CFI(HttpStatus.BAD_REQUEST, "사진을 삭제할 수 없습니다. 사진 ID를 다시 확인해주세요."),    // CANT_FIND_IMAGE
    CD(HttpStatus.BAD_REQUEST, "해당 글램핑에는 이용 예정인 예약이 존재하여 탈퇴를 할 수 없습니다."),    // CAN_NOT_DELETE
    DTE(HttpStatus.BAD_REQUEST, "입력하신 날짜가 벗어났습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}

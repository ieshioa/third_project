package com.green.glampick.service;

import com.green.glampick.dto.request.login.*;
import com.green.glampick.dto.response.login.*;
import com.green.glampick.dto.response.login.mail.PostMailCheckResponseDto;
import com.green.glampick.dto.response.login.mail.PostMailSendResponseDto;
import com.green.glampick.dto.response.login.sms.PostSmsCheckResponseDto;
import com.green.glampick.dto.response.login.sms.PostSmsSendResponseDto;
import com.green.glampick.dto.response.login.social.PostSnsSignUpResponseDto;
import com.green.glampick.dto.response.login.token.GetAccessTokenResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface LoginService {

    //  최초 실행 시, 초기화를 한번만 진행  //
    void init();

    //  6자리의 랜덤 숫자코드를 생성  //
    int createKey();

    //  로그인 및 회원가입 페이지 - 이메일 회원가입 처리  //
    ResponseEntity<? super PostSignUpResponseDto> signUpUser(SignUpRequestDto dto);

    //  로그인 및 회원가입 페이지 - 소셜 회원가입 처리  //
    ResponseEntity<? super PostSnsSignUpResponseDto> signUpSnsUser(SignUpSnsRequestDto dto);

    //  로그인 및 회원가입 페이지 - 사장님 회원가입 처리  //
    ResponseEntity<? super PostOwnerSignUpResponseDto> signUpOwner(MultipartFile file, OwnerSignUpRequestDto dto);

    //  로그인 및 회원가입 페이지 - 이메일 로그인 처리  //
    ResponseEntity<? super PostSignInResponseDto> signInUser(HttpServletResponse res, SignInRequestDto dto);

    //  로그인 및 회원가입 페이지 - 사장님 로그인 처리  //
    ResponseEntity<? super PostOwnerSignInResponseDto> signInOwner(HttpServletResponse res, OwnerSignInRequestDto dto);

    //  로그인 및 회원가입 페이지 - 관리자 로그인 처리  //
    ResponseEntity<? super PostAdminSignInResponseDto> signInAdmin(HttpServletResponse res, AdminSignInRequestDto dto);

    //  AccessToken 불러오기  //
    ResponseEntity<? super GetAccessTokenResponseDto> getAccessToken(HttpServletRequest req);

    //  로그아웃 처리하기  //
    ResponseEntity<? super PostSignOutResponseDto> signOut(HttpServletResponse res);

    //  로그인 및 회원가입 페이지 - 유저 휴대폰 인증 문자 보내기  //
    ResponseEntity<? super PostSmsSendResponseDto> sendOne(String userPhone);

    //  로그인 및 회원가입 페이지 - 유저 휴대폰 인증코드 체크하기  //
    ResponseEntity<? super PostSmsCheckResponseDto> checkPhone(String userPhone, int phoneKey);

    //  로그인 및 회원가입 페이지 - 유저 이메일 인증 보내기  //
    ResponseEntity<? super PostMailSendResponseDto> sendAuthCode(String userEmail);

    //  로그인 및 회원가입 페이지 - 유저 이메일 코드 체크하기  //
    ResponseEntity<? super PostMailCheckResponseDto> checkCode(String userEmail, int authKey);

    //  로그인 및 회원가입 페이지 - 사장님 휴대폰 인증 문자 보내기  //
    ResponseEntity<? super PostSmsSendResponseDto> sendOneOwner(String userPhone);

    //  로그인 및 회원가입 페이지 - 사장님 휴대폰 인증코드 체크하기  //
    ResponseEntity<? super PostSmsCheckResponseDto> checkPhoneOwner(String userPhone, int phoneKey);

    //  로그인 및 회원가입 페이지 - 사장님 이메일 인증 보내기  //
    ResponseEntity<? super PostMailSendResponseDto> sendAuthCodeOwner(String userEmail);

    //  로그인 및 회원가입 페이지 - 사장님 이메일 코드 체크하기  //
    ResponseEntity<? super PostMailCheckResponseDto> checkCodeOwner(String userEmail, int emailKey);

    //  로그인 및 회원가입 페이지 - 유저 이메일 찾기  //
    ResponseEntity<? super PostSearchEmailResponseDto> searchEmail (PostSearchEmailRequestDto dto);

    //  로그인 및 회원가입 페이지 - 유저 이메일 찾기 - 휴대폰 인증 보내기  //
    ResponseEntity<? super PostSmsSendResponseDto> sendOneSearchEmail(String userPhone);

    //  로그인 및 회원가입 페이지 - 유저 이메일 찾기 - 휴대폰 인증 체크하기  //
    ResponseEntity<? super PostSmsCheckResponseDto> checkPhoneSearchEmail(String userPhone, int phoneKey);

    //  로그인 및 회원가입 페이지 - 유저 비밀번호 찾기 후 변경 처리  //
    ResponseEntity<? super PostSearchPwResponseDto> searchPw(PostSearchPwRequestDto dto);

    //  로그인 및 회원가입 페이지 - 유저 비밀번호 찾기 - 이메일 인증 보내기  //
    ResponseEntity<? super PostMailSendResponseDto> sendMailSearchPw(String userEmail);

    //  로그인 및 회원가입 페이지 - 유저 비밀번호 찾기 - 이메일 인증 체크하기  //
    ResponseEntity<? super PostMailCheckResponseDto> mailCheckSearchPw(String userEmail, int emailKey);

    //  로그인 및 회원가입 페이지 - 사장님 이메일 찾기  //
    ResponseEntity<? super PostSearchEmailResponseDto> searchOwnerEmail(PostSearchOwnerEmailRequestDto dto);

    //  로그인 및 회원가입 페이지 - 사장님 이메일 찾기 - 휴대폰 인증 보내기  //
    ResponseEntity<? super PostSmsSendResponseDto> sendOneSearchOwnerEmail(String ownerPhone);

    //  로그인 및 회원가입 페이지 - 사장님 이메일 찾기 - 휴대폰 인증 체크하기  //
    ResponseEntity<? super PostSmsCheckResponseDto> checkPhoneSearchOwnerEmail(String ownerPhone, int phoneKey);

    //  로그인 및 회원가입 페이지 - 사장님 비밀번호 검증 후 변경처리  //
    ResponseEntity<? super PostSearchPwResponseDto> searchOwnerPw(PostSearchOwnerPwRequestDto dto);

    //  로그인 및 회원가입 페이지 - 사장님 비밀번호 찾기 - 이메일 인증 보내기  //
    ResponseEntity<? super PostMailSendResponseDto> sendMailSearchOwnerPw(String ownerEmail);

    //  로그인 및 회원가입 페이지 - 사장님 비밀번호 찾기 - 이메일 인증 체크하기  //
    ResponseEntity<? super PostMailCheckResponseDto> mailCheckSearchOwnerPw(String ownerEmail, int emailKey);


}

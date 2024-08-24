package com.green.glampick.controller;

import com.green.glampick.dto.request.login.*;
import com.green.glampick.dto.response.login.*;
import com.green.glampick.dto.response.login.mail.PostMailCheckResponseDto;
import com.green.glampick.dto.response.login.mail.PostMailSendResponseDto;
import com.green.glampick.dto.response.login.sms.PostSmsCheckResponseDto;
import com.green.glampick.dto.response.login.sms.PostSmsSendResponseDto;
import com.green.glampick.dto.response.login.social.PostSnsSignUpResponseDto;
import com.green.glampick.dto.response.login.token.GetAccessTokenResponseDto;
import com.green.glampick.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.http.Multipart;

import static com.green.glampick.common.swagger.description.login.GetAccessTokenSwaggerDescription.ACCESS_TOKEN_DESCRIPTION;
import static com.green.glampick.common.swagger.description.login.PostCheckMailSwaggerDescription.CHECK_MAIL_DESCRIPTION;
import static com.green.glampick.common.swagger.description.login.PostCheckMailSwaggerDescription.CHECK_MAIL_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.login.PostCheckSmsSwaggerDescription.CHECK_SMS_DESCRIPTION;
import static com.green.glampick.common.swagger.description.login.PostCheckSmsSwaggerDescription.CHECK_SMS_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.login.PostSendMailSwaggerDescription.SEND_MAIL_DESCRIPTION;
import static com.green.glampick.common.swagger.description.login.PostSendMailSwaggerDescription.SEND_MAIL_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.login.PostSendSmsSwaggerDescription.SEND_SMS_DESCRIPTION;
import static com.green.glampick.common.swagger.description.login.PostSendSmsSwaggerDescription.SEND_SMS_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.login.PostSignInSwaggerDescription.SIGN_IN_DESCRIPTION;
import static com.green.glampick.common.swagger.description.login.PostSignInSwaggerDescription.SIGN_IN_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.login.PostSignUpSwaggerDescription.SIGN_UP_DESCRIPTION;
import static com.green.glampick.common.swagger.description.login.PostSignUpSwaggerDescription.SIGN_UP_RESPONSE_ERROR_CODE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "로그인 및 회원가입 컨트롤러")
public class LoginController {
    private final LoginService service;

    @PostMapping("/sign-up")
    @Operation(summary = "이메일 회원가입 (김수찬)", description = SIGN_UP_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = SIGN_UP_RESPONSE_ERROR_CODE,
        content = @Content(
                mediaType = "application/json", schema = @Schema(implementation = PostSignUpResponseDto.class)))
    public ResponseEntity<? super PostSignUpResponseDto> signUpUser(@RequestBody @Valid SignUpRequestDto dto) {
        return service.signUpUser(dto);
    }

    @PostMapping("/social/sign-up")
    @Operation(summary = "소셜 회원가입 (김수찬)", description = "")
    @ApiResponse(responseCode = "200", description = "",
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PostSnsSignUpResponseDto.class)))
    public ResponseEntity<? super PostSnsSignUpResponseDto> signUpSnsUser(@RequestBody @Valid SignUpSnsRequestDto dto) {
        return service.signUpSnsUser(dto);
    }

    @PostMapping(value = "/owner/sign-up", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "사장님 회원가입 (김수찬)", description = "")
    @ApiResponse(responseCode = "200", description = "",
        content = @Content(
                mediaType = "application/json", schema = @Schema(implementation = PostOwnerSignUpResponseDto.class)
        ))
    public ResponseEntity<? super PostOwnerSignUpResponseDto> signUpOwner(@RequestPart MultipartFile file, @RequestPart OwnerSignUpRequestDto dto) {
        return service.signUpOwner(file, dto);
    }

    @PostMapping("/sign-in")
    @Operation(summary = "이메일 로그인 (김수찬)", description = SIGN_IN_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = SIGN_IN_RESPONSE_ERROR_CODE,
       content = @Content(
                mediaType = "application/json", schema = @Schema(implementation = PostSignInResponseDto.class)))
    public ResponseEntity<? super PostSignInResponseDto> signInUser(HttpServletResponse res, @RequestBody @Valid SignInRequestDto dto) {
        return service.signInUser(res, dto);
    }

    @PostMapping("/owner/sign-in")
    @Operation(summary = "사장님 로그인 (김수찬)", description = "")
    @ApiResponse(responseCode = "200", description = "",
        content = @Content(
                mediaType = "application/json", schema = @Schema(implementation = PostOwnerSignInResponseDto.class)))
    public ResponseEntity<? super PostOwnerSignInResponseDto> signInOwner(HttpServletResponse res, @RequestBody @Valid OwnerSignInRequestDto dto) {
        return service.signInOwner(res, dto);
    }

    @PostMapping("/admin/sign-in")
    @Operation(summary = "관리자 로그인 (김수찬)", description = "")
    @ApiResponse(responseCode = "200", description = "",
        content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = PostAdminSignInResponseDto.class)))
    public ResponseEntity<? super PostAdminSignInResponseDto> signInAdmin(HttpServletResponse res, @RequestBody @Valid AdminSignInRequestDto dto) {
        return service.signInAdmin(res, dto);
    }

    @GetMapping("/access-token")
    @Operation(summary = "Access Token 불러오기 (김수찬)", description = ACCESS_TOKEN_DESCRIPTION)
    @ApiResponse(responseCode = "200",
        content = @Content(
                mediaType = "application/json", schema = @Schema(implementation = GetAccessTokenResponseDto.class)))
    public ResponseEntity<? super GetAccessTokenResponseDto> getAccessToken(HttpServletRequest req) {
        return service.getAccessToken(req);
    }

    @PostMapping("/sign-out")
    @Operation(summary = "로그아웃 처리 (김수찬)", description = "")
    @ApiResponse(responseCode = "200", description = "",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostSignOutResponseDto.class)))
    public ResponseEntity<? super PostSignOutResponseDto> signOut(HttpServletResponse res) {
        return service.signOut(res);
    }

    @PostMapping("/send-sms")
    @Operation(summary = "유저 휴대폰 인증 문자 보내기 (김수찬)", description = SEND_SMS_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = SEND_SMS_RESPONSE_ERROR_CODE,
        content = @Content(
                mediaType = "application/json", schema = @Schema(implementation = PostSmsSendResponseDto.class)))
    public ResponseEntity<? super PostSmsSendResponseDto> sendOne(@RequestParam String userPhone) {
        return service.sendOne(userPhone);
    }

    @PostMapping("/check-sms")
    @Operation(summary = "유저 휴대폰 인증코드 체크 (김수찬)", description = CHECK_SMS_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = CHECK_SMS_RESPONSE_ERROR_CODE,
        content = @Content(
                mediaType = "application/json", schema = @Schema(implementation = PostSmsCheckResponseDto.class)))
    public ResponseEntity<? super PostSmsCheckResponseDto> smsCheck(
            @RequestParam String userPhone, @RequestParam int phoneKey)
    {
        return service.checkPhone(userPhone, phoneKey);
    }

    @PostMapping("/mail-send")
    @Operation(summary = "유저 이메일 인증 보내기 (김수찬)", description = SEND_MAIL_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = SEND_MAIL_RESPONSE_ERROR_CODE,
        content = @Content(
                mediaType = "application/json", schema = @Schema(implementation = PostMailSendResponseDto.class)))
    public ResponseEntity<? super PostMailSendResponseDto> sendMail(@RequestParam String userEmail) {
        return service.sendAuthCode(userEmail);
    }

    @PostMapping("/mail-check")
    @Operation(summary = "유저 이메일 인증확인 (김수찬)", description = CHECK_MAIL_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = CHECK_MAIL_RESPONSE_ERROR_CODE,
        content = @Content(
                mediaType = "application/json", schema = @Schema(implementation = PostMailCheckResponseDto.class)))
    public ResponseEntity<? super PostMailCheckResponseDto> mailCheck(
            @RequestParam String userEmail, @RequestParam int emailKey)
    {
        return service.checkCode(userEmail, emailKey);
    }

    @PostMapping("/owner/send-sms")
    @Operation(summary = "사장님 휴대폰 인증 문자 보내기 (김수찬)", description = SEND_SMS_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = SEND_SMS_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PostSmsSendResponseDto.class)))
    public ResponseEntity<? super PostSmsSendResponseDto> sendOneOwner(@RequestParam String ownerPhone) {
        return service.sendOneOwner(ownerPhone);
    }

    @PostMapping("/owner/check-sms")
    @Operation(summary = "사장님 휴대폰 인증코드 체크 (김수찬)", description = CHECK_SMS_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = CHECK_SMS_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PostSmsCheckResponseDto.class)))
    public ResponseEntity<? super PostSmsCheckResponseDto> smsCheckOwner(
            @RequestParam String ownerPhone, @RequestParam int phoneKey)
    {
        return service.checkPhoneOwner(ownerPhone, phoneKey);
    }

    @PostMapping("/owner/mail-send")
    @Operation(summary = "사장님 이메일 인증 보내기 (김수찬)", description = SEND_MAIL_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = SEND_MAIL_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PostMailSendResponseDto.class)))
    public ResponseEntity<? super PostMailSendResponseDto> sendMailOwner(@RequestParam String ownerEmail) {
        return service.sendAuthCodeOwner(ownerEmail);
    }

    @PostMapping("/owner/mail-check")
    @Operation(summary = "사장님 이메일 인증확인 (김수찬)", description = CHECK_MAIL_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = CHECK_MAIL_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PostMailCheckResponseDto.class)))
    public ResponseEntity<? super PostMailCheckResponseDto> mailCheckOwner(
            @RequestParam String ownerEmail, @RequestParam int emailKey)
    {
        return service.checkCodeOwner(ownerEmail, emailKey);
    }

    @PostMapping("/search/email")
    @Operation(summary = "유저 이메일 찾기 (김수찬)")
    @ApiResponse(responseCode = "200", description = "",
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PostSearchEmailResponseDto.class)))
    public ResponseEntity<? super PostSearchEmailResponseDto> searchEmail(@RequestBody PostSearchEmailRequestDto dto) {
        return service.searchEmail(dto);
    }

    @PostMapping("/search/send-sms")
    @Operation(summary = "유저 이메일 찾기 - 휴대폰 인증 보내기 (김수찬)", description = SEND_SMS_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = SEND_SMS_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PostSmsSendResponseDto.class)))
    public ResponseEntity<? super PostSmsSendResponseDto> sendOneSearchEmail(@RequestParam String userPhone) {
        return service.sendOneSearchEmail(userPhone);
    }

    @PostMapping("/search/check-sms")
    @Operation(summary = "유저 이메일 찾기 - 휴대폰 인증코드 체크 (김수찬)", description = CHECK_SMS_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = CHECK_SMS_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PostSmsCheckResponseDto.class)))
    public ResponseEntity<? super PostSmsCheckResponseDto> smsCheckSearchEmail(
            @RequestParam String userPhone, @RequestParam int phoneKey)
    {
        return service.checkPhoneSearchEmail(userPhone, phoneKey);
    }

    @PostMapping("/search/password")
    @Operation(summary = "유저 비밀번호 찾기 (김수찬)")
    @ApiResponse(responseCode = "200", description = "",
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PostMailCheckResponseDto.class)))
    public ResponseEntity<? super PostSearchPwResponseDto> searchPw(@RequestBody PostSearchPwRequestDto dto) {
        return service.searchPw(dto);
    }

    @PostMapping("/search/mail-send")
    @Operation(summary = "유저 비밀번호 찾기 - 이메일 인증 보내기 (김수찬)", description = SEND_MAIL_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = SEND_MAIL_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PostMailSendResponseDto.class)))
    public ResponseEntity<? super PostMailSendResponseDto> sendMailSearchPw(@RequestParam String userEmail) {
        return service.sendMailSearchPw(userEmail);
    }

    @PostMapping("/search/mail-check")
    @Operation(summary = "유저 비밀번호 찾기 - 이메일 인증확인 (김수찬)", description = CHECK_MAIL_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = CHECK_MAIL_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PostMailCheckResponseDto.class)))
    public ResponseEntity<? super PostMailCheckResponseDto> mailCheckSearchPw(
            @RequestParam String userEmail, @RequestParam int emailKey)
    {
        return service.mailCheckSearchPw(userEmail, emailKey);
    }

    @PostMapping("/search/owner/email")
    @Operation(summary = "사장님 이메일 찾기 (김수찬)")
    @ApiResponse(responseCode = "200", description = "",
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PostSearchEmailResponseDto.class)))
    public ResponseEntity<? super PostSearchEmailResponseDto> searchOwnerEmail(@RequestBody PostSearchOwnerEmailRequestDto dto) {
        return service.searchOwnerEmail(dto);
    }

    @PostMapping("/search/owner/send-sms")
    @Operation(summary = "사장님 이메일 찾기 - 휴대폰 인증 보내기 (김수찬)", description = SEND_SMS_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = SEND_SMS_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PostSmsSendResponseDto.class)))
    public ResponseEntity<? super PostSmsSendResponseDto> sendOneSearchOwnerEmail(@RequestParam String ownerPhone) {
        return service.sendOneSearchOwnerEmail(ownerPhone);
    }

    @PostMapping("/search/owner/check-sms")
    @Operation(summary = "사장님 이메일 찾기 - 휴대폰 인증코드 체크 (김수찬)", description = CHECK_SMS_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = CHECK_SMS_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PostSmsCheckResponseDto.class)))
    public ResponseEntity<? super PostSmsCheckResponseDto> checkPhoneSearchOwnerEmail(
            @RequestParam String ownerPhone, @RequestParam int phoneKey)
    {
        return service.checkPhoneSearchOwnerEmail(ownerPhone, phoneKey);
    }

    @PostMapping("/search/owner/password")
    @Operation(summary = "사장님 비밀번호 찾기 (김수찬)")
    @ApiResponse(responseCode = "200", description = "",
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PostMailCheckResponseDto.class)))
    public ResponseEntity<? super PostSearchPwResponseDto> searchOwnerPw(@RequestBody PostSearchOwnerPwRequestDto dto) {
        return service.searchOwnerPw(dto);
    }

    @PostMapping("/search/owner/mail-send")
    @Operation(summary = "사장님 비밀번호 찾기 - 이메일 인증 보내기 (김수찬)", description = SEND_MAIL_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = SEND_MAIL_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PostMailSendResponseDto.class)))
    public ResponseEntity<? super PostMailSendResponseDto> sendMailSearchOwnerPw(@RequestParam String ownerEmail) {
        return service.sendMailSearchOwnerPw(ownerEmail);
    }

    @PostMapping("/search/owner/mail-check")
    @Operation(summary = "사장님 비밀번호 찾기 - 이메일 인증확인 (김수찬)", description = CHECK_MAIL_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = CHECK_MAIL_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PostMailCheckResponseDto.class)))
    public ResponseEntity<? super PostMailCheckResponseDto> mailCheckSearchOwnerPw(
            @RequestParam String ownerEmail, @RequestParam int emailKey)
    {
        return service.mailCheckSearchOwnerPw(ownerEmail, emailKey);
    }

}
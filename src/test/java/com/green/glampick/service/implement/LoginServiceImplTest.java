package com.green.glampick.service.implement;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import com.green.glampick.common.CustomFileUtils;
import com.green.glampick.common.Role;
import com.green.glampick.common.coolsms.SmsUtils;
import com.green.glampick.common.security.AppProperties;
import com.green.glampick.common.security.CookieUtils;
import com.green.glampick.dto.request.login.*;
import com.green.glampick.dto.response.login.*;
import com.green.glampick.dto.response.login.mail.PostMailCheckResponseDto;
import com.green.glampick.dto.response.login.mail.PostMailSendResponseDto;
import com.green.glampick.dto.response.login.sms.PostSmsCheckResponseDto;
import com.green.glampick.dto.response.login.sms.PostSmsSendResponseDto;
import com.green.glampick.dto.response.login.token.GetAccessTokenResponseDto;
import com.green.glampick.entity.AdminEntity;
import com.green.glampick.entity.OwnerEntity;
import com.green.glampick.exception.CustomException;
import com.green.glampick.exception.errorCode.CommonErrorCode;
import com.green.glampick.exception.errorCode.UserErrorCode;
import com.green.glampick.jwt.JwtTokenProvider;
import com.green.glampick.repository.AdminRepository;
import com.green.glampick.repository.OwnerRepository;
import com.green.glampick.repository.UserRepository;
import com.green.glampick.security.MyUser;
import com.green.glampick.security.MyUserDetail;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("tdd")
class LoginServiceImplTest {

    @InjectMocks
    private LoginServiceImpl loginService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private CookieUtils cookieUtils;

    @Mock
    private SmsUtils smsUtils;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private AppProperties appProperties;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginService.init(); // Initialize maps
    }

    @Test
    void testSignInOwner_Success() {
        OwnerSignInRequestDto dto = new OwnerSignInRequestDto();
        dto.setOwnerEmail("owner@example.com");
        dto.setOwnerPw("Test1234!");

        OwnerEntity ownerEntity = new OwnerEntity();
        ownerEntity.setOwnerId(1L);  // ownerId를 설정합니다.
        ownerEntity.setOwnerPw("encodedPassword");
        ownerEntity.setActivateStatus(1);
        ownerEntity.setRole(Role.ROLE_OWNER);

        AppProperties.Jwt jwtProperties = mock(AppProperties.Jwt.class);
        when(appProperties.getJwt()).thenReturn(jwtProperties);
        when(jwtProperties.getRefreshTokenCookieMaxAge()).thenReturn(3600);

        when(ownerRepository.findByOwnerEmail(dto.getOwnerEmail())).thenReturn(ownerEntity);
        when(passwordEncoder.matches(dto.getOwnerPw(), ownerEntity.getOwnerPw())).thenReturn(true);
        when(jwtTokenProvider.generateAccessToken(any(MyUser.class))).thenReturn("accessToken");
        when(jwtTokenProvider.generateRefreshToken(any(MyUser.class))).thenReturn("refreshToken");

        ResponseEntity<?> response = loginService.signInOwner(mock(HttpServletResponse.class), dto);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof PostOwnerSignInResponseDto);

        verify(ownerRepository, times(1)).findByOwnerEmail(dto.getOwnerEmail());
        verify(jwtTokenProvider, times(1)).generateAccessToken(any(MyUser.class));
    }

    @Test
    void testSignInOwner_InvalidCredentials() {
        OwnerSignInRequestDto dto = new OwnerSignInRequestDto();
        dto.setOwnerEmail("owner@example.com");
        dto.setOwnerPw("wrongPassword");

        OwnerEntity ownerEntity = new OwnerEntity();
        ownerEntity.setOwnerPw("encodedPassword");

        when(ownerRepository.findByOwnerEmail(dto.getOwnerEmail())).thenReturn(ownerEntity);
        when(passwordEncoder.matches(dto.getOwnerPw(), ownerEntity.getOwnerPw())).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> loginService.signInOwner(mock(HttpServletResponse.class), dto));

        assertEquals(CommonErrorCode.SF, exception.getErrorCode());

        verify(ownerRepository, times(1)).findByOwnerEmail(dto.getOwnerEmail());
    }

    @Test
    void testSignInOwner_ReservedOwner() {
        OwnerSignInRequestDto dto = new OwnerSignInRequestDto();
        dto.setOwnerEmail("owner@example.com");
        dto.setOwnerPw("Test1234!");

        OwnerEntity ownerEntity = new OwnerEntity();
        ownerEntity.setOwnerPw("encodedPassword");
        ownerEntity.setActivateStatus(1);
        ownerEntity.setRole(Role.ROLE_RESERVE_OWNER);

        when(ownerRepository.findByOwnerEmail(dto.getOwnerEmail())).thenReturn(ownerEntity);
        when(passwordEncoder.matches(dto.getOwnerPw(), ownerEntity.getOwnerPw())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> loginService.signInOwner(mock(HttpServletResponse.class), dto));

        assertEquals(CommonErrorCode.WS, exception.getErrorCode());
    }

    @Test
    void testSignInAdmin_Success() {
        AdminSignInRequestDto dto = new AdminSignInRequestDto();
        dto.setAdminId("adminId");
        dto.setAdminPw("Test1234!");

        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setAdminIdx(1L);
        adminEntity.setAdminPw("encodedPassword");

        AppProperties.Jwt jwtProperties = mock(AppProperties.Jwt.class);
        when(appProperties.getJwt()).thenReturn(jwtProperties);
        when(jwtProperties.getRefreshTokenCookieMaxAge()).thenReturn(3600);

        when(adminRepository.findByAdminId(dto.getAdminId())).thenReturn(adminEntity);
        when(passwordEncoder.matches(dto.getAdminPw(), adminEntity.getAdminPw())).thenReturn(true);
        when(jwtTokenProvider.generateAccessToken(any(MyUser.class))).thenReturn("accessToken");
        when(jwtTokenProvider.generateRefreshToken(any(MyUser.class))).thenReturn("refreshToken");

        ResponseEntity<?> response = loginService.signInAdmin(mock(HttpServletResponse.class), dto);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof PostAdminSignInResponseDto);

        verify(adminRepository, times(1)).findByAdminId(dto.getAdminId());
        verify(jwtTokenProvider, times(1)).generateAccessToken(any(MyUser.class));
    }

    @Test
    void testSignInAdmin_InvalidCredentials() {
        AdminSignInRequestDto dto = new AdminSignInRequestDto();
        dto.setAdminId("adminId");
        dto.setAdminPw("wrongPassword");

        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setAdminPw("encodedPassword");

        when(adminRepository.findByAdminId(dto.getAdminId())).thenReturn(adminEntity);
        when(passwordEncoder.matches(dto.getAdminPw(), adminEntity.getAdminPw())).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> loginService.signInAdmin(mock(HttpServletResponse.class), dto));

        assertEquals(CommonErrorCode.SF, exception.getErrorCode());

        verify(adminRepository, times(1)).findByAdminId(dto.getAdminId());
    }

    @Test
    void testSendAuthCode_Success() throws Exception {
        String userEmail = "test@example.com";

        when(userRepository.existsByUserEmail(userEmail)).thenReturn(false);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        ResponseEntity<?> response = loginService.sendAuthCode(userEmail);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof PostMailSendResponseDto);

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void testSendAuthCode_InvalidEmail() {
        String userEmail = "invalidEmail";

        CustomException exception = assertThrows(CustomException.class, () -> loginService.sendAuthCode(userEmail));

        assertEquals(UserErrorCode.IE, exception.getErrorCode());
    }

    @Test
    void testSendAuthCode_DuplicateEmail() {
        String userEmail = "test@example.com";

        when(userRepository.existsByUserEmail(userEmail)).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> loginService.sendAuthCode(userEmail));

        assertEquals(UserErrorCode.DE, exception.getErrorCode());
    }

    @Test
    void testCheckCode_Success() {
        String userEmail = "test@example.com";
        int emailKey = 123456;

        loginService.getCodeMap().put(userEmail, emailKey);
        loginService.getCodeExpiryMap().put(userEmail, System.currentTimeMillis() + 300000);

        ResponseEntity<?> response = loginService.checkCode(userEmail, emailKey);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof PostMailCheckResponseDto);
    }

    @Test
    void testCheckCode_InvalidCode() {
        String userEmail = "test@example.com";
        int emailKey = 123456;

        loginService.getCodeMap().put(userEmail, 654321);

        CustomException exception = assertThrows(CustomException.class, () -> loginService.checkCode(userEmail, emailKey));

        assertEquals(CommonErrorCode.IC, exception.getErrorCode());
    }

    @Test
    void testCheckCode_ExpiredCode() {
        String userEmail = "test@example.com";
        int emailKey = 123456;

        loginService.getCodeMap().put(userEmail, emailKey);
        loginService.getCodeExpiryMap().put(userEmail, System.currentTimeMillis() - 1);

        CustomException exception = assertThrows(CustomException.class, () -> loginService.checkCode(userEmail, emailKey));

        assertEquals(CommonErrorCode.EF, exception.getErrorCode());
    }

    @Test
    void testSendOneOwner_Success() {
        String ownerPhone = "01012345678";

        when(ownerRepository.existsByOwnerPhone(ownerPhone)).thenReturn(false);

        ResponseEntity<?> response = loginService.sendOneOwner(ownerPhone);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof PostSmsSendResponseDto);

        verify(smsUtils, times(1)).sendOne(eq(ownerPhone), anyInt());
    }

    @Test
    void testSendOneOwner_DuplicatePhone() {
        String ownerPhone = "01012345678";

        when(ownerRepository.existsByOwnerPhone(ownerPhone)).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> loginService.sendOneOwner(ownerPhone));

        assertEquals(UserErrorCode.DT, exception.getErrorCode());
    }

    @Test
    void testCheckPhoneOwner_Success() {
        String ownerPhone = "01012345678";
        int phoneKey = 123456;

        loginService.getCodeMap().put(ownerPhone, phoneKey);
        loginService.getCodeExpiryMap().put(ownerPhone, System.currentTimeMillis() + 300000);

        ResponseEntity<?> response = loginService.checkPhoneOwner(ownerPhone, phoneKey);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof PostSmsCheckResponseDto);
    }

    @Test
    void testCheckPhoneOwner_InvalidCode() {
        String ownerPhone = "01012345678";
        int phoneKey = 123456;

        loginService.getCodeMap().put(ownerPhone, 654321);

        CustomException exception = assertThrows(CustomException.class, () -> loginService.checkPhoneOwner(ownerPhone, phoneKey));

        assertEquals(CommonErrorCode.IC, exception.getErrorCode());
    }

    @Test
    void testCheckPhoneOwner_ExpiredCode() {
        String ownerPhone = "01012345678";
        int phoneKey = 123456;

        loginService.getCodeMap().put(ownerPhone, phoneKey);
        loginService.getCodeExpiryMap().put(ownerPhone, System.currentTimeMillis() - 1);

        CustomException exception = assertThrows(CustomException.class, () -> loginService.checkPhoneOwner(ownerPhone, phoneKey));

        assertEquals(CommonErrorCode.EF, exception.getErrorCode());
    }

    @Test
    void testSendAuthCodeOwner_Success() throws Exception {
        String ownerEmail = "owner@example.com";

        when(ownerRepository.existsByOwnerEmail(ownerEmail)).thenReturn(false);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        ResponseEntity<?> response = loginService.sendAuthCodeOwner(ownerEmail);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof PostMailSendResponseDto);

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void testSendAuthCodeOwner_InvalidEmail() {
        String ownerEmail = "invalidEmail";

        CustomException exception = assertThrows(CustomException.class, () -> loginService.sendAuthCodeOwner(ownerEmail));

        assertEquals(UserErrorCode.IE, exception.getErrorCode());
    }

    @Test
    void testSendAuthCodeOwner_DuplicateEmail() {
        String ownerEmail = "owner@example.com";

        when(ownerRepository.existsByOwnerEmail(ownerEmail)).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> loginService.sendAuthCodeOwner(ownerEmail));

        assertEquals(UserErrorCode.DE, exception.getErrorCode());
    }

    @Test
    void testCheckCodeOwner_Success() {
        String ownerEmail = "owner@example.com";
        int emailKey = 123456;

        loginService.getCodeMap().put(ownerEmail, emailKey);
        loginService.getCodeExpiryMap().put(ownerEmail, System.currentTimeMillis() + 300000);

        ResponseEntity<?> response = loginService.checkCodeOwner(ownerEmail, emailKey);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof PostMailCheckResponseDto);
    }

    @Test
    void testCheckCodeOwner_InvalidCode() {
        String ownerEmail = "owner@example.com";
        int emailKey = 123456;

        loginService.getCodeMap().put(ownerEmail, 654321);

        CustomException exception = assertThrows(CustomException.class, () -> loginService.checkCodeOwner(ownerEmail, emailKey));

        assertEquals(CommonErrorCode.IC, exception.getErrorCode());
    }

    @Test
    void testCheckCodeOwner_ExpiredCode() {
        String ownerEmail = "owner@example.com";
        int emailKey = 123456;

        loginService.getCodeMap().put(ownerEmail, emailKey);
        loginService.getCodeExpiryMap().put(ownerEmail, System.currentTimeMillis() - 1);

        CustomException exception = assertThrows(CustomException.class, () -> loginService.checkCodeOwner(ownerEmail, emailKey));

        assertEquals(CommonErrorCode.EF, exception.getErrorCode());
    }

    // Test for getAccessToken method
    @Test
    void testGetAccessToken_Success() {
        String refreshToken = "validRefreshToken";
        Cookie cookie = new Cookie("refresh-token", refreshToken);

        HttpServletRequest req = mock(HttpServletRequest.class);
        when(cookieUtils.getCookie(req, "refresh-token")).thenReturn(cookie);
        when(jwtTokenProvider.isValidateToken(refreshToken)).thenReturn(true);
        UserDetails userDetails = mock(MyUserDetail.class);
        when(jwtTokenProvider.getUserDetailsFromToken(refreshToken)).thenReturn(userDetails);
        when(((MyUserDetail) userDetails).getMyUser()).thenReturn(MyUser.builder().userId(1L).role(Role.ROLE_USER).build());
        when(jwtTokenProvider.generateAccessToken(any(MyUser.class))).thenReturn("newAccessToken");

        ResponseEntity<?> response = loginService.getAccessToken(req);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof GetAccessTokenResponseDto);
    }

    @Test
    void testGetAccessToken_InvalidToken() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(cookieUtils.getCookie(req, "refresh-token")).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> loginService.getAccessToken(req));

        assertNotNull(exception);
    }

    @Test
    void testGetAccessToken_ExpiredToken() {
        String refreshToken = "expiredRefreshToken";
        Cookie cookie = new Cookie("refresh-token", refreshToken);

        HttpServletRequest req = mock(HttpServletRequest.class);
        when(cookieUtils.getCookie(req, "refresh-token")).thenReturn(cookie);
        when(jwtTokenProvider.isValidateToken(refreshToken)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> loginService.getAccessToken(req));

        assertNotNull(exception);
    }

}
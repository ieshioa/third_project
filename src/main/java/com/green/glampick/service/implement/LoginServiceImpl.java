package com.green.glampick.service.implement;

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
import com.green.glampick.dto.response.login.social.PostSnsSignUpResponseDto;
import com.green.glampick.dto.response.login.token.GetAccessTokenResponseDto;
import com.green.glampick.entity.AdminEntity;
import com.green.glampick.entity.OwnerEntity;
import com.green.glampick.entity.UserEntity;
import com.green.glampick.exception.CustomException;
import com.green.glampick.exception.errorCode.CommonErrorCode;
import com.green.glampick.exception.errorCode.UserErrorCode;
import com.green.glampick.jwt.JwtTokenProvider;
import com.green.glampick.repository.AdminRepository;
import com.green.glampick.repository.OwnerRepository;
import com.green.glampick.repository.UserRepository;
import com.green.glampick.security.MyUser;
import com.green.glampick.security.MyUserDetail;
import com.green.glampick.security.SignInProviderType;
import com.green.glampick.service.LoginService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomFileUtils customFileUtils;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieUtils cookieUtils;
    private final AppProperties appProperties;
    private final SmsUtils smsUtils;

    @Getter
    private Map<String, Integer> CodeMap;
    @Getter
    private Map<String, Long> CodeExpiryMap;

    private final JavaMailSender mailSender;

    //  최초 실행 시, 초기화를 한번만 진행  //
    @PostConstruct
    @Override
    public void init() {
        CodeMap = new ConcurrentHashMap<>();
        CodeExpiryMap = new ConcurrentHashMap<>();
    }

    //  6자리의 랜덤 숫자코드를 생성  //
    @Override
    public int createKey() {
        Random random = new Random();
        StringBuilder keyBuilder = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            keyBuilder.append(random.nextInt(10));
        }

        return Integer.parseInt(keyBuilder.toString());
    }

    //  1분 마다 스케줄이 실행되는 메소드  //
    @Scheduled(fixedRate = 60000)
    public void cleanUpExpiredCodes() {
        long now = System.currentTimeMillis();
        CodeExpiryMap.entrySet().removeIf(entry -> now > entry.getValue());
    }

    //  로그인 및 회원가입 페이지 - 이메일 회원가입 처리  //
    @Override
    @Transactional
    public ResponseEntity<? super PostSignUpResponseDto> signUpUser(SignUpRequestDto dto) {

        try {
            if (dto.getUserEmail() == null || dto.getUserEmail().isEmpty()) {
                throw new CustomException(CommonErrorCode.VF);
            }
            if (dto.getUserPw() == null || dto.getUserPw().isEmpty()) {
                throw new CustomException(CommonErrorCode.VF);
            }
            if (dto.getUserPhone() == null || dto.getUserPhone().isEmpty()) {
                throw new CustomException(CommonErrorCode.VF);
            }
            if (dto.getUserName() == null || dto.getUserName().isEmpty()) {
                throw new CustomException(CommonErrorCode.VF);
            }
            if (dto.getUserNickname() == null || dto.getUserNickname().isEmpty()) {
                throw new CustomException(CommonErrorCode.VF);
            }

            String userEmail = dto.getUserEmail();
            String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
            Pattern patternEmail = Pattern.compile(emailRegex);
            Matcher matcherEmail = patternEmail.matcher(userEmail);
            if (!matcherEmail.matches()) { throw new CustomException(UserErrorCode.IE); }

            String userNickname = dto.getUserNickname();
            String nicknameRegex = "^[a-zA-Z가-힣][a-zA-Z0-9가-힣]{2,10}$";
            Pattern patternNickname = Pattern.compile(nicknameRegex);
            Matcher matcherNickname = patternNickname.matcher(userNickname);
            if (!matcherNickname.matches()) { throw new CustomException(UserErrorCode.IN); }

            boolean existedNickname = userRepository.existsByUserNickname(userNickname);
            if (existedNickname) { throw new CustomException(UserErrorCode.DN); }

            String userPhone = dto.getUserPhone();
            String phoneRegex = "^(01[016789]-?\\d{3,4}-?\\d{4})|(0[2-9][0-9]-?\\d{3,4}-?\\d{4})$";
            Pattern patternPhone = Pattern.compile(phoneRegex);
            Matcher matcherPhone = patternPhone.matcher(userPhone);
            if (!matcherPhone.matches()) { throw new CustomException(UserErrorCode.IPH); }
            String userPhoneReplace = userPhone.replaceAll("-", "");
            dto.setUserPhone(userPhoneReplace);

            String userPw = dto.getUserPw();
            String passwordRegex = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
            Pattern patternPw = Pattern.compile(passwordRegex);
            Matcher matcherPw = patternPw.matcher(userPw);
            if (!matcherPw.matches()) { throw new CustomException(UserErrorCode.IP); }

            String encodingPw = passwordEncoder.encode(userPw);
            dto.setUserPw(encodingPw);

            dto.setUserRole(Role.ROLE_USER);
            dto.setUserSocialType(SignInProviderType.LOCAL);

            UserEntity userEntity = new UserEntity();
            userEntity.setUserEmail(dto.getUserEmail());
            userEntity.setUserPw(dto.getUserPw());
            userEntity.setUserName(dto.getUserName());
            userEntity.setUserNickname(dto.getUserNickname());
            userEntity.setUserPhone(dto.getUserPhone());
            userEntity.setRole(dto.getUserRole());
            userEntity.setActivateStatus(1);
            userEntity.setUserSocialType(dto.getUserSocialType());
            UserEntity savedUser = userRepository.save(userEntity);


            return PostSignUpResponseDto.success(savedUser.getUserId());

        } catch (CustomException e) {
            e.printStackTrace();
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

    }

    //  로그인 및 회원가입 페이지 - 소셜 회원가입 처리  //
    @Override
    @Transactional
    public ResponseEntity<? super PostSnsSignUpResponseDto> signUpSnsUser(SignUpSnsRequestDto dto) {

        try {

            if (dto.getUserPhone() == null || dto.getUserPhone().isEmpty()) {
                throw new CustomException(CommonErrorCode.VF);
            }
            if (dto.getUserName() == null || dto.getUserName().isEmpty()) {
                throw new CustomException(CommonErrorCode.VF);
            }
            if (dto.getUserNickname() == null || dto.getUserNickname().isEmpty()) {
                throw new CustomException(CommonErrorCode.VF);
            }

            String userPw = dto.getUserPw();
            String passwordRegex = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
            Pattern patternPw = Pattern.compile(passwordRegex);
            Matcher matcherPw = patternPw.matcher(userPw);
            if (!matcherPw.matches()) { throw new CustomException(UserErrorCode.IP); }
            String encodingPw = passwordEncoder.encode(userPw);
            dto.setUserPw(encodingPw);

            String userNickname = dto.getUserNickname();
            String nicknameRegex = "^[a-zA-Z가-힣][a-zA-Z0-9가-힣]{2,10}$";
            Pattern patternNickname = Pattern.compile(nicknameRegex);
            Matcher matcherNickname = patternNickname.matcher(userNickname);
            if (!matcherNickname.matches()) { throw new CustomException(UserErrorCode.IN); }

            boolean existedNickname = userRepository.existsByUserNickname(userNickname);
            if (existedNickname) { throw new CustomException(UserErrorCode.DN); }

            String userPhone = dto.getUserPhone();
            String phoneRegex = "^(01[016789]-?\\d{3,4}-?\\d{4})|(0[2-9][0-9]-?\\d{3,4}-?\\d{4})$";
            Pattern patternPhone = Pattern.compile(phoneRegex);
            Matcher matcherPhone = patternPhone.matcher(userPhone);
            if (!matcherPhone.matches()) { throw new CustomException(UserErrorCode.IPH); }
            String userPhoneReplace = userPhone.replaceAll("-", "");
            dto.setUserPhone(userPhoneReplace);

            UserEntity userEntity = userRepository.findByUserId(dto.getUserId());
            userEntity.setUserPw(dto.getUserPw());
            userEntity.setUserName(dto.getUserName());
            userEntity.setUserNickname(dto.getUserNickname());
            userEntity.setUserPhone(dto.getUserPhone());
            userEntity.setActivateStatus(1);
            UserEntity savedUser = userRepository.save(userEntity);

            return PostSnsSignUpResponseDto.success(savedUser.getUserId());

        } catch (CustomException e) {
            e.printStackTrace();
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

    }

    //  로그인 및 회원가입 페이지 - 사장님 회원가입 처리  //
    @Override
    @Transactional
    public ResponseEntity<? super PostOwnerSignUpResponseDto> signUpOwner(MultipartFile file, OwnerSignUpRequestDto dto) {

        try {
            //  입력받은 값이 없다면, 유효성 검사에 대한 응답을 보낸다.  //
            if (dto.getOwnerEmail() == null || dto.getOwnerEmail().isEmpty()) {
                throw new CustomException(CommonErrorCode.VF);
            }
            if (dto.getOwnerPw() == null || dto.getOwnerPw().isEmpty()) {
                throw new CustomException(CommonErrorCode.VF);
            }
            if (dto.getOwnerPhone() == null || dto.getOwnerPhone().isEmpty()) {
                throw new CustomException(CommonErrorCode.VF);
            }
            if (dto.getOwnerName() == null || dto.getOwnerName().isEmpty()) {
                throw new CustomException(CommonErrorCode.VF);
            }
            if (dto.getBusinessNumber() == null || dto.getBusinessNumber().isEmpty()) {
                throw new CustomException(CommonErrorCode.VF);
            }

            //  입력받은 이메일이 정규표현식을 통하여 이메일 형식에 맞지 않으면, 이메일 형식 오류에 대한 응답을 보낸다.  //
            String ownerEmail = dto.getOwnerEmail();
            String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
            Pattern patternEmail = Pattern.compile(emailRegex);
            Matcher matcherEmail = patternEmail.matcher(ownerEmail);
            if (!matcherEmail.matches()) {
                throw new CustomException(UserErrorCode.IE);
            }


            //  입력받은 전화번호가 정규표현식을 통하여 전화번호 형식에 맞지 않으면, 전화번호 형식 오류에 대한 응답을 보낸다.  //
            String ownerPhone = dto.getOwnerPhone();
            String phoneRegex = "^(01[016789]-?\\d{3,4}-?\\d{4})|(0[2-9][0-9]-?\\d{3,4}-?\\d{4})$";
            Pattern patternPhone = Pattern.compile(phoneRegex);
            Matcher matcherPhone = patternPhone.matcher(ownerPhone);
            if (!matcherPhone.matches()) {
                throw new CustomException(UserErrorCode.IPH);
            }
            String ownerPhoneReplace = ownerPhone.replaceAll("-", "");
            dto.setOwnerPhone(ownerPhoneReplace);


            //  입력받은 비밀번호가 정규표현식을 통하여 비밀번호 형식에 맞지 않으면, 비밀번호 형식 오류에 대한 응답을 보낸다.  //
            String userPw = dto.getOwnerPw();
            String passwordRegex = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
            Pattern patternPw = Pattern.compile(passwordRegex);
            Matcher matcherPw = patternPw.matcher(userPw);
            if (!matcherPw.matches()) {
                throw new CustomException(UserErrorCode.IP);
            }
            //  입력받은 DTO 에서 패스워드를 암호화 하여 다시 DTO 값에 넣는다.  //
            String encodingPw = passwordEncoder.encode(userPw);
            dto.setOwnerPw(encodingPw);

            dto.setRole(Role.ROLE_RESERVE_OWNER);

            //  가공이 끝난 DTO 를 새로운 userEntity 객체로 생성한다.  //
            OwnerEntity ownerEntity = new OwnerEntity();
            ownerEntity.setBusinessNumber(dto.getBusinessNumber());
            ownerEntity.setOwnerEmail(dto.getOwnerEmail());
            ownerEntity.setOwnerPw(dto.getOwnerPw());
            ownerEntity.setOwnerName(dto.getOwnerName());
            ownerEntity.setOwnerPhone(dto.getOwnerPhone());
            ownerEntity.setRole(dto.getRole());
            ownerEntity.setActivateStatus(1);
            ownerEntity.setGlampingStatus(0);

            //  바로 위에서 만든 객체를 JPA 를 통해서 DB에 저장한다.  //
            OwnerEntity savedUser = ownerRepository.save(ownerEntity);

            String makeFolder = String.format("businessInfo/%d", savedUser.getOwnerId());
            customFileUtils.makeFolders(makeFolder);
            String saveFileName = customFileUtils.makeRandomFileName(file);
            String saveDbFileName = String.format("/pic/businessInfo/%d/%s", savedUser.getOwnerId(), saveFileName);
            String filePath = String.format("%s/%s", makeFolder, saveFileName);
            customFileUtils.transferTo(file, filePath);

            ownerEntity = ownerRepository.findByOwnerId(savedUser.getOwnerId());
            ownerEntity.setBusinessPaperImage(saveDbFileName);
            ownerRepository.save(ownerEntity);

            return PostOwnerSignUpResponseDto.success(savedUser.getOwnerId());

        } catch (CustomException e) {
            e.printStackTrace();
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

    }

    //  로그인 및 회원가입 페이지 - 이메일 로그인 처리  //
    @Override
    @Transactional
    public ResponseEntity<? super PostSignInResponseDto> signInUser(HttpServletResponse res, SignInRequestDto dto) {

        String accessToken = null;
        String refreshToken = null;

        try {

            //  입력받은 값이 없다면, 유효성 검사에 대한 응답을 보낸다.  //
            if (dto.getUserEmail() == null || dto.getUserEmail().isEmpty()) {
                throw new CustomException(CommonErrorCode.VF);
            }
            if (dto.getUserPw() == null || dto.getUserPw().isEmpty()) {
                throw new CustomException(CommonErrorCode.VF);
            }

            //  입력받은 이메일이 유저 테이블에 없다면, 로그인 실패에 대한 응답을 보낸다.  //
            String userEmail = dto.getUserEmail();
            UserEntity userEntity = userRepository.findByUserEmail(userEmail);
            if (userEntity == null) {
                throw new CustomException(CommonErrorCode.SF);
            }

            //  입력받은 비밀번호와 유저 테이블에 있는 비밀번호가 같은지 확인하고, 다르다면 로그인 실패에 대한 응답을 보낸다.  //
            String userPw = dto.getUserPw();
            String encodingPw = userEntity.getUserPw();
            boolean matches = passwordEncoder.matches(userPw, encodingPw);
            if (!matches) {
                throw new CustomException(CommonErrorCode.SF);
            }

            if (userEntity.getActivateStatus() != 1) {
                throw new CustomException(CommonErrorCode.NS);
            }

            //  로그인에 성공할 경우, myUser 에 로그인한 userId 값을 넣고, 권한을 넣는다.  //
            MyUser myUser = MyUser.builder()
                    .userId(userEntity.getUserId())
                    .role(userEntity.getRole())
                    .build();

            //  myUser 에 넣은 데이터를 통해, AccessToken, RefreshToken 을 만든다.  //
            accessToken = jwtTokenProvider.generateAccessToken(myUser);
            refreshToken = jwtTokenProvider.generateRefreshToken(myUser);

            //  RefreshToken 을 갱신한다.  //
            int refreshTokenMaxAge = appProperties.getJwt().getRefreshTokenCookieMaxAge();
            cookieUtils.deleteCookie(res, "refresh-token");
            cookieUtils.setCookie(res, "refresh-token", refreshToken, refreshTokenMaxAge);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return PostSignInResponseDto.success(accessToken);

    }

    //  로그인 및 회원가입 페이지 - 사장님 로그인 처리  //
    @Override
    @Transactional
    public ResponseEntity<? super PostOwnerSignInResponseDto> signInOwner(HttpServletResponse res, OwnerSignInRequestDto dto) {

        String accessToken = null;
        String refreshToken = null;

        try {

            //  입력받은 값이 없다면, 유효성 검사에 대한 응답을 보낸다.  //
            if (dto.getOwnerEmail() == null || dto.getOwnerEmail().isEmpty()) {
                throw new CustomException(CommonErrorCode.VF);
            }
            if (dto.getOwnerPw() == null || dto.getOwnerPw().isEmpty()) {
                throw new CustomException(CommonErrorCode.VF);
            }

            //  입력받은 이메일이 유저 테이블에 없다면, 로그인 실패에 대한 응답을 보낸다.  //
            String ownerEmail = dto.getOwnerEmail();
            OwnerEntity ownerEntity = ownerRepository.findByOwnerEmail(ownerEmail);
            if (ownerEntity == null) {
                throw new CustomException(CommonErrorCode.SF);
            }

            //  입력받은 비밀번호와 유저 테이블에 있는 비밀번호가 같은지 확인하고, 다르다면 로그인 실패에 대한 응답을 보낸다.  //
            String ownerPw = dto.getOwnerPw();
            String encodingPw = ownerEntity.getOwnerPw();
            boolean matches = passwordEncoder.matches(ownerPw, encodingPw);
            if (!matches) {
                throw new CustomException(CommonErrorCode.SF);
            }

            if (ownerEntity.getActivateStatus() == -1) {
                throw new CustomException(CommonErrorCode.NS);
            }

            if (ownerEntity.getActivateStatus() == 0) {
                throw new CustomException(CommonErrorCode.WO);
            }

            if (ownerEntity.getRole() == Role.ROLE_RESERVE_OWNER) {
                throw new CustomException(CommonErrorCode.WS);
            }

            //  로그인에 성공할 경우, myUser 에 로그인한 userId 값을 넣고, 권한을 넣는다.  //
            MyUser myUser = MyUser.builder()
                    .userId(ownerEntity.getOwnerId())
                    .role(ownerEntity.getRole())
                    .build();

            //  myUser 에 넣은 데이터를 통해, AccessToken, RefreshToken 을 만든다.  //
            accessToken = jwtTokenProvider.generateAccessToken(myUser);
            refreshToken = jwtTokenProvider.generateRefreshToken(myUser);

            //  RefreshToken 을 갱신한다.  //
            int refreshTokenMaxAge = appProperties.getJwt().getRefreshTokenCookieMaxAge();
            cookieUtils.deleteCookie(res, "refresh-token");
            cookieUtils.setCookie(res, "refresh-token", refreshToken, refreshTokenMaxAge);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return PostOwnerSignInResponseDto.success(accessToken);

    }

    //  로그인 및 회원가입 페이지 - 관리자 로그인 처리  //
    @Override
    @Transactional
    public ResponseEntity<? super PostAdminSignInResponseDto> signInAdmin(HttpServletResponse res, AdminSignInRequestDto dto) {

        String accessToken = null;
        String refreshToken = null;

        try {

            //  입력받은 값이 없다면, 유효성 검사에 대한 응답을 보낸다.  //
            if (dto.getAdminId() == null || dto.getAdminId().isEmpty()) {
                throw new CustomException(CommonErrorCode.VF);
            }
            if (dto.getAdminPw() == null || dto.getAdminPw().isEmpty()) {
                throw new CustomException(CommonErrorCode.VF);
            }

            //  입력받은 이메일이 유저 테이블에 없다면, 로그인 실패에 대한 응답을 보낸다.  //
            String adminId = dto.getAdminId();
            AdminEntity adminEntity = adminRepository.findByAdminId(adminId);
            if (adminEntity == null) {
                throw new CustomException(CommonErrorCode.SF);
            }

            //  입력받은 비밀번호와 유저 테이블에 있는 비밀번호가 같은지 확인하고, 다르다면 로그인 실패에 대한 응답을 보낸다.  //
            String adminPw = dto.getAdminPw();
            String encodingPw = adminEntity.getAdminPw();
            boolean matches = passwordEncoder.matches(adminPw, encodingPw);
            if (!matches) {
                throw new CustomException(CommonErrorCode.SF);
            }

            //  로그인에 성공할 경우, myUser 에 로그인한 userId 값을 넣고, 권한을 넣는다.  //
            MyUser myUser = MyUser.builder()
                    .userId(adminEntity.getAdminIdx())
                    .role(adminEntity.getRole())
                    .build();

            //  myUser 에 넣은 데이터를 통해, AccessToken, RefreshToken 을 만든다.  //
            accessToken = jwtTokenProvider.generateAccessToken(myUser);
            refreshToken = jwtTokenProvider.generateRefreshToken(myUser);

            //  RefreshToken 을 갱신한다.  //
            int refreshTokenMaxAge = appProperties.getJwt().getRefreshTokenCookieMaxAge();
            cookieUtils.deleteCookie(res, "refresh-token");
            cookieUtils.setCookie(res, "refresh-token", refreshToken, refreshTokenMaxAge);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return PostAdminSignInResponseDto.success(accessToken);

    }

    //  AccessToken 불러오기  //
    @Override
    @Transactional
    public ResponseEntity<? super GetAccessTokenResponseDto> getAccessToken(HttpServletRequest req) {

        try {

            //  req 에서 "refresh-token" 이름의 쿠키를 가져와서 cookie 에 저장하며, cookie 가 없다면 예외를 발생한다.  //
            Cookie cookie = cookieUtils.getCookie(req, "refresh-token");
            if (cookie == null) {
                throw new RuntimeException();
            }

            //  RefreshToken 이 유효한지 확인하고, 유효하지 않다면 예외를 발생한다.  //
            String refreshToken = cookie.getValue();
            if (!jwtTokenProvider.isValidateToken(refreshToken)) {
                throw new RuntimeException();
            }

            //  RefreshToken 에서 사용자의 세부 정보를 가져오고, 해당 정보를 통해서 MyUser 객체를 가져온다.  //
            UserDetails auth = jwtTokenProvider.getUserDetailsFromToken(refreshToken);
            MyUser myUser = ((MyUserDetail) auth).getMyUser();

            //  위에서 가져온 MyUser 정보가 담긴 AccessToken 을 가져온다.  //
            String accessToken = jwtTokenProvider.generateAccessToken(myUser);

            //  Map 객체에 AccessToken 을 추가한다. (키는 AccessToken, 값은 accessToken)  //
            Map map = new HashMap();
            map.put("accessToken", accessToken);

            return GetAccessTokenResponseDto.success(map);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

    }

    //  로그아웃 처리하기  //
    @Override
    public ResponseEntity<? super PostSignOutResponseDto> signOut(HttpServletResponse res) {
        cookieUtils.deleteCookie(res, "refresh-token");

        return PostSignOutResponseDto.success();
    }

    //  로그인 및 회원가입 페이지 - 유저 휴대폰 인증 문자 보내기  //
    @Override
    @Transactional
    public ResponseEntity<? super PostSmsSendResponseDto> sendOne(String userPhone) {

        int verificationCode;

        try {

            String phoneRegex = "^(01[016789]-?\\d{3,4}-?\\d{4})|(0[2-9][0-9]-?\\d{3,4}-?\\d{4})$";
            Pattern patternPhone = Pattern.compile(phoneRegex);
            Matcher matcherPhone = patternPhone.matcher(userPhone);
            if (!matcherPhone.matches()) {
                throw new CustomException(UserErrorCode.IPH);
            }
            boolean existedPhone = userRepository.existsByUserPhone(userPhone);
            if (existedPhone) {
                throw new CustomException(UserErrorCode.DT);
            }
            //  받아온 유저 휴대폰 번호의 "-" 부분을 없앤다. (010-1234-5678 -> 01012345678)  //
            userPhone.replaceAll("-", "");

            //  변수에 랜덤으로 생성되는 6자리의 숫자를 넣는다.  //
            verificationCode = createKey();

            //  Map 객체에 유저 휴대폰 번호와 위에서 생성한 코드를 추가하고, 유효시간은 5분으로 지정한다. (5분뒤 삭제) //
            CodeMap.put(userPhone, verificationCode);
            CodeExpiryMap.put(userPhone, System.currentTimeMillis() + 300000);

            //  Cool SMS 를 통하여, 받아온 유저 휴대폰 번호에 코드를 보낸다.  //
            smsUtils.sendOne(userPhone, verificationCode);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return PostSmsSendResponseDto.success();

    }

    //  로그인 및 회원가입 페이지 - 유저 휴대폰 인증코드 체크하기  //
    @Override
    @Transactional
    public ResponseEntity<? super PostSmsCheckResponseDto> checkPhone(String userPhone, int phoneKey) {

        try {
            //  이메일과 인증코드가 Map 에 저장되어 있는 인증코드와 같다면  //
            if (CodeMap.containsKey(userPhone) && CodeMap.get(userPhone).equals(phoneKey)) {

                //  Map 에 저장되어 있는 인증코드의 유효시간이 지났다면  //
                if (System.currentTimeMillis() > CodeExpiryMap.get(userPhone)) {

                    //  Map 에 저장되어 있는 정보를 삭제하고, 유효시간이 만료된 응답을 보낸다.  //
                    CodeMap.remove(userPhone);
                    CodeExpiryMap.remove(userPhone);
                    throw new CustomException(CommonErrorCode.EF);

                }

                //  인증 성공 시, Map 에 저장되어 있는 코드와 유효시간을 삭제한다.  //
                CodeMap.remove(userPhone);
                CodeExpiryMap.remove(userPhone);

                return PostSmsCheckResponseDto.success();

            } else {

                //  인증코드가 틀리다면 틀린 인증번호에 대한 응답을 보낸다.  //
                throw new CustomException(CommonErrorCode.IC);

            }

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

    }

    //  로그인 및 회원가입 페이지 - 유저 이메일 인증 보내기  //
    @Override
    @Transactional
    public ResponseEntity<? super PostMailSendResponseDto> sendAuthCode(String userEmail) {

        try {

            //  입력받은 이메일이 비어있는 값이면, 빈 값에 대한 응답을 보낸다.  //
            if (userEmail == null || userEmail.isEmpty()) {
                throw new CustomException(UserErrorCode.EE);
            }

            //  입력받은 이메일이 정규표현식을 통하여 이메일 형식에 맞지 않으면, 이메일 형식 오류에 대한 응답을 보낸다.  //
            String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
            Pattern patternEmail = Pattern.compile(emailRegex);
            Matcher matcherEmail = patternEmail.matcher(userEmail);
            if (!matcherEmail.matches()) {
                throw new CustomException(UserErrorCode.IE);
            }

            //  입력받은 이메일이 유저 테이블에 이미 있는 이메일 이라면, 중복 이메일에 대한 응답을 보낸다.  //
            boolean existedEmail = userRepository.existsByUserEmail(userEmail);
            if (existedEmail) {
                throw new CustomException(UserErrorCode.DE);
            }

            //  변수에 랜덤으로 생성되는 6자리의 숫자를 넣는다.  //
            int mailCode = createKey();

            //  Map 객체에 유저 이메일과 위에서 생성한 코드를 추가하고, 유효시간은 5분으로 지정한다. (5분뒤 삭제) //
            CodeMap.put(userEmail, mailCode);
            CodeExpiryMap.put(userEmail, System.currentTimeMillis() + 300000);

            //  MimeMessage 객체를 만든다.  //
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            //  MimeMessage 에 받아온 유저 이메일과, Text, Code 에 대한 값을 넣는다.  //
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(userEmail);
            helper.setSubject("글램픽 인증 코드");

            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<style>" +
                    "@import url('https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css');" +
                    "body {font-family: 'Pretendard-Regular', sans-serif;}" +
                    ".container {padding: 20px; text-align: center;}" +
                    ".message {font-size: 16px; color: #34495e; margin-top: 20px;}" +
                    ".code {font-size: 24px; font-weight: bold; color: #2c3e50; margin-top: 10px;}" +
                    ".highlight {color: #355179;}" + // 이미지 색상과 조화로운 색상
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<img src='cid:mailImage' alt='메일 이미지' class='background-image'>" +
                    "<p class='message'>안녕하세요.</p>" +
                    "<p class='message'>글램픽 인증 코드는 다음과 같습니다.</p>" +
                    "<p class='code highlight'>" + mailCode + "</p>" +
                    "<p class='message'>글램픽을 이용해 주셔서 감사합니다 !</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";
            helper.setText(htmlContent, true);
            helper.addInline("mailImage", new ClassPathResource("mailImage/main-big.png"));

            //  위에서 정의한 MimeMessage 를 전송한다.  //
            mailSender.send(mimeMessage);

            return PostMailSendResponseDto.success();

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
    }

    //  로그인 및 회원가입 페이지 - 유저 이메일 코드 체크하기  //
    @Override
    @Transactional
    public ResponseEntity<? super PostMailCheckResponseDto> checkCode(String userEmail, int emailKey) {

        try {
            //  이메일과 인증코드가 Map 에 저장되어 있는 인증코드와 같다면  //
            if (CodeMap.containsKey(userEmail) && CodeMap.get(userEmail).equals(emailKey)) {

                //  Map 에 저장되어 있는 인증코드의 유효시간이 지났다면  //
                if (System.currentTimeMillis() > CodeExpiryMap.get(userEmail)) {

                    //  Map 에 저장되어 있는 정보를 삭제하고, 유효시간이 만료된 응답을 보낸다.  //
                    CodeMap.remove(userEmail);
                    CodeExpiryMap.remove(userEmail);
                    throw new CustomException(CommonErrorCode.EF);

                }

                //  인증 성공 시, Map 에 저장되어 있는 코드와 유효시간을 삭제한다.  //
                CodeMap.remove(userEmail);
                CodeExpiryMap.remove(userEmail);

                return PostMailCheckResponseDto.success();

            } else {

                //  인증코드가 틀리다면 틀린 인증번호에 대한 응답을 보낸다.  //
                throw new CustomException(CommonErrorCode.IC);

            }

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

    }

    //  로그인 및 회원가입 페이지 - 사장님 휴대폰 인증 문자 보내기  //
    @Override
    @Transactional
    public ResponseEntity<? super PostSmsSendResponseDto> sendOneOwner(String ownerPhone) {

        int verificationCode;

        try {

            String phoneRegex = "^(01[016789]-?\\d{3,4}-?\\d{4})|(0[2-9][0-9]-?\\d{3,4}-?\\d{4})$";
            Pattern patternPhone = Pattern.compile(phoneRegex);
            Matcher matcherPhone = patternPhone.matcher(ownerPhone);
            if (!matcherPhone.matches()) {
                throw new CustomException(UserErrorCode.IPH);
            }
            boolean existedPhone = ownerRepository.existsByOwnerPhone(ownerPhone);
            if (existedPhone) {
                throw new CustomException(UserErrorCode.DT);
            }
            //  받아온 유저 휴대폰 번호의 "-" 부분을 없앤다. (010-1234-5678 -> 01012345678)  //
            ownerPhone.replaceAll("-", "");

            //  변수에 랜덤으로 생성되는 6자리의 숫자를 넣는다.  //
            verificationCode = createKey();

            //  Map 객체에 유저 휴대폰 번호와 위에서 생성한 코드를 추가하고, 유효시간은 5분으로 지정한다. (5분뒤 삭제) //
            CodeMap.put(ownerPhone, verificationCode);
            CodeExpiryMap.put(ownerPhone, System.currentTimeMillis() + 300000);

            //  Cool SMS 를 통하여, 받아온 유저 휴대폰 번호에 코드를 보낸다.  //
            smsUtils.sendOne(ownerPhone, verificationCode);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return PostSmsSendResponseDto.success();

    }

    //  로그인 및 회원가입 페이지 - 사장님 휴대폰 인증코드 체크하기  //
    @Override
    @Transactional
    public ResponseEntity<? super PostSmsCheckResponseDto> checkPhoneOwner(String ownerPhone, int phoneKey) {

        try {
            //  이메일과 인증코드가 Map 에 저장되어 있는 인증코드와 같다면  //
            if (CodeMap.containsKey(ownerPhone) && CodeMap.get(ownerPhone).equals(phoneKey)) {

                //  Map 에 저장되어 있는 인증코드의 유효시간이 지났다면  //
                if (System.currentTimeMillis() > CodeExpiryMap.get(ownerPhone)) {

                    //  Map 에 저장되어 있는 정보를 삭제하고, 유효시간이 만료된 응답을 보낸다.  //
                    CodeMap.remove(ownerPhone);
                    CodeExpiryMap.remove(ownerPhone);
                    throw new CustomException(CommonErrorCode.EF);

                }

                //  인증 성공 시, Map 에 저장되어 있는 코드와 유효시간을 삭제한다.  //
                CodeMap.remove(ownerPhone);
                CodeExpiryMap.remove(ownerPhone);

                return PostSmsCheckResponseDto.success();

            } else {

                //  인증코드가 틀리다면 틀린 인증번호에 대한 응답을 보낸다.  //
                throw new CustomException(CommonErrorCode.IC);

            }

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

    }

    //  로그인 및 회원가입 페이지 - 사장님 이메일 인증 보내기  //
    @Override
    @Transactional
    public ResponseEntity<? super PostMailSendResponseDto> sendAuthCodeOwner(String ownerEmail) {

        try {

            //  입력받은 이메일이 비어있는 값이면, 빈 값에 대한 응답을 보낸다.  //
            if (ownerEmail == null || ownerEmail.isEmpty()) {
                throw new CustomException(UserErrorCode.EE);
            }

            //  입력받은 이메일이 정규표현식을 통하여 이메일 형식에 맞지 않으면, 이메일 형식 오류에 대한 응답을 보낸다.  //
            String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
            Pattern patternEmail = Pattern.compile(emailRegex);
            Matcher matcherEmail = patternEmail.matcher(ownerEmail);
            if (!matcherEmail.matches()) {
                throw new CustomException(UserErrorCode.IE);
            }

            //  입력받은 이메일이 유저 테이블에 이미 있는 이메일 이라면, 중복 이메일에 대한 응답을 보낸다.  //
            boolean existedEmail = ownerRepository.existsByOwnerEmail(ownerEmail);
            if (existedEmail) {
                throw new CustomException(UserErrorCode.DE);
            }

            //  변수에 랜덤으로 생성되는 6자리의 숫자를 넣는다.  //
            int mailCode = createKey();

            //  Map 객체에 유저 이메일과 위에서 생성한 코드를 추가하고, 유효시간은 5분으로 지정한다. (5분뒤 삭제) //
            CodeMap.put(ownerEmail, mailCode);
            CodeExpiryMap.put(ownerEmail, System.currentTimeMillis() + 300000);

            //  MimeMessage 객체를 만든다.  //
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            //  MimeMessage 에 받아온 유저 이메일과, Text, Code 에 대한 값을 넣는다.  //
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(ownerEmail);
            helper.setSubject("글램픽 인증 코드");

            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<style>" +
                    "@import url('https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css');" +
                    "body {font-family: 'Pretendard-Regular', sans-serif;}" +
                    ".container {padding: 20px; text-align: center;}" +
                    ".message {font-size: 16px; color: #34495e; margin-top: 20px;}" +
                    ".code {font-size: 24px; font-weight: bold; color: #2c3e50; margin-top: 10px;}" +
                    ".highlight {color: #355179;}" + // 이미지 색상과 조화로운 색상
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<img src='cid:mailImage' alt='메일 이미지' class='background-image'>" +
                    "<p class='message'>안녕하세요.</p>" +
                    "<p class='message'>글램픽 인증 코드는 다음과 같습니다.</p>" +
                    "<p class='code highlight'>" + mailCode + "</p>" +
                    "<p class='message'>글램픽을 이용해 주셔서 감사합니다 !</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";
            helper.setText(htmlContent, true);
            helper.addInline("mailImage", new ClassPathResource("mailImage/main-big.png"));

            //  위에서 정의한 MimeMessage 를 전송한다.  //
            mailSender.send(mimeMessage);

            return PostMailSendResponseDto.success();

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
    }

    //  로그인 및 회원가입 페이지 - 사장님 이메일 코드 체크하기  //
    @Override
    @Transactional
    public ResponseEntity<? super PostMailCheckResponseDto> checkCodeOwner(String ownerEmail, int emailKey) {

        try {
            //  이메일과 인증코드가 Map 에 저장되어 있는 인증코드와 같다면  //
            if (CodeMap.containsKey(ownerEmail) && CodeMap.get(ownerEmail).equals(emailKey)) {

                //  Map 에 저장되어 있는 인증코드의 유효시간이 지났다면  //
                if (System.currentTimeMillis() > CodeExpiryMap.get(ownerEmail)) {

                    //  Map 에 저장되어 있는 정보를 삭제하고, 유효시간이 만료된 응답을 보낸다.  //
                    CodeMap.remove(ownerEmail);
                    CodeExpiryMap.remove(ownerEmail);
                    throw new CustomException(CommonErrorCode.EF);

                }

                //  인증 성공 시, Map 에 저장되어 있는 코드와 유효시간을 삭제한다.  //
                CodeMap.remove(ownerEmail);
                CodeExpiryMap.remove(ownerEmail);

                return PostMailCheckResponseDto.success();

            } else {

                //  인증코드가 틀리다면 틀린 인증번호에 대한 응답을 보낸다.  //
                throw new CustomException(CommonErrorCode.IC);

            }

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
    }

    //  로그인 및 회원가입 페이지 - 유저 이메일 찾기  //
    @Override
    @Transactional
    public ResponseEntity<? super PostSearchEmailResponseDto> searchEmail(PostSearchEmailRequestDto dto) {

        try { if (dto == null) { throw new CustomException(CommonErrorCode.VF); }
        } catch (CustomException e) { throw new CustomException(e.getErrorCode()); }

        UserEntity userEntity = userRepository.findByUserNameAndUserPhone(dto.getUserName(), dto.getUserPhone());

        try {
            if (userEntity == null) { throw new CustomException(UserErrorCode.NU); }
        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return PostSearchEmailResponseDto.success(userEntity.getUserEmail());

    }

    //  로그인 및 회원가입 페이지 - 유저 이메일 찾기 - 휴대폰 인증 보내기  //
    @Override
    @Transactional
    public ResponseEntity<? super PostSmsSendResponseDto> sendOneSearchEmail(String userPhone) {
        int verificationCode;

        try {

            String phoneRegex = "^(01[016789]-?\\d{3,4}-?\\d{4})|(0[2-9][0-9]-?\\d{3,4}-?\\d{4})$";
            Pattern patternPhone = Pattern.compile(phoneRegex);
            Matcher matcherPhone = patternPhone.matcher(userPhone);
            if (!matcherPhone.matches()) { throw new CustomException(UserErrorCode.IPH); }

            UserEntity userEntity = userRepository.findByUserPhone(userPhone);
            if (userEntity == null) { throw new CustomException(UserErrorCode.NU); }


            //  받아온 유저 휴대폰 번호의 "-" 부분을 없앤다. (010-1234-5678 -> 01012345678)  //
            userPhone.replaceAll("-", "");

            //  변수에 랜덤으로 생성되는 6자리의 숫자를 넣는다.  //
            verificationCode = createKey();

            //  Map 객체에 유저 휴대폰 번호와 위에서 생성한 코드를 추가하고, 유효시간은 5분으로 지정한다. (5분뒤 삭제) //
            CodeMap.put(userPhone, verificationCode);
            CodeExpiryMap.put(userPhone, System.currentTimeMillis() + 300000);

            //  Cool SMS 를 통하여, 받아온 유저 휴대폰 번호에 코드를 보낸다.  //
            smsUtils.sendOne(userPhone, verificationCode);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return PostSmsSendResponseDto.success();
    }

    //  로그인 및 회원가입 페이지 - 유저 이메일 찾기 - 휴대폰 인증 체크하기  //
    @Override
    @Transactional
    public ResponseEntity<? super PostSmsCheckResponseDto> checkPhoneSearchEmail(String userPhone, int phoneKey) {

        try {

            if (CodeMap.containsKey(userPhone) && CodeMap.get(userPhone).equals(phoneKey)) {

                if (System.currentTimeMillis() > CodeExpiryMap.get(userPhone)) {
                    CodeMap.remove(userPhone);
                    CodeExpiryMap.remove(userPhone);
                    throw new CustomException(CommonErrorCode.EF);
                }

                CodeMap.remove(userPhone);
                CodeExpiryMap.remove(userPhone);

                return PostSmsCheckResponseDto.success();

            } else { throw new CustomException(CommonErrorCode.IC); }

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
    }

    //  로그인 및 회원가입 페이지 - 유저 비밀번호 검증 후 변경처리  //
    @Override
    @Transactional
    public ResponseEntity<? super PostSearchPwResponseDto> searchPw(PostSearchPwRequestDto dto) {

        try { if (dto == null) { throw new CustomException(CommonErrorCode.VF); }
        } catch (CustomException e) { throw new CustomException(e.getErrorCode()); }

        UserEntity userEntity = userRepository.findByUserEmailAndUserName(dto.getUserEmail(), dto.getUserName());

        try {

            if (userEntity == null) { throw new CustomException(UserErrorCode.NU); }

            String userPw = dto.getUserPw();
            String passwordRegex = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
            Pattern patternPw = Pattern.compile(passwordRegex);
            Matcher matcherPw = patternPw.matcher(userPw);
            if (!matcherPw.matches()) { throw new CustomException(UserErrorCode.IP); }

            String encodingPw = passwordEncoder.encode(userPw);
            dto.setUserPw(encodingPw);

            userEntity.setUserPw(dto.getUserPw());

            userRepository.save(userEntity);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return PostSearchPwResponseDto.success();

    }

    //  로그인 및 회원가입 페이지 - 유저 비밀번호 찾기 - 이메일 인증 보내기  //
    @Override
    @Transactional
    public ResponseEntity<? super PostMailSendResponseDto> sendMailSearchPw(String userEmail) {

        try {
            if (userEmail == null || userEmail.isEmpty()) {
                throw new CustomException(UserErrorCode.EE);
            }

            String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
            Pattern patternEmail = Pattern.compile(emailRegex);
            Matcher matcherEmail = patternEmail.matcher(userEmail);
            if (!matcherEmail.matches()) {
                throw new CustomException(UserErrorCode.IE);
            }

            //  입력받은 이메일이 유저 테이블에 이미 있는 이메일 이라면, 중복 이메일에 대한 응답을 보낸다.  //
            UserEntity userEntity = userRepository.findByUserEmail(userEmail);
            if (userEntity == null) { throw new CustomException(UserErrorCode.NU); }

            //  변수에 랜덤으로 생성되는 6자리의 숫자를 넣는다.  //
            int mailCode = createKey();

            //  Map 객체에 유저 이메일과 위에서 생성한 코드를 추가하고, 유효시간은 5분으로 지정한다. (5분뒤 삭제) //
            CodeMap.put(userEmail, mailCode);
            CodeExpiryMap.put(userEmail, System.currentTimeMillis() + 300000);

            //  MimeMessage 객체를 만든다.  //
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            //  MimeMessage 에 받아온 유저 이메일과, Text, Code 에 대한 값을 넣는다.  //
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(userEmail);
            helper.setSubject("글램픽 인증 코드");

            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<style>" +
                    "@import url('https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css');" +
                    "body {font-family: 'Pretendard-Regular', sans-serif;}" +
                    ".container {padding: 20px; text-align: center;}" +
                    ".message {font-size: 16px; color: #34495e; margin-top: 20px;}" +
                    ".code {font-size: 24px; font-weight: bold; color: #2c3e50; margin-top: 10px;}" +
                    ".highlight {color: #355179;}" + // 이미지 색상과 조화로운 색상
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<img src='cid:mailImage' alt='메일 이미지' class='background-image'>" +
                    "<p class='message'>안녕하세요.</p>" +
                    "<p class='message'>글램픽 인증 코드는 다음과 같습니다.</p>" +
                    "<p class='code highlight'>" + mailCode + "</p>" +
                    "<p class='message'>글램픽을 이용해 주셔서 감사합니다 !</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";
            helper.setText(htmlContent, true);
            helper.addInline("mailImage", new ClassPathResource("mailImage/main-big.png"));

            //  위에서 정의한 MimeMessage 를 전송한다.  //
            mailSender.send(mimeMessage);

            return PostMailSendResponseDto.success();

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
    }

    //  로그인 및 회원가입 페이지 - 유저 비밀번호 찾기 - 이메일 인증 체크하기  //
    @Override
    @Transactional
    public ResponseEntity<? super PostMailCheckResponseDto> mailCheckSearchPw(String userEmail, int emailKey) {

        try {
            if (CodeMap.containsKey(userEmail) && CodeMap.get(userEmail).equals(emailKey)) {

                if (System.currentTimeMillis() > CodeExpiryMap.get(userEmail)) {
                    CodeMap.remove(userEmail);
                    CodeExpiryMap.remove(userEmail);
                    throw new CustomException(CommonErrorCode.EF);
                }

                CodeMap.remove(userEmail);
                CodeExpiryMap.remove(userEmail);

                return PostMailCheckResponseDto.success();

            } else {
                throw new CustomException(CommonErrorCode.IC);
            }

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

    }

    //  로그인 및 회원가입 페이지 - 사장님 이메일 찾기  //
    @Override
    @Transactional
    public ResponseEntity<? super PostSearchEmailResponseDto> searchOwnerEmail(PostSearchOwnerEmailRequestDto dto) {

        try { if (dto == null) { throw new CustomException(CommonErrorCode.VF); }
        } catch (CustomException e) { throw new CustomException(e.getErrorCode()); }

        OwnerEntity ownerEntity = ownerRepository.findByOwnerNameAndOwnerPhone(dto.getOwnerName(), dto.getOwnerPhone());

        try {
            if (ownerEntity == null) { throw new CustomException(UserErrorCode.NU); }
        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return PostSearchEmailResponseDto.success(ownerEntity.getOwnerEmail());

    }

    //  로그인 및 회원가입 페이지 - 사장님 이메일 찾기 - 휴대폰 인증 보내기  //
    @Override
    @Transactional
    public ResponseEntity<? super PostSmsSendResponseDto> sendOneSearchOwnerEmail(String ownerPhone) {
        int verificationCode;

        try {

            String phoneRegex = "^(01[016789]-?\\d{3,4}-?\\d{4})|(0[2-9][0-9]-?\\d{3,4}-?\\d{4})$";
            Pattern patternPhone = Pattern.compile(phoneRegex);
            Matcher matcherPhone = patternPhone.matcher(ownerPhone);
            if (!matcherPhone.matches()) { throw new CustomException(UserErrorCode.IPH); }

            OwnerEntity ownerEntity = ownerRepository.findByOwnerPhone(ownerPhone);
            if (ownerEntity == null) { throw new CustomException(UserErrorCode.NU); }


            //  받아온 유저 휴대폰 번호의 "-" 부분을 없앤다. (010-1234-5678 -> 01012345678)  //
            ownerPhone.replaceAll("-", "");

            //  변수에 랜덤으로 생성되는 6자리의 숫자를 넣는다.  //
            verificationCode = createKey();

            //  Map 객체에 유저 휴대폰 번호와 위에서 생성한 코드를 추가하고, 유효시간은 5분으로 지정한다. (5분뒤 삭제) //
            CodeMap.put(ownerPhone, verificationCode);
            CodeExpiryMap.put(ownerPhone, System.currentTimeMillis() + 300000);

            //  Cool SMS 를 통하여, 받아온 유저 휴대폰 번호에 코드를 보낸다.  //
            smsUtils.sendOne(ownerPhone, verificationCode);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return PostSmsSendResponseDto.success();
    }

    //  로그인 및 회원가입 페이지 - 사장님 이메일 찾기 - 휴대폰 인증 체크하기  //
    @Override
    @Transactional
    public ResponseEntity<? super PostSmsCheckResponseDto> checkPhoneSearchOwnerEmail(String ownerPhone, int phoneKey) {

        try {

            if (CodeMap.containsKey(ownerPhone) && CodeMap.get(ownerPhone).equals(phoneKey)) {

                if (System.currentTimeMillis() > CodeExpiryMap.get(ownerPhone)) {
                    CodeMap.remove(ownerPhone);
                    CodeExpiryMap.remove(ownerPhone);
                    throw new CustomException(CommonErrorCode.EF);
                }

                CodeMap.remove(ownerPhone);
                CodeExpiryMap.remove(ownerPhone);

                return PostSmsCheckResponseDto.success();

            } else { throw new CustomException(CommonErrorCode.IC); }

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
    }

    //  로그인 및 회원가입 페이지 - 사장님 비밀번호 검증 후 변경처리  //
    @Override
    @Transactional
    public ResponseEntity<? super PostSearchPwResponseDto> searchOwnerPw(PostSearchOwnerPwRequestDto dto) {

        try { if (dto == null) { throw new CustomException(CommonErrorCode.VF); }
        } catch (CustomException e) { throw new CustomException(e.getErrorCode()); }

        OwnerEntity ownerEntity = ownerRepository.findByOwnerEmailAndOwnerName(dto.getOwnerEmail(), dto.getOwnerName());

        try {

            if (ownerEntity == null) { throw new CustomException(UserErrorCode.NU); }

            String userPw = dto.getOwnerPw();
            String passwordRegex = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
            Pattern patternPw = Pattern.compile(passwordRegex);
            Matcher matcherPw = patternPw.matcher(userPw);
            if (!matcherPw.matches()) { throw new CustomException(UserErrorCode.IP); }

            String encodingPw = passwordEncoder.encode(userPw);
            dto.setOwnerPw(encodingPw);

            ownerEntity.setOwnerPw(dto.getOwnerPw());

            ownerRepository.save(ownerEntity);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return PostSearchPwResponseDto.success();

    }

    //  로그인 및 회원가입 페이지 - 사장님 비밀번호 찾기 - 이메일 인증 보내기  //
    @Override
    @Transactional
    public ResponseEntity<? super PostMailSendResponseDto> sendMailSearchOwnerPw(String ownerEmail) {

        try {
            if (ownerEmail == null || ownerEmail.isEmpty()) {
                throw new CustomException(UserErrorCode.EE);
            }

            String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
            Pattern patternEmail = Pattern.compile(emailRegex);
            Matcher matcherEmail = patternEmail.matcher(ownerEmail);
            if (!matcherEmail.matches()) {
                throw new CustomException(UserErrorCode.IE);
            }

            //  입력받은 이메일이 유저 테이블에 이미 있는 이메일 이라면, 중복 이메일에 대한 응답을 보낸다.  //
            UserEntity userEntity = userRepository.findByUserEmail(ownerEmail);
            if (userEntity == null) { throw new CustomException(UserErrorCode.NU); }

            //  변수에 랜덤으로 생성되는 6자리의 숫자를 넣는다.  //
            int mailCode = createKey();

            //  Map 객체에 유저 이메일과 위에서 생성한 코드를 추가하고, 유효시간은 5분으로 지정한다. (5분뒤 삭제) //
            CodeMap.put(ownerEmail, mailCode);
            CodeExpiryMap.put(ownerEmail, System.currentTimeMillis() + 300000);

            //  MimeMessage 객체를 만든다.  //
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            //  MimeMessage 에 받아온 유저 이메일과, Text, Code 에 대한 값을 넣는다.  //
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(ownerEmail);
            helper.setSubject("글램픽 인증 코드");

            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<style>" +
                    "@import url('https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css');" +
                    "body {font-family: 'Pretendard-Regular', sans-serif;}" +
                    ".container {padding: 20px; text-align: center;}" +
                    ".message {font-size: 16px; color: #34495e; margin-top: 20px;}" +
                    ".code {font-size: 24px; font-weight: bold; color: #2c3e50; margin-top: 10px;}" +
                    ".highlight {color: #355179;}" + // 이미지 색상과 조화로운 색상
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<img src='cid:mailImage' alt='메일 이미지' class='background-image'>" +
                    "<p class='message'>안녕하세요.</p>" +
                    "<p class='message'>글램픽 인증 코드는 다음과 같습니다.</p>" +
                    "<p class='code highlight'>" + mailCode + "</p>" +
                    "<p class='message'>글램픽을 이용해 주셔서 감사합니다 !</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";
            helper.setText(htmlContent, true);
            helper.addInline("mailImage", new ClassPathResource("mailImage/main-big.png"));

            //  위에서 정의한 MimeMessage 를 전송한다.  //
            mailSender.send(mimeMessage);

            return PostMailSendResponseDto.success();

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
    }

    //  로그인 및 회원가입 페이지 - 사장님 비밀번호 찾기 - 이메일 인증 체크하기  //
    @Override
    @Transactional
    public ResponseEntity<? super PostMailCheckResponseDto> mailCheckSearchOwnerPw(String ownerEmail, int emailKey) {

        try {
            if (CodeMap.containsKey(ownerEmail) && CodeMap.get(ownerEmail).equals(emailKey)) {

                if (System.currentTimeMillis() > CodeExpiryMap.get(ownerEmail)) {
                    CodeMap.remove(ownerEmail);
                    CodeExpiryMap.remove(ownerEmail);
                    throw new CustomException(CommonErrorCode.EF);
                }

                CodeMap.remove(ownerEmail);
                CodeExpiryMap.remove(ownerEmail);

                return PostMailCheckResponseDto.success();

            } else {
                throw new CustomException(CommonErrorCode.IC);
            }

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

    }

}
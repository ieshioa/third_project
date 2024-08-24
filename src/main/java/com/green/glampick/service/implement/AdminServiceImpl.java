package com.green.glampick.service.implement;

import com.green.glampick.common.CustomFileUtils;
import com.green.glampick.common.Role;
import com.green.glampick.dto.request.admin.DeleteBannerRequestDto;
import com.green.glampick.dto.request.admin.exclusionGlampingRequestDto;
import com.green.glampick.dto.request.admin.exclusionSignUpRequestDto;
import com.green.glampick.dto.request.admin.module.AdminModule;
import com.green.glampick.dto.response.admin.*;
import com.green.glampick.entity.BannerEntity;
import com.green.glampick.entity.GlampingEntity;
import com.green.glampick.entity.GlampingWaitEntity;
import com.green.glampick.entity.OwnerEntity;
import com.green.glampick.exception.CustomException;
import com.green.glampick.exception.errorCode.AdminErrorCode;
import com.green.glampick.exception.errorCode.CommonErrorCode;
import com.green.glampick.exception.errorCode.UserErrorCode;
import com.green.glampick.repository.*;
import com.green.glampick.repository.resultset.GetAccessGlampingListResultSet;
import com.green.glampick.repository.resultset.GetAccessOwnerSignUpListResultSet;
import com.green.glampick.repository.resultset.GetDeleteOwnerListResultSet;
import com.green.glampick.security.AuthenticationFacade;
import com.green.glampick.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static com.green.glampick.common.GlobalConst.MAX_BANNER_SIZE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final OwnerRepository ownerRepository;
    private final BannerRepository bannerRepository;
    private final GlampingRepository glampingRepository;
    private final GlampingWaitRepository glampingWaitRepository;
    private final AdminRepository adminRepository;
    private final CustomFileUtils customFileUtils;

    private final JavaMailSender mailSender;

    //  관리자 페이지 - 사장님 회원가입 정보 확인하기  //
    @Override
    @Transactional
    public ResponseEntity<? super GetOwnerSignUpResponseDto> getOwnerSignUpInfo(Long ownerId) {

        try {

            OwnerEntity ownerEntity = ownerRepository.findByOwnerId(ownerId);
            return GetOwnerSignUpResponseDto.success(ownerEntity);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

    }

    //  관리자 페이지 - 대기중인 사장님 회원가입 리스트 불러오기  //
    @Override
    @Transactional
    public ResponseEntity<? super GetAccessOwnerSignUpListResponseDto> accessSignUpList() {

        try {

            List<GetAccessOwnerSignUpListResultSet> list = adminRepository.getAccessOwnerSignUpList();

            return GetAccessOwnerSignUpListResponseDto.success(list);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

    }

    //  관리자 페이지 - 사장님 회원가입 승인 처리하기  //
    @Override
    @Transactional
    public ResponseEntity<? super PatchAccessOwnerSignUpResponseDto> accessSignUp(Long ownerId) {

        try {

            ownerRepository.updateOwnerRole(Role.ROLE_OWNER, ownerId);
            OwnerEntity ownerEntity = ownerRepository.findByOwnerId(ownerId);

            //  MimeMessage 객체를 만든다.  //
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            //  MimeMessage 에 받아온 유저 이메일과, Text, Code 에 대한 값을 넣는다.  //
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(ownerEntity.getOwnerEmail());
            helper.setSubject("[글램픽] 신청하신 사장님 회원가입 심사 처리 내용입니다.");

            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<style>" +
                    "@import url('https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css');" +
                    "body {font-family: 'Pretendard-Regular', sans-serif;}" +
                    ".container {padding: 20px; text-align: center;}" +
                    ".message {font-size: 16px; color: #34495e; margin-top: 20px;}" +
                    ".code {font-size: 24px; font-weight: bold; color: #2c3e50; margin-top: 10px;}" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<img src='cid:mailImage' alt='메일 이미지' class='background-image'>" +
                    "<p class='message'>안녕하세요, 글램픽 관리자 입니다.</p>" +
                    "<p class='message'>신청하신 사장님 회원가입 신청이 승인되었습니다.</p>" +
                    "<p class='message'>승인된 시점으로부터 글램핑 등록이 가능합니다.</p>" +
                    "<p class='message'>글램핑 등록, 수정시에는 등록 심사를 받아야하니 이용에 참고 부탁드립니다.</p>" +
                    "<p class='message'>항상 사장님들의 건승을 기원드립니다.</p>" +
                    "<p class='message'>감사합니다.</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";
            helper.setText(htmlContent, true);
            helper.addInline("mailImage", new ClassPathResource("mailImage/main-big.png"));

            //  위에서 정의한 MimeMessage 를 전송한다.  //
            mailSender.send(mimeMessage);

        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return PatchAccessOwnerSignUpResponseDto.success();
    }

    //  관리자 페이지 - 사장님 회원가입 반려 처리하기 - 완료  //
    @Override
    @Transactional
    public ResponseEntity<? super DeleteExclusionOwnerSignUpResponseDto> exclutionSignUp(exclusionSignUpRequestDto dto) {

        try {

            OwnerEntity ownerEntity = ownerRepository.findByOwnerId(dto.getOwnerId());
            if (ownerEntity.getRole() != Role.ROLE_RESERVE_OWNER) { throw new CustomException(UserErrorCode.NEP); }

            //  MimeMessage 객체를 만든다.  //
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            //  MimeMessage 에 받아온 유저 이메일과, Text, Code 에 대한 값을 넣는다.  //
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(ownerEntity.getOwnerEmail());
            helper.setSubject("[글램픽] 신청하신 사장님 회원가입 심사 처리 내용입니다.");

            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<style>" +
                    "@import url('https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css');" +
                    "body {font-family: 'Pretendard-Regular', sans-serif;}" +
                    ".container {padding: 20px; text-align: center;}" +
                    ".message {font-size: 16px; color: #34495e; margin-top: 20px;}" +
                    ".code {font-size: 24px; font-weight: bold; color: #2c3e50; margin-top: 10px;}" +
                    ".highlight {color: #000000;}" + // 이미지 색상과 조화로운 색상
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<img src='cid:mailImage' alt='메일 이미지' class='background-image'>" +
                    "<p class='message'>안녕하세요, 글램픽 관리자 입니다.</p>" +
                    "<p class='message'>신청하신 사장님 회원가입 신청이 반려되었습니다.</p>" +
                    "<p class='message'>반려된 사유는 아래와 같습니다.</p>" +
                    "<p class='code highlight'>" + dto.getExclusionComment() + "</p>" +
                    "<p class='message'>반려처리와 함께 회원가입은 취소처리 되오니, 사유를 다시 확인 하시고 회원가입 부탁드립니다.</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";
            helper.setText(htmlContent, true);
            helper.addInline("mailImage", new ClassPathResource("mailImage/main-big.png"));

            //  위에서 정의한 MimeMessage 를 전송한다.  //
            mailSender.send(mimeMessage);

            String filePath = String.format("businessInfo/%d", ownerEntity.getOwnerId());
            customFileUtils.deleteFolder(filePath);

            ownerRepository.delete(ownerEntity);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return DeleteExclusionOwnerSignUpResponseDto.success();

    }

    //  관리자 페이지 - 메인 화면 배너 추가하기  //
    @Override
    @Transactional
    public ResponseEntity<? super PostBannerResponseDto> postBanner(List<MultipartFile> file) {

        List<BannerEntity> bannerEntityList = bannerRepository.findAll();

        try {

            if (bannerEntityList.size() + file.size() > MAX_BANNER_SIZE) {
                throw new CustomException(AdminErrorCode.UFF);
            }

            for (MultipartFile image : file) {
                String makeFolder = String.format("banner");
                customFileUtils.makeFolders(makeFolder);
                String saveFileName = customFileUtils.makeRandomFileName(image);
                String saveDbFileName = String.format("/pic/banner/%s",saveFileName);
                String filePath = String.format("/%s/%s", makeFolder, saveFileName);
                customFileUtils.transferTo(image, filePath);

                BannerEntity bannerEntity = new BannerEntity();
                bannerEntity.setBannerUrl(saveDbFileName);
                bannerRepository.save(bannerEntity);

            }

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
        return PostBannerResponseDto.success();
    }

    //  관리자 페이지 - 메인 화면 배너 삭제하기 - 완료  //
    @Override
    @Transactional
    public ResponseEntity<? super DeleteBannerResponseDto> deleteBanner(Long bannerId) {

        try {

            if (bannerId == null) { throw new CustomException(AdminErrorCode.NFB); }

            BannerEntity bannerEntity = bannerRepository.findByBannerId(bannerId);
            AdminModule.deleteBannerImage(bannerId, bannerRepository, customFileUtils);
            bannerRepository.delete(bannerEntity);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

        return DeleteBannerResponseDto.success();

    }

    //  관리자 페이지 - 메인 화면 배너 불러오기  //
    @Override
    @Transactional
    public ResponseEntity<? super GetBannerResponseDto> getBanner() {

        try {

            List<BannerEntity> list = bannerRepository.findAll();
            return GetBannerResponseDto.success(list);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            throw new CustomException(CommonErrorCode.DBE);
        }

    }

    //  관리자 페이지 - 승인 대기중인 글램핑장 리스트 불러오기  //
    @Override
    @Transactional
    public ResponseEntity<? super GetAccessGlampingListResponseDto> getAccessGlampingList() {

        try {

            List<GetAccessGlampingListResultSet> list = adminRepository.getAccessGlampingList();
            return GetAccessGlampingListResponseDto.success(list);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            throw new CustomException(CommonErrorCode.DBE);
        }

    }

    //  관리자 페이지 - 사장님 글램핑 등록 상세 정보 불러오기 - 완료  //
    @Override
    @Transactional
    public ResponseEntity<? super GetAccessGlampingInfoResponseDto> getAccessGlamping(Long glampId) {


//        GlampingEntity glampingEntity = new GlampingEntity();
        GlampingWaitEntity glampingWaitEntity = new GlampingWaitEntity();
        try {

//            glampingEntity = glampingRepository.findByGlampId(glampId);
            glampingWaitEntity = glampingWaitRepository.findByGlampId(glampId);
//            if (glampingEntity == null) { throw new CustomException(AdminErrorCode.NG); }
            if (glampingWaitEntity == null) { throw new CustomException(AdminErrorCode.NG); }
        } catch (CustomException e) {
          throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }

//        return GetAccessGlampingInfoResponseDto.success(glampingEntity);
        return GetAccessGlampingInfoResponseDto.success(glampingWaitEntity);

    }

    //  관리자 페이지 - 글램핑 등록 승인 처리하기  //
    @Override
    @Transactional
    public ResponseEntity<? super PatchGlampingAccessResponseDto> accessGlamping(Long glampId) {

        GlampingWaitEntity glampingWaitEntity = glampingWaitRepository.findByGlampId(glampId);
        OwnerEntity ownerEntity = ownerRepository.findByOwnerId(glampingWaitEntity.getOwner().getOwnerId());

        try {



            ownerEntity.setGlampingStatus(1);
            ownerRepository.save(ownerEntity);

            GlampingEntity glampingEntity = new GlampingEntity();
            glampingEntity.setOwner(glampingWaitEntity.getOwner());
            glampingEntity.setGlampName(glampingWaitEntity.getGlampName());
            glampingEntity.setGlampCall(glampingWaitEntity.getGlampCall());
            glampingEntity.setRecommendScore(0D);
            glampingEntity.setGlampImage(glampingWaitEntity.getGlampImage());
            glampingEntity.setStarPointAvg(0D);
            glampingEntity.setReviewCount(0);
            glampingEntity.setGlampLocation(glampingWaitEntity.getGlampLocation());
//            glampingEntity.setLocation(glampingWaitEntity.getLocation());
            glampingEntity.setRegion(glampingWaitEntity.getRegion());
            glampingEntity.setExtraCharge(glampingWaitEntity.getExtraCharge());
            glampingEntity.setGlampIntro(glampingWaitEntity.getGlampIntro());
            glampingEntity.setInfoBasic(glampingWaitEntity.getInfoBasic());
            glampingEntity.setInfoNotice(glampingWaitEntity.getInfoNotice());
            glampingEntity.setTraffic(glampingWaitEntity.getTraffic());
            glampingEntity.setActivateStatus(1);



            //  MimeMessage 객체를 만든다.  //
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            //  MimeMessage 에 받아온 유저 이메일과, Text, Code 에 대한 값을 넣는다.  //
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(ownerEntity.getOwnerEmail());
            helper.setSubject("[글램픽] 신청하신 사장님 글램핑 등록심사 처리 내용입니다.");

            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<style>" +
                    "@import url('https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css');" +
                    "body {font-family: 'Pretendard-Regular', sans-serif;}" +
                    ".container {padding: 20px; text-align: center;}" +
                    ".message {font-size: 16px; color: #34495e; margin-top: 20px;}" +
                    ".code {font-size: 24px; font-weight: bold; color: #2c3e50; margin-top: 10px;}" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<img src='cid:mailImage' alt='메일 이미지' class='background-image'>" +
                    "<p class='message'>안녕하세요, 글램픽 관리자 입니다.</p>" +
                    "<p class='message'>신청하신 사장님 글램핑 등록 신청이 승인되었습니다.</p>" +
                    "<p class='message'>승인된 시점으로부터 객실 등록이 가능합니다.</p>" +
                    "<p class='message'>글램핑 수정시에는 등록 심사를 받아야하니 이용에 참고 부탁드립니다.</p>" +
                    "<p class='message'>이용에 불편함이 있으면 고객센터로 문의 부탁드립니다.</p>" +
                    "<p class='message'>감사합니다.</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";
            helper.setText(htmlContent, true);
            helper.addInline("mailImage", new ClassPathResource("mailImage/main-big.png"));

            //  위에서 정의한 MimeMessage 를 전송한다.  //
            mailSender.send(mimeMessage);

            glampingRepository.save(glampingEntity);

            String fileOriginName = glampingWaitEntity.getGlampImage(); // 예: /pic/glampingWait/11/glamp/4a6f8660-ec70-42c2-9176-0bb361824379.jpg

            // "/glampingWait"부터 끝까지의 경로를 추출 ("/glampingWait" 앞의 "/pic" 부분은 제외)
            int idx = fileOriginName.indexOf("/glampingWait");
            String fileOriginPath = fileOriginName.substring(idx); // "/glampingWait/11/glamp/4a6f8660-ec70-42c2-9176-0bb361824379.jpg"

            // 순수 파일 이름 추출
            int index = fileOriginName.lastIndexOf("/");
            String fileFullName = fileOriginName.substring(index + 1); // "4a6f8660-ec70-42c2-9176-0bb361824379.jpg"

            // 새로운 경로 생성
            String path = String.format("/%s/%s/glamp", "glamping", glampId); // "/glamping/11/glamp"
            customFileUtils.makeFolders(path);
            String filePath = String.format("%s/%s", path, fileFullName); // "/glamping/11/glamp/4a6f8660-ec70-42c2-9176-0bb361824379.jpg"

            // 원본 파일과 복사될 파일 경로 설정
            File file = new File(customFileUtils.uploadPath + fileOriginPath); // "/glampingWait/11/glamp/4a6f8660-ec70-42c2-9176-0bb361824379.jpg"
            File copyfile = new File(customFileUtils.uploadPath + filePath); // "/glamping/11/glamp/4a6f8660-ec70-42c2-9176-0bb361824379.jpg"

            Files.copy(file.toPath(),copyfile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            customFileUtils.deleteFolder(fileOriginPath);

            glampingEntity.setGlampImage("/pic" + filePath);
            glampingRepository.save(glampingEntity);

            glampingWaitRepository.delete(glampingWaitEntity);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
        return PatchGlampingAccessResponseDto.success();

    }

    //  관리자 페이지 - 글램핑 등록 반려 처리하기  //
    @Override
    @Transactional
    public ResponseEntity<? super GlampingExclutionResponseDto> exclutionGlamping(exclusionGlampingRequestDto dto) {

        try {

            GlampingWaitEntity glampingWaitEntity = glampingWaitRepository.findByGlampId(dto.getGlampId());

            glampingWaitEntity.setExclusionStatus(-1);

            //  MimeMessage 객체를 만든다.  //
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            //  MimeMessage 에 받아온 유저 이메일과, Text, Code 에 대한 값을 넣는다.  //
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(glampingWaitEntity.getOwner().getOwnerEmail());
            helper.setSubject("[글램픽] 신청하신 사장님 글램핑 등록심사 처리 내용입니다.");

            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<style>" +
                    "@import url('https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css');" +
                    "body {font-family: 'Pretendard-Regular', sans-serif;}" +
                    ".container {padding: 20px; text-align: center;}" +
                    ".message {font-size: 16px; color: #34495e; margin-top: 20px;}" +
                    ".code {font-size: 24px; font-weight: bold; color: #2c3e50; margin-top: 10px;}" +
                    ".highlight {color: #000000;}" + // 이미지 색상과 조화로운 색상
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<img src='cid:mailImage' alt='메일 이미지' class='background-image'>" +
                    "<p class='message'>안녕하세요, 글램픽 관리자 입니다.</p>" +
                    "<p class='message'>신청하신 글램핑장 등록 신청이 반려되었습니다.</p>" +
                    "<p class='message'>반려된 사유는 아래와 같습니다.</p>" +
                    "<p class='code highlight'>" + dto.getExclusionComment() + "</p>" +
                    "<p class='message'>사유를 다시 확인 하시고 승인 신청 부탁드립니다.</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";
            helper.setText(htmlContent, true);
            helper.addInline("mailImage", new ClassPathResource("mailImage/main-big.png"));

            //  위에서 정의한 MimeMessage 를 전송한다.  //
            mailSender.send(mimeMessage);

            glampingWaitRepository.save(glampingWaitEntity);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            throw new CustomException(CommonErrorCode.DBE);
        }

        return GlampingExclutionResponseDto.success();

    }

    //  관리자 페이지 - 회원탈퇴 대기 사장님 리스트 불러오기  //
    @Override
    @Transactional
    public ResponseEntity<? super getDeleteOwnerListResponseDto> deleteOwnerList() {

        try {

            List<GetDeleteOwnerListResultSet> list = ownerRepository.getDeleteOwnerList();
            return getDeleteOwnerListResponseDto.success(list);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            throw new CustomException(CommonErrorCode.DBE);
        }

    }

    //  관리자 페이지 - 사장님 회원탈퇴 승인 처리하기 - 완료  //
    @Transactional
    @Override
    public ResponseEntity<? super PatchDeleteOwnerResponseDto> deleteOwner(Long ownerId) {

        try {

            OwnerEntity ownerEntity = ownerRepository.findByOwnerId(ownerId);

            //  MimeMessage 객체를 만든다.  //
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            //  MimeMessage 에 받아온 유저 이메일과, Text, Code 에 대한 값을 넣는다.  //
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(ownerEntity.getOwnerEmail());
            helper.setSubject("[글램픽] 신청하신 사장님 회원탈퇴 심사 처리 내용입니다.");

            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<style>" +
                    "@import url('https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css');" +
                    "body {font-family: 'Pretendard-Regular', sans-serif;}" +
                    ".container {padding: 20px; text-align: center;}" +
                    ".message {font-size: 16px; color: #34495e; margin-top: 20px;}" +
                    ".code {font-size: 24px; font-weight: bold; color: #2c3e50; margin-top: 10px;}" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='container'>" +
                    "<img src='cid:mailImage' alt='메일 이미지' class='background-image'>" +
                    "<p class='message'>안녕하세요, 글램픽 관리자 입니다.</p>" +
                    "<p class='message'>신청하신 사장님 회원탈퇴 신청이 승인되었습니다.</p>" +
                    "<p class='message'>가입하신 이메일은 재사용이 불가하오니, 추후 재가입 시 참고 부탁드립니다.</p>" +
                    "<p class='message'>가입을 다시 신청하실 때는 심사를 다시 받아 이용 가능합니다.</p>" +
                    "<p class='message'>더욱 노력하는 글램픽이 되겠습니다.</p>" +
                    "<p class='message'>감사합니다.</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";
            helper.setText(htmlContent, true);
            helper.addInline("mailImage", new ClassPathResource("mailImage/main-big.png"));

            //  위에서 정의한 MimeMessage 를 전송한다.  //
            mailSender.send(mimeMessage);


            if (ownerEntity.getActivateStatus() != 0) {
                throw new CustomException(AdminErrorCode.NWO);
            }

            ownerEntity.setActivateStatus(-1);
            ownerEntity.setBusinessNumber("null");
            ownerEntity.setOwnerPw(null);
            ownerEntity.setOwnerName("null");
            ownerEntity.setOwnerPhone("null");
            ownerEntity.setRole(null);
            ownerEntity.setBusinessPaperImage(null);
            if (ownerEntity.getBusinessPaperImage() != null) {
                AdminModule.deleteBusinessImage(ownerId, ownerRepository, customFileUtils);
            }
            ownerRepository.save(ownerEntity);

            GlampingEntity glampingEntity = glampingRepository.findByOwner(ownerEntity);
            if (glampingEntity == null) { return PatchDeleteOwnerResponseDto.success(); }

            glampingEntity.setActivateStatus(-1);
            glampingEntity.setGlampCall(null);
            glampingEntity.setGlampLocation("탈퇴한 회원입니다.");
            glampingEntity.setGlampIntro("탈퇴한 회원입니다.");
            glampingEntity.setInfoBasic("탈퇴한 회원입니다.");
            glampingEntity.setTraffic("탈퇴한 회원입니다.");
            glampingEntity.setInfoNotice("탈퇴한 회원입니다.");
            glampingEntity.setRegion("null");
            glampingEntity.setRecommendScore(0D);
            glampingEntity.setStarPointAvg(0D);
            glampingEntity.setReviewCount(0);
            glampingEntity.setExtraCharge(0);
            glampingRepository.save(glampingEntity);

        } catch (CustomException e) {
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(CommonErrorCode.DBE);
        }
        return PatchDeleteOwnerResponseDto.success();
    }

}

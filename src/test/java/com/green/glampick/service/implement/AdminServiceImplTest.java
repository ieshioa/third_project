package com.green.glampick.service.implement;

import com.green.glampick.common.CustomFileUtils;
import com.green.glampick.common.Role;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;

import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("tdd")
class AdminServiceImplTest {

    @InjectMocks
    private AdminServiceImpl adminService;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private BannerRepository bannerRepository;

    @Mock
    private GlampingRepository glampingRepository;

    @Mock
    private GlampingWaitRepository glampingWaitRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private CustomFileUtils customFileUtils;

    @Mock
    private AdminModule adminModule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAccessSignUpList_Success() {
        List<GetAccessOwnerSignUpListResultSet> mockList = new ArrayList<>();

        when(adminRepository.getAccessOwnerSignUpList()).thenReturn(mockList);

        ResponseEntity<?> response = adminService.accessSignUpList();

        assertNotNull(response);
        assertTrue(response.getBody() instanceof GetAccessOwnerSignUpListResponseDto);
        GetAccessOwnerSignUpListResponseDto responseDto = (GetAccessOwnerSignUpListResponseDto) response.getBody();
        assertEquals(mockList, responseDto.getList());

        verify(adminRepository, times(1)).getAccessOwnerSignUpList();
    }

    @Test
    void testAccessSignUpList_Exception() {
        when(adminRepository.getAccessOwnerSignUpList()).thenThrow(new CustomException(CommonErrorCode.DBE));

        CustomException exception = assertThrows(CustomException.class, () -> adminService.accessSignUpList());

        assertEquals(CommonErrorCode.DBE, exception.getErrorCode());

        verify(adminRepository, times(1)).getAccessOwnerSignUpList();
    }

    @Test
    void testAccessSignUp_Success() throws Exception {
        Long ownerId = 1L;
        OwnerEntity mockOwnerEntity = new OwnerEntity();
        mockOwnerEntity.setOwnerId(ownerId);
        mockOwnerEntity.setOwnerEmail("test@example.com");

        when(ownerRepository.findByOwnerId(ownerId)).thenReturn(mockOwnerEntity);
        MimeMessage mockMimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mockMimeMessage);

        ResponseEntity<?> response = adminService.accessSignUp(ownerId);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof PatchAccessOwnerSignUpResponseDto);

        verify(ownerRepository, times(1)).updateOwnerRole(Role.ROLE_OWNER, ownerId);
        verify(ownerRepository, times(1)).findByOwnerId(ownerId);
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void testAccessSignUp_Exception() {
        Long ownerId = 1L;

        when(ownerRepository.findByOwnerId(ownerId)).thenThrow(new CustomException(CommonErrorCode.DBE));

        CustomException exception = assertThrows(CustomException.class, () -> adminService.accessSignUp(ownerId));

        assertEquals(CommonErrorCode.DBE, exception.getErrorCode());

        verify(ownerRepository, times(1)).findByOwnerId(ownerId);
    }

    @Test
    void testExclutionSignUp_Success() throws Exception {
        exclusionSignUpRequestDto dto = new exclusionSignUpRequestDto();
        dto.setOwnerId(1L);
        dto.setExclusionComment("Invalid data");

        OwnerEntity mockOwnerEntity = new OwnerEntity();
        mockOwnerEntity.setOwnerId(dto.getOwnerId());
        mockOwnerEntity.setRole(Role.ROLE_RESERVE_OWNER);
        mockOwnerEntity.setOwnerEmail("test@example.com");

        when(ownerRepository.findByOwnerId(dto.getOwnerId())).thenReturn(mockOwnerEntity);
        MimeMessage mockMimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mockMimeMessage);

        ResponseEntity<?> response = adminService.exclutionSignUp(dto);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof DeleteExclusionOwnerSignUpResponseDto);

        verify(ownerRepository, times(1)).findByOwnerId(dto.getOwnerId());
        verify(mailSender, times(1)).send(any(MimeMessage.class));
        verify(ownerRepository, times(1)).delete(mockOwnerEntity);
    }

    @Test
    void testExclutionSignUp_InvalidRole() {
        exclusionSignUpRequestDto dto = new exclusionSignUpRequestDto();
        dto.setOwnerId(1L);

        OwnerEntity mockOwnerEntity = new OwnerEntity();
        mockOwnerEntity.setRole(Role.ROLE_OWNER);

        when(ownerRepository.findByOwnerId(dto.getOwnerId())).thenReturn(mockOwnerEntity);

        CustomException exception = assertThrows(CustomException.class, () -> adminService.exclutionSignUp(dto));

        assertEquals(UserErrorCode.NEP, exception.getErrorCode());

        verify(ownerRepository, times(1)).findByOwnerId(dto.getOwnerId());
    }

    @Test
    void testPostBanner_Success() throws Exception {
        List<MultipartFile> mockFiles = new ArrayList<>();
        MultipartFile mockFile = mock(MultipartFile.class);
        mockFiles.add(mockFile);

        when(bannerRepository.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = adminService.postBanner(mockFiles);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof PostBannerResponseDto);

        verify(bannerRepository, times(1)).findAll();
        verify(bannerRepository, times(mockFiles.size())).save(any(BannerEntity.class));
    }

    @Test
    void testPostBanner_TooManyFiles() {

        List<MultipartFile> mockFiles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mockFiles.add(mock(MultipartFile.class));
        }

        List<BannerEntity> existingBanners = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            existingBanners.add(new BannerEntity());
        }

        when(bannerRepository.findAll()).thenReturn(existingBanners);

        CustomException exception = assertThrows(CustomException.class, () -> adminService.postBanner(mockFiles));

        assertEquals(AdminErrorCode.UFF, exception.getErrorCode());

        verify(bannerRepository, times(1)).findAll();
    }

    @Test
    void testGetBanner_Success() {
        List<BannerEntity> mockList = new ArrayList<>();

        when(bannerRepository.findAll()).thenReturn(mockList);

        ResponseEntity<?> response = adminService.getBanner();

        assertNotNull(response);
        assertTrue(response.getBody() instanceof GetBannerResponseDto);
        GetBannerResponseDto responseDto = (GetBannerResponseDto) response.getBody();
        assertEquals(mockList, responseDto.getList());

        verify(bannerRepository, times(1)).findAll();
    }

    @Test
    void testGetAccessGlampingList_Success() {
        List<GetAccessGlampingListResultSet> mockList = new ArrayList<>();

        when(adminRepository.getAccessGlampingList()).thenReturn(mockList);

        ResponseEntity<?> response = adminService.getAccessGlampingList();

        assertNotNull(response);
        assertTrue(response.getBody() instanceof GetAccessGlampingListResponseDto);
        GetAccessGlampingListResponseDto responseDto = (GetAccessGlampingListResponseDto) response.getBody();
        assertEquals(mockList, responseDto.getList());

        verify(adminRepository, times(1)).getAccessGlampingList();
    }

    @Test
    void testGetAccessGlamping_Success() {
        Long glampId = 1L;
        GlampingWaitEntity mockGlampingWaitEntity = new GlampingWaitEntity();
        mockGlampingWaitEntity.setGlampId(glampId);
        mockGlampingWaitEntity.setGlampName("Test Glamping");

        OwnerEntity mockOwnerEntity = new OwnerEntity();
        mockOwnerEntity.setOwnerId(1L);
        mockGlampingWaitEntity.setOwner(mockOwnerEntity);

        when(glampingWaitRepository.findByGlampId(glampId)).thenReturn(mockGlampingWaitEntity);

        ResponseEntity<?> response = adminService.getAccessGlamping(glampId);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof GetAccessGlampingInfoResponseDto);
        GetAccessGlampingInfoResponseDto responseDto = (GetAccessGlampingInfoResponseDto) response.getBody();
        assertEquals(mockGlampingWaitEntity.getGlampName(), responseDto.getGlampName());

        verify(glampingWaitRepository, times(1)).findByGlampId(glampId);
    }

    @Test
    void testGetAccessGlamping_NotFound() {
        Long glampId = 1L;

        when(glampingWaitRepository.findByGlampId(glampId)).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class, () -> adminService.getAccessGlamping(glampId));

        assertEquals(AdminErrorCode.NG, exception.getErrorCode());

        verify(glampingWaitRepository, times(1)).findByGlampId(glampId);
    }

    @Test
    void testAccessGlamping_Exception() {
        Long glampId = 1L;

        when(glampingWaitRepository.findByGlampId(glampId)).thenThrow(new CustomException(CommonErrorCode.DBE));

        CustomException exception = assertThrows(CustomException.class, () -> adminService.accessGlamping(glampId));

        assertEquals(CommonErrorCode.DBE, exception.getErrorCode());

        verify(glampingWaitRepository, times(1)).findByGlampId(glampId);
    }

    @Test
    void testExclutionGlamping_Success() throws Exception {
        exclusionGlampingRequestDto dto = new exclusionGlampingRequestDto();
        dto.setGlampId(1L);
        dto.setExclusionComment("Invalid glamping information");

        GlampingWaitEntity mockGlampingWaitEntity = new GlampingWaitEntity();
        mockGlampingWaitEntity.setGlampId(dto.getGlampId());

        OwnerEntity mockOwnerEntity = new OwnerEntity();
        mockOwnerEntity.setOwnerEmail("test@example.com");
        mockGlampingWaitEntity.setOwner(mockOwnerEntity);

        when(glampingWaitRepository.findByGlampId(dto.getGlampId())).thenReturn(mockGlampingWaitEntity);
        MimeMessage mockMimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mockMimeMessage);

        ResponseEntity<?> response = adminService.exclutionGlamping(dto);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof GlampingExclutionResponseDto);

        verify(glampingWaitRepository, times(1)).findByGlampId(dto.getGlampId());
        verify(mailSender, times(1)).send(any(MimeMessage.class));
        verify(glampingWaitRepository, times(1)).save(mockGlampingWaitEntity);
    }

    @Test
    void testExclutionGlamping_Exception() {
        exclusionGlampingRequestDto dto = new exclusionGlampingRequestDto();
        dto.setGlampId(1L);

        when(glampingWaitRepository.findByGlampId(dto.getGlampId())).thenThrow(new CustomException(CommonErrorCode.DBE));

        CustomException exception = assertThrows(CustomException.class, () -> adminService.exclutionGlamping(dto));

        assertEquals(CommonErrorCode.DBE, exception.getErrorCode());

        verify(glampingWaitRepository, times(1)).findByGlampId(dto.getGlampId());
    }

    @Test
    void testDeleteOwnerList_Success() {
        List<GetDeleteOwnerListResultSet> mockList = new ArrayList<>();

        when(ownerRepository.getDeleteOwnerList()).thenReturn(mockList);

        ResponseEntity<?> response = adminService.deleteOwnerList();

        assertNotNull(response);
        assertTrue(response.getBody() instanceof getDeleteOwnerListResponseDto);
        getDeleteOwnerListResponseDto responseDto = (getDeleteOwnerListResponseDto) response.getBody();
        assertEquals(mockList, responseDto.getList());

        verify(ownerRepository, times(1)).getDeleteOwnerList();
    }

    @Test
    void testDeleteOwner_Success() throws Exception {
        Long ownerId = 1L;
        OwnerEntity mockOwnerEntity = new OwnerEntity();
        mockOwnerEntity.setOwnerId(ownerId);
        mockOwnerEntity.setOwnerEmail("test@example.com");
        mockOwnerEntity.setActivateStatus(0);

        GlampingEntity mockGlampingEntity = new GlampingEntity();
        mockGlampingEntity.setOwner(mockOwnerEntity);

        when(ownerRepository.findByOwnerId(ownerId)).thenReturn(mockOwnerEntity);
        when(glampingRepository.findByOwner(mockOwnerEntity)).thenReturn(mockGlampingEntity);
        MimeMessage mockMimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mockMimeMessage);

        ResponseEntity<?> response = adminService.deleteOwner(ownerId);

        assertNotNull(response);
        assertTrue(response.getBody() instanceof PatchDeleteOwnerResponseDto);

        verify(ownerRepository, times(1)).findByOwnerId(ownerId);
        verify(mailSender, times(1)).send(any(MimeMessage.class));
        verify(ownerRepository, times(1)).save(any(OwnerEntity.class));
        verify(glampingRepository, times(1)).save(any(GlampingEntity.class));
    }

    @Test
    void testDeleteOwner_Exception() {
        Long ownerId = 1L;

        when(ownerRepository.findByOwnerId(ownerId)).thenThrow(new CustomException(CommonErrorCode.DBE));

        CustomException exception = assertThrows(CustomException.class, () -> adminService.deleteOwner(ownerId));

        assertEquals(CommonErrorCode.DBE, exception.getErrorCode());

        verify(ownerRepository, times(1)).findByOwnerId(ownerId);
    }
}
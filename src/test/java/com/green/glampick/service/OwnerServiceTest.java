package com.green.glampick.service;

import com.green.glampick.common.CustomFileUtils;
import com.green.glampick.common.Role;
import com.green.glampick.dto.object.owner.GetRoomItem;
import com.green.glampick.entity.GlampingEntity;
import com.green.glampick.entity.GlampingWaitEntity;
import com.green.glampick.entity.OwnerEntity;
import com.green.glampick.entity.RoomEntity;
import com.green.glampick.repository.*;
import com.green.glampick.repository.resultset.GetRoomListResultSet;
import com.green.glampick.security.AuthenticationFacade;
import com.green.glampick.service.implement.OwnerServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@Import({ OwnerServiceImpl.class })
@ActiveProfiles("tdd")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Rollback(value = false)
class OwnerServiceTest {

    @Autowired private OwnerService service;
    @Autowired private OwnerRepository ownerRepository;
    @Autowired private GlampingRepository glampingRepository;
    @Autowired private GlampingWaitRepository glampingWaitRepository;
    @Autowired private RoomRepository roomRepository;
    @MockBean private AuthenticationFacade authenticationFacade;
    @MockBean private PasswordEncoder passwordEncoder;
    @MockBean private CustomFileUtils customFileUtils;
    @MockBean private RoomImageRepository roomImageRepository;
    @MockBean private ServiceRepository serviceRepository;
    @MockBean private RoomServiceRepository roomServiceRepository;
    @MockBean private ReviewRepository reviewRepository;
    @MockBean private ReviewImageRepository reviewImageRepository;
    @MockBean private ReservationBeforeRepository reservationBeforeRepository;
    @MockBean private ReservationCancelRepository reservationCancelRepository;
    @MockBean private ReservationCompleteRepository reservationCompleteRepository;
    @MockBean private RoomPriceRepository roomPriceRepository;
    @MockBean private GlampPeakRepository glampPeakRepository;


    @Test
    @DisplayName("글램핑 등록 테스트")
    void postGlampingInfo() {
        // 권한 없음
        given(authenticationFacade.getLoginUserId()).willReturn(1L);
        long ownerId1 = authenticationFacade.getLoginUserId();
        OwnerEntity owner1 = ownerRepository.getReferenceById(ownerId1);
        assertNotEquals(Role.ROLE_OWNER, owner1.getRole(), "1번 사장의 권한은 ROLE_OWNER이 아니기 때문에 일치하지 않아야 함");

        // 중복 등록
        given(authenticationFacade.getLoginUserId()).willReturn(2L);
        long ownerId2 = authenticationFacade.getLoginUserId();
        OwnerEntity owner2 = ownerRepository.getReferenceById(ownerId2);
        GlampingEntity glam2 = glampingRepository.findByOwner(owner2);
        assertNotNull(glam2, "2번 사장은 이미 글램핑을 가지고 있기 때문에 null이어야 함");

        // 정상 등록
        given(authenticationFacade.getLoginUserId()).willReturn(3L);
        long ownerId3 = authenticationFacade.getLoginUserId();
        OwnerEntity owner3 = ownerRepository.getReferenceById(ownerId3);
        assertEquals(Role.ROLE_OWNER, owner3.getRole(), "owner 권한이 들어있지 않음");
        GlampingEntity glam3 = glampingRepository.findByOwner(owner3);
        assertNull(glam3, "글램핑이 이미 등록되어있음");

        GlampingWaitEntity entity = new GlampingWaitEntity();
        entity.setOwner(owner3);
        entity.setGlampName("그린 글램핑");
        entity.setGlampLocation("대구광역시 중구 109-2");
        entity.setRegion("gyeongbuk");
        entity.setGlampImage("img");
        entity.setGlampIntro("공기 좋은 그린 글램핑입니다.");
        entity.setInfoBasic("입실은 15시부터 퇴실은 12시까지 가능합니다.");
        entity.setInfoNotice("취사를 금지합니다.");
        entity.setTraffic("해수욕장 10분");
        entity.setExclusionStatus(0);
        glampingWaitRepository.save(entity);

        // 글램핑 등록 확인
        GlampingWaitEntity savedEntity = glampingWaitRepository.findById(entity.getGlampId()).orElse(null);
        assertNotNull(savedEntity, "글램핑 등록 실패");

        verify(authenticationFacade, times(3)).getLoginUserId();
    }

    @Test
    @DisplayName("객실 불러오기 테스트")
    void getRoomList() {
        // 잘못된 권한
        given(authenticationFacade.getLoginUserId()).willReturn(1L);
        long ownerId1 = authenticationFacade.getLoginUserId();
        OwnerEntity owner1 = ownerRepository.getReferenceById(ownerId1);
        assertNotEquals(Role.ROLE_OWNER, owner1.getRole(), "1번 사장의 권한은 ROLE_OWNER 가 아니기 때문에 일치하지 않아야 함");

        // 잘못된 PK 입력
        given(authenticationFacade.getLoginUserId()).willReturn(2L);
        long ownerId2 = authenticationFacade.getLoginUserId();
        OwnerEntity owner2 = ownerRepository.getReferenceById(ownerId2);
        assertEquals(Role.ROLE_OWNER, owner2.getRole(), "owner 권한이 들어있지 않음");
        long glampIdWrong = 2;  // 2번 유저가 가진 글램핑은 1번임 > 잘못된 입력
        GlampingEntity glampWrong = glampingRepository.getReferenceById(glampIdWrong);
        assertNotEquals(owner2, glampWrong.getOwner(), "pk가 잘못 입력되어 일치하지 않아야 함");

        // 정상
        long glampId = 1;
        GlampingEntity glamping = glampingRepository.getReferenceById(glampId);
        assertEquals(owner2, glamping.getOwner(), "소유자가 일치하지 않음");
        List<GetRoomListResultSet> resultSet = roomRepository.getRoomList(glamping);
        List<GetRoomItem> result = new ArrayList<>();
        for (GetRoomListResultSet item : resultSet) {
            GetRoomItem room = new GetRoomItem(item.getRoomId(), item.getRoomName(), item.getRoomImageName());
            result.add(room);
        }
        assertEquals(result.get(0).getRoomId(), 1, "0번 인덱스의 room PK가 일치하지 않음");
        assertEquals(result.get(0).getRoomName(), "101호", "0번 인덱스의 객실 이름이 일치하지 않음");
        assertEquals(result.get(1).getRoomId(), 2, "1번 인덱스의 room PK가 일치하지 않음");
        assertEquals(result.get(1).getRoomName(), "102호", "1번 인덱스의 객실 이름이 일치하지 않음");
        assertEquals(result.get(2).getRoomId(), 3, "2번 인덱스의 room PK가 일치하지 않음");
        assertEquals(result.get(2).getRoomName(), "103호", "2번 인덱스의 객실 이름이 일치하지 않음");

        verify(authenticationFacade, times(2)).getLoginUserId();
    }

    @Test
    @DisplayName("객실 삭제 테스트")
    void deleteRoom() {
        // 잘못된 권한
        given(authenticationFacade.getLoginUserId()).willReturn(1L);
        long ownerId1 = authenticationFacade.getLoginUserId();
        OwnerEntity owner1 = ownerRepository.getReferenceById(ownerId1);
        assertNotEquals(Role.ROLE_OWNER, owner1.getRole(), "1번 사장의 권한은 ROLE_OWNER 가 아니기 때문에 일치하지 않아야 함");

        // 잘못된 PK 입력 - 글램핑
        given(authenticationFacade.getLoginUserId()).willReturn(2L);
        long ownerId2 = authenticationFacade.getLoginUserId();
        OwnerEntity owner2 = ownerRepository.getReferenceById(ownerId2);
        assertEquals(Role.ROLE_OWNER, owner2.getRole(), "owner 권한이 들어있지 않음");
        long glampIdWong = 2;   // 2번 유저가 가진 글램핑은 1번임 > 잘못된 입력
        GlampingEntity glampWrong = glampingRepository.getReferenceById(glampIdWong);
        assertNotEquals(owner2, glampWrong.getOwner(), "글램핑 pk가 잘못 입력되어 일치하지 않아야 함");

        // 잘못된 PK 입력 - 글램핑
        long glampId = 1;
        GlampingEntity glamping = glampingRepository.getReferenceById(glampId);
        assertEquals(owner2, glamping.getOwner(), "소유자가 일치하지 않음");
        long roomIdWrong = 4;   // 4번방은 1번 글램핑에 포함되어 있지 않음 > 잘못된 입력
        RoomEntity roomWrong = roomRepository.getReferenceById(roomIdWrong);
        assertNotEquals(glamping, roomWrong.getGlamp(), "객실 pk가 잘못 입력되어 일치하지 않아야 함");

        // 정상 동작
        long roomId = 2;
        RoomEntity room = roomRepository.getReferenceById(roomId);
        roomRepository.delete(room);
        assertFalse(roomRepository.existsById(roomId), "객실이 삭제되지 않았음");

        verify(authenticationFacade, times(2)).getLoginUserId();
    }

    @Test
    void patchOwnerInfo() {
        // 비로그인
        given(authenticationFacade.getLoginUserId()).willReturn(0L);
        long ownerIdWrong = authenticationFacade.getLoginUserId();
        assertEquals(0, ownerIdWrong, "로그인을 하지 않았기 때문에 0이어야 함");

        // 정상
        given(authenticationFacade.getLoginUserId()).willReturn(4L);
        long ownerId = authenticationFacade.getLoginUserId();
        assertEquals(4, ownerId, "4번 회원으로 로그인 했음");
        OwnerEntity owner = ownerRepository.getReferenceById(ownerId);
        String phone = "01012345678";
        String password = "thisisTDD!";
        owner.setOwnerPhone(phone);
        given(passwordEncoder.encode(password)).willReturn("encodedPassword");
        owner.setOwnerPw(passwordEncoder.encode(password));
        ownerRepository.save(owner);
        OwnerEntity findOwner = ownerRepository.getReferenceById(ownerId);
        assertEquals(phone, findOwner.getOwnerPhone(), "전화번호가 수정되지 않음");
        given(passwordEncoder.matches(password, "encodedPassword")).willReturn(true);
        assertTrue(passwordEncoder.matches(password, findOwner.getOwnerPw()), "비밀번호가 수정되지 않음");

        verify(authenticationFacade, times(2)).getLoginUserId();
        verify(passwordEncoder).encode(password);
        verify(passwordEncoder).matches(password, findOwner.getOwnerPw());
    }
}
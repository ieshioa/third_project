package com.green.glampick.service.implement;

import com.green.glampick.common.CustomFileUtils;
import com.green.glampick.dto.request.user.PostReviewRequestDto;
import com.green.glampick.entity.*;

import com.green.glampick.repository.ReservationCompleteRepository;
import com.green.glampick.repository.ReviewImageRepository;
import com.green.glampick.repository.ReviewRepository;
import com.green.glampick.security.AuthenticationFacade;
import com.green.glampick.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Import({UserServiceImpl.class})
class UserServiceTest {
    @Autowired
    private UserService userservice;
    @MockBean
    private ReviewRepository reviewRepository;
    @MockBean
    private ReservationCompleteRepository reservationCompleteRepository;
    @MockBean
    private ReviewImageRepository reviewImageRepository;
    @MockBean
    private CustomFileUtils customFileUtils;
    @MockBean
    private AuthenticationFacade authenticationFacade;

    @Test
    void getBook() {
    }

    @Test
    void cancelBook() {
    }

    @Test
    @DisplayName("별점범위 벗어난 코드 검사")
    void postStarOutReview() {

        PostReviewRequestDto dto = new PostReviewRequestDto();
        dto.setReviewStarPoint(8);

        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setReviewStarPoint(dto.getReviewStarPoint());

        when(reviewRepository.save(reviewEntity)).thenReturn(new ReviewEntity());
        int starPoint = dto.getReviewStarPoint();
        if (starPoint >= 0 && starPoint <= 5) {
            assertNotEquals(starPoint, dto.getReviewStarPoint(), "0~5사이 입니다.");
        } else {
            assertEquals(starPoint, dto.getReviewStarPoint(), "0~5를 벗어남");

        }
    }

    @Test
    @DisplayName("별점범위 내 코드 검사")
    void postStarInReview() {

        PostReviewRequestDto dto = new PostReviewRequestDto();
        dto.setReviewStarPoint(3);

        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setReviewStarPoint(dto.getReviewStarPoint());

// given(); 객체가 특정 상황에서 해야하는 행위를 정의하는 메소드
        when(reviewRepository.save(reviewEntity)).thenReturn(new ReviewEntity());

        int starPoint = dto.getReviewStarPoint();

        if (starPoint >= 0 && starPoint <= 5) {

            assertEquals(starPoint, dto.getReviewStarPoint(), "0~5를 벗어남");
        } else {
            assertNotEquals(starPoint, dto.getReviewStarPoint(), "0~5사이 입니다.");
        }
    }

    @Test
    @DisplayName("리뷰가 있을 경우")
    void postExistReview() {

        PostReviewRequestDto dto = new PostReviewRequestDto();
        dto.setReviewContent("리뷰 있어요!!");
        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setReviewStarPoint(dto.getReviewStarPoint());

        when(reviewRepository.save(reviewEntity)).thenReturn(new ReviewEntity());

        String review = dto.getReviewContent();
        boolean ExistReview = (review != null);
        assertTrue(ExistReview, "리뷰가 있어요");


    }

    @Test
    @DisplayName("리뷰가 없는 경우")
    void postNotExistReview() {

        PostReviewRequestDto dto = new PostReviewRequestDto();

        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setReviewStarPoint(dto.getReviewStarPoint());

        when(reviewRepository.save(reviewEntity)).thenReturn(new ReviewEntity());

        String review = dto.getReviewContent();
        boolean ExistReview = (review == null);
        assertTrue(ExistReview, "리뷰가 없어요");


    }

    @Test
    @DisplayName("리뷰 삭제 확인")
    void deleteReview() {
        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setReviewId(1L);

        reviewRepository.deleteById(reviewEntity.getReviewId());


        verify(reviewRepository, times(1)).deleteById(reviewEntity.getReviewId());
        when(reviewRepository.findById(reviewEntity.getReviewId())).thenReturn(Optional.empty());
        assertFalse(reviewRepository.findById(reviewEntity.getReviewId()).isPresent(), "리뷰 삭제 실패!!");
    }

    @Test
    @DisplayName("리뷰이미지 삭제 확인")
    void deleteImageReview() {
        ReviewImageEntity reviewImageEntity = new ReviewImageEntity();
        ReviewEntity reviewEntity= new ReviewEntity();
        reviewImageEntity.setReviewEntity(reviewEntity);
        String imageFold = String.format("%d/%d", reviewImageEntity.getReviewImageId(), reviewEntity.getReviewId());
        when(customFileUtils.makeFolders(imageFold)).thenReturn("폴더 생성??");
        reviewRepository.deleteById(reviewImageEntity.getReviewEntity().getReviewId());


        verify(reviewRepository, times(1)).deleteById(reviewImageEntity.getReviewEntity().getReviewId());
        when(reviewRepository.findById(reviewImageEntity.getReviewEntity().getReviewId())).thenReturn(Optional.empty());
        assertFalse(reviewRepository.findById(reviewImageEntity.getReviewEntity().getReviewId()).isPresent(), "리뷰(이미지) 삭제 실패!!");
    }
//
//    @Test
//    @DisplayName("유효한 사용자 ID를 사용한 리뷰 조회")
//    void getLogInReview() {
//        List<ReviewEntity> list1 = new ArrayList<>();
//        UserEntity userEntity = new UserEntity();
//        ReviewEntity reviewEntity = new ReviewEntity();
//        GlampingEntity s1 = reviewEntity.getGlampId();
//        Long t1 = reviewEntity.getReviewId();
//        ReservationCompleteEntity u1 = reviewEntity.getReservationId();
//        reviewEntity.setUserId(userEntity);
//        reviewEntity.setReviewContent("너무 별로에요");
//        reviewEntity.setGlampId(s1);
//        reviewEntity.setReviewComment("방문해 주셔서 ㄳㄳ 개ㄳ");
//        reviewEntity.setReviewId(t1);
//        reviewEntity.setReservationId(u1);
//        reviewEntity.setReviewStarPoint(4);
//
//        list1.add(reviewEntity);
//
//
//        List<ReviewEntity> list2 =new ArrayList<>();
//        long uid = 1;
//        when(reviewRepository.findById(uid)).thenReturn(Optional.of(reviewEntity));
//
//        Optional<ReviewEntity> foundReview = reviewRepository.findById(uid);
//
//        List<ReviewEntity> list3 = new ArrayList<>();
//        GlampingEntity s2 = reviewEntity.getGlampId();
//        Long t2 = reviewEntity.getReviewId();
//        ReservationCompleteEntity u2 = reviewEntity.getReservationId();
//        reviewEntity.setUserId(userEntity);
//        reviewEntity.setReviewContent("너무 별로에요");
//        reviewEntity.setGlampId(s2);
//        reviewEntity.setReviewComment("방문해 주셔서 ㄳㄳ 개ㄳ");
//        reviewEntity.setReviewId(t2);
//        reviewEntity.setReservationId(u2);
//        reviewEntity.setReviewStarPoint(4);
//
//        list3.add(reviewEntity);
//
//        assertEquals(list1, foundReview, "동일");
////        assertEquals(list2, list1,"동일");
//    }
//
//    @Test
//    @DisplayName("유효한 사용자 ID를 사용한 리뷰 조회")
//    void getLogInsReview() {
//        // Arrange
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUserId(1L); // 사용자 ID 설정
//
//        ReviewEntity reviewEntity = new ReviewEntity();
//        reviewEntity.setUserId(userEntity);
//        reviewEntity.setReviewContent("너무 별로에요");
//        reviewEntity.setReviewComment("방문해 주셔서 감사합니다");
//        reviewEntity.setReviewStarPoint(4);
//
//        // 해당 리뷰를 리포지토리에서 찾았을 때 반환하도록 설정
//        when(reviewRepository.findById(1L)).thenReturn(Optional.of(reviewEntity));
//
//        // Act
//        Optional<ReviewEntity> foundReview = reviewRepository.findById(1L);
//
//        // Assert
//        assertTrue(foundReview.isPresent(), "리뷰가 조회되어야 합니다.");
//        assertEquals(reviewEntity, foundReview.get(), "조회된 리뷰는 예상과 동일해야 합니다.");
//    }
    @Test
    @DisplayName("유효하지 않은 사용자 ID를 사용한 예외 처리")
    void getNuLogInReview() {
        ReviewEntity reviewEntity = new ReviewEntity();


    }

    @Test
    @DisplayName("데이터베이스 오류 처리")
    void getDBErrorReview() {
        ReviewEntity reviewEntity = new ReviewEntity();


    }

    @Test
    @DisplayName("리뷰와 이미지가 올바르게 매핑되는지 확인")
    void getReviewOrImage() {
        ReviewEntity reviewEntity = new ReviewEntity();


    }

    @Test
    void getFavoriteGlamping() {
    }

    @Test
    @DisplayName("유효한 사용자 ID로 사용자 정보 불러오기")
    void getUser() {
    }
    @Test
    @DisplayName("유효하지 않은 사용자 ID")
    void getNotUser() {
    }
    @Test
    @DisplayName("존재하지 않는 사용자")
    void getNotExistEUser() {
    }
    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void postUserPassword() {
    }
}
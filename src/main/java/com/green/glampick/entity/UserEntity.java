package com.green.glampick.entity;

import com.green.glampick.common.Role;
import com.green.glampick.common.RoleConverter;
import com.green.glampick.dto.request.login.SignUpRequestDto;
import com.green.glampick.dto.request.user.GetBookRequestDto;
import com.green.glampick.dto.request.user.UpdateUserRequestDto;
import com.green.glampick.security.SignInProviderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity extends UpdatedAt {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Comment("유저 ID")
    private Long userId;

    @Column(length = 50, nullable = false, unique = true) @Comment("유저 이메일")
    private String userEmail;

    @Column(length = 70, nullable = false) @Comment("쿠폰 ID")
    private String userPw;

    @Column(length = 50, nullable = false) @Comment("유저 실명")
    private String userName;

    @Column(length = 30, nullable = false, unique = true) @Comment("유저 닉네임")
    private String userNickname;

    @Column(length = 11) @Comment("유저 휴대폰 번호")
    private String userPhone;

    @Column(length = 200) @Comment("유저 프로필 이미지")
    private String userProfileImage;

    @Column(length = 20, nullable = false) @Convert(converter = RoleConverter.class) @Comment("유저 권한")
    private Role role;

    @Column(length = 50) @Comment("소셜 유저 ID")
    private String providerId;

    @Column(length = 10, nullable = false) @Enumerated(value = EnumType.STRING) @Comment("소셜 로그인 타입")
    private SignInProviderType userSocialType;

    @Column(columnDefinition = "TINYINT") @ColumnDefault("1") @Comment("회원 탈퇴시 -1")
    private Integer activateStatus;

}

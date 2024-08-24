package com.green.glampick.entity;

import com.green.glampick.common.Role;
import com.green.glampick.common.RoleConverter;
import com.green.glampick.dto.request.login.SignUpRequestDto;
import com.green.glampick.dto.request.user.UpdateUserRequestDto;
import com.green.glampick.security.SignInProviderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter
@Setter
@Entity
@Table(name = "admin")
public class AdminEntity extends CreatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Comment("관리자 PK")
    private Long adminIdx;

    @Column(length = 50, nullable = false, unique = true) @Comment("관리자 ID")
    private String adminId;

    @Column(length = 70, nullable = false) @Comment("관리자 패스워드")
    private String adminPw;

    @Column(length = 50, nullable = false) @Comment("관리자 이름")
    private String adminName;

    @Column(length = 22) @Comment("휴대폰 번호")
    private String adminPhone;

    @Column(nullable = false) @Comment("유저 권한") @Convert(converter = RoleConverter.class)
    private Role role;


}
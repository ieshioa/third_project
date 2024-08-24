package com.green.glampick.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name = "service")
public class ServiceEntity extends CreatedAt {
    //서비스시설 테이블
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Comment("서비스 ID")
    private Long serviceId;

    @Column(length = 20, nullable = false) @Comment("서비스 이름")
    private String serviceTitle;

}

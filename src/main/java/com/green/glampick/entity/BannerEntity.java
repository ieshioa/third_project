package com.green.glampick.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "banner")
@Table(name = "banner")
public class BannerEntity extends CreatedAt{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bannerId;

    @Column(length = 100, unique = true) @Comment("배너 이미지 URL")
    private String bannerUrl;

}

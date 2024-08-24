package com.green.glampick.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "glamp_favorite", uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"user_id","glamp_id"}
)})
public class GlampFavoriteEntity extends CreatedAt {
    //관심 글램핑 테이블
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("관심글램핑 ID")
    private Long glampFavoriteId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Comment("유저 ID")
    private UserEntity user;  //유저 ID

    @ManyToOne
    @JoinColumn(name = "glamp_id", nullable = false)
    @Comment("글램핑 ID")
    private GlampingEntity glamping;  //글램핑 ID

}

package com.green.glampick.repository;

import com.green.glampick.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// UserEntity - DB 테이블과 매핑되는 Entity Class
// Long - Entity 의 Primary Key Type
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUserEmail(String userEmail);
    boolean existsByUserNickname(String userNickname);
    boolean existsByUserPhone(String userPhone);

    UserEntity findByUserEmail(String userEmail);
    UserEntity findByProviderId(String providerId);
    UserEntity findByUserId(Long userId);
    UserEntity findByUserPhone(String userPhone);
    UserEntity findByUserNameAndUserPhone(String userName, String userPhone);
    UserEntity findByUserEmailAndUserName(String userEmail, String userName);


}

package com.green.glampick.repository;

import com.green.glampick.entity.BannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface BannerRepository extends JpaRepository<BannerEntity, Long> {

    BannerEntity findByBannerId(Long bannerId);

}

package com.green.glampick.dto.request.admin.module;

import com.green.glampick.common.CustomFileUtils;
import com.green.glampick.entity.BannerEntity;
import com.green.glampick.entity.OwnerEntity;
import com.green.glampick.exception.CustomException;
import com.green.glampick.exception.errorCode.OwnerErrorCode;
import com.green.glampick.repository.BannerRepository;
import com.green.glampick.repository.OwnerRepository;

import java.io.File;

public class AdminModule {

    // 배너 파일 삭제
    public static void deleteBannerImage(Long bannerId, BannerRepository bannerRepository, CustomFileUtils customFileUtils) {
        BannerEntity bannerEntity = bannerRepository.getReferenceById(bannerId);
        String dbName = bannerEntity.getBannerUrl();
        try {
            String filePath = dbName.substring(5);
            File file = new File(String.format("%s%s", customFileUtils.uploadPath, filePath));
            file.delete();
        } catch (Exception e) {
            throw new CustomException(OwnerErrorCode.CF);
        }
        bannerRepository.delete(bannerEntity);
    }

    // 사업자등록증 파일 삭제
    public static void deleteBusinessImage(Long ownerId, OwnerRepository ownerRepository, CustomFileUtils customFileUtils) {
        OwnerEntity ownerEntity = ownerRepository.getReferenceById(ownerId);
        String dbName = ownerEntity.getBusinessPaperImage();
        try {
            String filePath = dbName.substring(5);
            File file = new File(String.format("%s%s", customFileUtils.uploadPath, filePath));
            file.delete();
        } catch (Exception e) {
            throw new CustomException(OwnerErrorCode.CF);
        }
        ownerRepository.delete(ownerEntity);
    }

}

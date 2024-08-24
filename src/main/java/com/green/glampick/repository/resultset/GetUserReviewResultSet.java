package com.green.glampick.repository.resultset;

import com.green.glampick.entity.ReviewImageEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface GetUserReviewResultSet {

     String getGlampName();
     String getRoomName();
     String getUserNickname();
     String getUserProfileImage();
//     long getReviewId();
     Long getReviewId();
//     long getReservationId();
     Long getReservationId();
     String getReviewContent();
     int getReviewStarPoint();
     String getOwnerReviewComment();
     LocalDateTime getCreatedAt();
//     long getBookId();
     Long getBookId();
//     long getGlampId();
     Long getGlampId();

}

package com.green.glampick.dto.object;

import com.green.glampick.common.CustomFileUtils;
import com.green.glampick.repository.*;
import com.green.glampick.security.AuthenticationFacade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Repository {
    private final AuthenticationFacade authenticationFacade;
    private final CustomFileUtils customFileUtils;
    private final PasswordEncoder passwordEncoder;
    private final GlampingWaitRepository glampingWaitRepository;
    private final GlampingRepository glampingRepository;
    private final OwnerRepository ownerRepository;
    private final RoomRepository roomRepository;
    private final RoomImageRepository roomImageRepository;
    private final ServiceRepository serviceRepository;
    private final RoomServiceRepository roomServiceRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ReservationBeforeRepository reservationBeforeRepository;
    private final ReservationCancelRepository reservationCancelRepository;
    private final ReservationCompleteRepository reservationCompleteRepository;
    private final RoomPriceRepository roomPriceRepository;
    private final GlampPeakRepository glampPeakRepository;
}

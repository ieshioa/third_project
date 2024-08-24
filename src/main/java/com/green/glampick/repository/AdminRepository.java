package com.green.glampick.repository;

import com.green.glampick.common.Role;
import com.green.glampick.entity.AdminEntity;
import com.green.glampick.entity.OwnerEntity;
import com.green.glampick.repository.resultset.GetAccessGlampingListResultSet;
import com.green.glampick.repository.resultset.GetAccessOwnerSignUpListResultSet;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface AdminRepository extends Repository<AdminEntity, Long> {

    AdminEntity findByAdminId(String adminId);

    @Query( value = "select oe.owner_name AS ownerName, oe.business_number AS businessNumber" +
            ", oe.owner_id AS ownerId, ge.glamp_id AS glampId, ge.glamp_name AS glampName, ge.region AS region " +
            "from owner oe inner join glamping_wait ge on oe.owner_id = ge.owner_id where ge.exclusion_status = 0", nativeQuery = true )
    List<GetAccessGlampingListResultSet> getAccessGlampingList();

    @Query( value = "select oe.ownerId AS ownerId, oe.ownerName AS ownerName from OwnerEntity oe WHERE oe.role = 'ROLE_RESERVE_OWNER' " )
    List<GetAccessOwnerSignUpListResultSet> getAccessOwnerSignUpList();

}

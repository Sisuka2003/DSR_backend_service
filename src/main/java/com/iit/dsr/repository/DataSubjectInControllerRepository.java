package com.iit.dsr.repository;

import com.iit.dsr.entity.DataControllerEntity;
import com.iit.dsr.entity.DataSubjectInOrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSubjectInControllerRepository extends JpaRepository<DataSubjectInOrganizationEntity, Integer> {


    @Query("SELECT DSIO FROM DataSubjectInOrganizationEntity DSIO WHERE DSIO.dsCode.id = ?1 AND DSIO.dcCode.id = ?2 AND DSIO.status.code = ?3")
    DataSubjectInOrganizationEntity getCustomerRecordFromOrganization(int dsCode, int dcCode,String statusCode);
}

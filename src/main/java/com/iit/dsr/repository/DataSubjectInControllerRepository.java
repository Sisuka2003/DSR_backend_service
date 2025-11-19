package com.iit.dsr.repository;

import com.iit.dsr.entity.DataControllerEntity;
import com.iit.dsr.entity.DataSubjectInOrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSubjectInControllerRepository extends JpaRepository<DataSubjectInOrganizationEntity, Integer> {


    @Query("SELECT DSIO FROM DataSubjectInOrganizationEntity DSIO WHERE DSIO.dsCode.id = ?1 AND DSIO.dcCode.id = ?2 AND DSIO.status.code = ?3")
    DataSubjectInOrganizationEntity getCustomerRecordFromOrganization(int dsCode, int dcCode,String statusCode);


    @Modifying
    @Query("UPDATE DataSubjectInOrganizationEntity DSIO SET DSIO.collectedData =?1")
    int updateCollectedDataOfDataSubject(String collectedData);

    @Modifying
    @Query("DELETE FROM DataSubjectInOrganizationEntity DSIO WHERE DSIO.id =?1")
    int deleteCollectedDataOfDataSubject(int id);
}

package com.iit.dsr.repository;

import com.iit.dsr.entity.DataControllerEntity;
import com.iit.dsr.entity.DataSubjectInOrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataSubjectInControllerRepository extends JpaRepository<DataSubjectInOrganizationEntity, Integer> {


    @Query("SELECT DSIO FROM DataSubjectInOrganizationEntity DSIO WHERE DSIO.dsCode.id = ?1 AND DSIO.dcCode.id = ?2 AND DSIO.status.code = ?3")
    DataSubjectInOrganizationEntity getCustomerRecordFromOrganization(int dsCode, int dcCode,String statusCode);


    @Modifying
    @Query("UPDATE DataSubjectInOrganizationEntity DSIO SET DSIO.backupData = DSIO.collectedData, DSIO.collectedData =?1 WHERE DSIO.dsCode.id =?2 AND DSIO.dcCode.id =?3 AND DSIO.status.id =?4")
    int updateCollectedDataOfDataSubject(String collectedData,Integer dsCode, Integer dcCode, Integer statusCode);

    @Modifying
    @Query("UPDATE DataSubjectInOrganizationEntity DSIO SET DSIO.status.id = ?3 WHERE DSIO.dsCode.id =?1 AND DSIO.dcCode.id =?2")
    int deleteCollectedDataOfDataSubject(Integer dsCode, Integer dcCode, Integer statusCode);


    @Query("SELECT DSIO FROM DataSubjectInOrganizationEntity DSIO WHERE DSIO.dcCode.id = ?1 AND DSIO.status.code = ?2")
    List<DataSubjectInOrganizationEntity> getCustomerRecordFromOrganizationMapped(int dcCode, String statusCode);
}

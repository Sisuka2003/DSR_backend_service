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
    @Query("UPDATE DataSubjectInOrganizationEntity DSIO SET DSIO.collectedData =?1, DSIO.backupData='', DSIO.activityStatus.id =?2 WHERE DSIO.dsCode.id =?3 AND DSIO.dcCode.id =?4 AND DSIO.status.id =?5")
    int updateDataSubjectDataOnApproval(String collectedData, Integer approvedStatus,Integer dsCode, Integer dcCode, Integer statusCode);

    @Modifying
    @Query("UPDATE DataSubjectInOrganizationEntity DSIO SET DSIO.collectedData = DSIO.backupData, DSIO.backupData ='', DSIO.activityStatus.id = ?1 WHERE DSIO.dsCode.id =?2 AND DSIO.dcCode.id =?3 AND DSIO.status.id =?4")
    int rejectDataSubjectDataModificationOrDeletionRequest(Integer rejectedCode, Integer dsCode, Integer dcCode, Integer statusCode);

    @Modifying
    @Query("UPDATE DataSubjectInOrganizationEntity DSIO SET DSIO.status.id = ?3 WHERE DSIO.dsCode.id =?1 AND DSIO.dcCode.id =?2")
    int deleteCollectedDataOfDataSubject(Integer dsCode, Integer dcCode, Integer statusCode);

    @Query("SELECT DSIO FROM DataSubjectInOrganizationEntity DSIO WHERE DSIO.dcCode.id = ?1 AND DSIO.status.code = ?2")
    List<DataSubjectInOrganizationEntity> getCustomerRecordFromOrganizationMapped(int dcCode, String statusCode);

    @Query("SELECT DSIO FROM DataSubjectInOrganizationEntity DSIO WHERE DSIO.dsCode.id = ?1 AND DSIO.dcCode.id = ?2 AND (DSIO.activityStatus.code = ?3 OR DSIO.activityStatus.code = ?4)")
    List<DataSubjectInOrganizationEntity> checkForPendingRequestsByDataSubject(int dsCode, int dcCode, String pendingStatusCode, String queuedStatusCode);
}

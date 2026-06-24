package com.iit.dsr.repository;

import com.iit.dsr.entity.DataControllerEntity;
import com.iit.dsr.entity.DataSubjectInOrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface DataSubjectInControllerRepository extends JpaRepository<DataSubjectInOrganizationEntity, Integer> {


    @Query("SELECT DSIO FROM DataSubjectInOrganizationEntity DSIO WHERE DSIO.dsCode.id = ?1 AND DSIO.dcCode.id = ?2 AND DSIO.status.code = ?3 AND (DSIO.activityStatus.code = ?3 OR DSIO.adminActivityStatus.code = ?4)")
    DataSubjectInOrganizationEntity getCustomerRecordFromOrganization(int dsCode, int dcCode,String statusCode,String agentApproved, String adminApproved);

    @Query("SELECT DSIO FROM DataSubjectInOrganizationEntity DSIO WHERE DSIO.dsCode.id = ?1 AND DSIO.dcCode.id = ?2 AND DSIO.status.code = ?3 AND (DSIO.activityStatus.code = ?3 OR DSIO.adminActivityStatus.code = ?4)")
    List<DataSubjectInOrganizationEntity> getDataSubjectLoginData(int dsCode, int dcCode,String statusCode,String agentApproved, String adminApproved);

    @Query("SELECT DSIO FROM DataSubjectInOrganizationEntity DSIO WHERE DSIO.dsCode.id = ?1 AND DSIO.dcCode.id = ?2 AND DSIO.status.code = ?3 AND DSIO.adminActivityStatus.code = ?4")
    DataSubjectInOrganizationEntity getDataONlyAdminApprovalsArePassed(int dsCode, int dcCode,String statusCode,String adminApproved);

//    @Modifying
//    @Query("UPDATE DataSubjectInOrganizationEntity DSIO SET DSIO.backupData = DSIO.collectedData, DSIO.collectedData =?1 WHERE DSIO.dsCode.id =?2 AND DSIO.dcCode.id =?3 AND DSIO.status.id =?4")
//    int updateCollectedDataOfDataSubject(String collectedData,Integer dsCode, Integer dcCode, Integer statusCode);

    @Modifying
    @Query("UPDATE DataSubjectInOrganizationEntity DSIO SET DSIO.collectedData =?1, DSIO.backupData='', DSIO.adminActivityStatus.id =?2, DSIO.activityStatus.id =?3 WHERE DSIO.id =?4 AND DSIO.status.id =?5")
    int updateDataSubjectDataOnApprovalSkipped(String collectedData, Integer approvedStatus, Integer skippedStatus, Integer dsioId, Integer statusCode);

    @Modifying
    @Query("UPDATE DataSubjectInOrganizationEntity DSIO SET DSIO.collectedData =?1, DSIO.backupData='', DSIO.adminActivityStatus.id =?2 WHERE DSIO.id =?3 AND DSIO.status.id =?4")
    int updateDataSubjectDataOnApproval(String collectedData, Integer approvedStatus, Integer dsioId, Integer statusCode);

    @Modifying
    @Query("UPDATE DataSubjectInOrganizationEntity DSIO SET DSIO.collectedData = DSIO.backupData, DSIO.backupData ='', DSIO.adminActivityStatus.id = ?1, DSIO.activityStatus.id =?2,DSIO.subjectActivityStatus.id =?3 WHERE DSIO.id=?4")
    int rejectDataSubjectDataModificationOrDeletionRequestForUserOnly(Integer skippedAdminCode, Integer skippedAgentCode, Integer rejectedUserCode, Integer recordId);

    @Modifying
    @Query("UPDATE DataSubjectInOrganizationEntity DSIO SET DSIO.collectedData = DSIO.backupData, DSIO.backupData ='', DSIO.adminActivityStatus.id = ?1, DSIO.activityStatus.id =?2 WHERE DSIO.id=?3 AND DSIO.status.id =?4")
    int rejectDataSubjectDataModificationOrDeletionRequest(Integer rejectedCode, Integer skippedCode, Integer recordId, Integer statusCode);

    @Modifying
    @Query("UPDATE DataSubjectInOrganizationEntity DSIO SET DSIO.status.id = ?3 WHERE DSIO.dsCode.id =?1 AND DSIO.dcCode.id =?2")
    int deleteCollectedDataOfDataSubject(Integer dsCode, Integer dcCode, Integer statusCode);

    @Query("SELECT DSIO FROM DataSubjectInOrganizationEntity DSIO WHERE DSIO.dcCode.id = ?1 AND DSIO.status.code = ?2 AND DSIO.subjectActivityStatus.id != 5")
    List<DataSubjectInOrganizationEntity> getCustomerRecordFromOrganizationMapped(int dcCode, String statusCode);

    @Query("SELECT DSIO FROM DataSubjectInOrganizationEntity DSIO WHERE DSIO.dcCode.id = ?1 AND DSIO.status.code = ?2 AND DSIO.agentCode IS NULL AND DSIO.subjectActivityStatus.id != 5 AND DSIO.activityStatus.code =?3 AND DSIO.adminActivityStatus.code =?3")
    List<DataSubjectInOrganizationEntity> getCustomerRecordFromOrganizationMappedTask(int dcCode, String statusCode, String pendingStatusCode);

    @Query("SELECT DSIO FROM DataSubjectInOrganizationEntity DSIO WHERE DSIO.agentCode.id = ?1 AND DSIO.dcCode.id = ?2 AND DSIO.status.code = ?3 AND DSIO.subjectActivityStatus.id != 5")
    List<DataSubjectInOrganizationEntity> getCustomerRecordFromAgentMapped(int agentCode,int dcCode, String statusCode);

    @Query("SELECT DSIO FROM DataSubjectInOrganizationEntity DSIO WHERE DSIO.agentCode.id = ?1 AND DSIO.dcCode.id = ?2 AND DSIO.status.code = ?3 AND DSIO.subjectActivityStatus.id != 5 AND DSIO.activityStatus.code =?4 AND DSIO.adminActivityStatus.code =?4")
    List<DataSubjectInOrganizationEntity> getCustomerRecordFromAgentMappedTask(int agentCode,int dcCode, String statusCode, String pendingStatusCode);

    @Query("SELECT DSIO FROM DataSubjectInOrganizationEntity DSIO WHERE DSIO.dsCode.id = ?1 AND DSIO.dcCode.id=?2 AND DSIO.status.code = ?3")
    List<DataSubjectInOrganizationEntity> getCustomerRecordFromCustomerMapped(int dsCode, int dcCode, String statusCode);

    @Query("SELECT DSIO FROM DataSubjectInOrganizationEntity DSIO WHERE DSIO.dsCode.id = ?1 AND DSIO.dcCode.id = ?2 AND (DSIO.activityStatus.code = ?3 OR DSIO.activityStatus.code = ?4) ")
    DataSubjectInOrganizationEntity checkForPendingRequestsByDataSubject(int dsCode, int dcCode, String pendingStatusCode, String queuedStatusCode);

    @Query("SELECT DSIO FROM DataSubjectInOrganizationEntity DSIO WHERE DSIO.dsCode.id = ?1 AND DSIO.dcCode.id = ?2 AND DSIO.status.id = ?3 ")
    List<DataSubjectInOrganizationEntity> checkForRequestsByDataSubject(Integer dsCode, Integer dcCode, Integer activeStatusCode);

    @Query("SELECT COUNT(DSIO.id) FROM DataSubjectInOrganizationEntity DSIO WHERE DSIO.status.code =?1")
    int getAllRecordsCount(String statusCode);


    @Query("SELECT COUNT(DSIO.id) FROM DataSubjectInOrganizationEntity DSIO WHERE DSIO.adminActivityStatus.code =?1")
    int getAllPendingRecordsCount(String statusCode);

    @Query("SELECT COUNT(DSIO.id) FROM DataSubjectInOrganizationEntity DSIO WHERE DSIO.adminActivityStatus.code =?1")
    int getAllApprovedRecordsCount(String statusCode);

    @Query("SELECT COUNT(DSIO.id) FROM DataSubjectInOrganizationEntity DSIO WHERE DSIO.adminActivityStatus.code =?1")
    int getAllRejectedRecordsCount(String statusCode);

    @Query("SELECT COUNT(DSIO.id) FROM DataSubjectInOrganizationEntity DSIO WHERE DSIO.adminActivityStatus.code =?1")
    int getAllQueuedRecordsCount(String statusCode);

    @Modifying
    @Query("UPDATE DataSubjectInOrganizationEntity DSIO SET DSIO.agentCode.id = ?1 WHERE DSIO.id =?2 AND DSIO.status.code =?3")
    int updateAgentCodeForDataSubjectRequestRecord(Integer agentCode,Integer recordId, String activeStatusCode);

    @Query("SELECT COUNT(DSIO.id) FROM DataSubjectInOrganizationEntity DSIO WHERE DSIO.agentCode.id=?1 AND DSIO.status.code =?2")
    int findRecordsForAgentId(Integer agentId, String active);

    @Query("SELECT DSICR FROM DataSubjectInOrganizationEntity DSICR WHERE DSICR.dcCode.id =?1 AND DSICR.activityStatus.code=?2 AND DSICR.status.code=?3")
    List<DataSubjectInOrganizationEntity> getPendingDSRsFromDataController (Integer dataControllerId, String pendingStatusCode, String activeStatusCode);

    @Modifying
    @Query("""
    UPDATE DataSubjectInOrganizationEntity DSIO
    SET DSIO.status.id = ?3
    WHERE DSIO.dsCode.id = ?1
      AND DSIO.dcCode.id = ?2
      AND DSIO.id <> ?4
""")
    int deactivatePreviousRecords(
            Integer dsCode,
            Integer dcCode,
            Integer inactiveStatusId,
            Integer currentRecordId
    );

    @Modifying
    @Query("UPDATE " +
            "DataSubjectInOrganizationEntity DSIO " +
            "SET " +
            "DSIO.status.id = 2, " +
            "DSIO.activityStatus.id = 8, " +
            "DSIO.adminActivityStatus.id = 8, " +
            "DSIO.subjectActivityStatus.id = 8 " +
            "WHERE " +
            "DSIO.id= ?1 " +
            "AND " +
            "DSIO.adminActivityStatus.code = 'PEND'")
    int updateRecordToExpiredWithId(Integer id);
}

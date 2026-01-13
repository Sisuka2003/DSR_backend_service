package com.iit.dsr.repository;

import com.iit.dsr.entity.DataControllerEntity;
import com.iit.dsr.entity.DataSubjectsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSubjectRepository extends JpaRepository<DataSubjectsEntity,Integer> {

    @Query("SELECT DSE FROM DataSubjectsEntity DSE WHERE DSE.nicNumber = ?1")
    DataSubjectsEntity getCustomerRecordFromNIC(String nicNumber);

    @Query("SELECT DSE FROM DataSubjectsEntity DSE WHERE DSE.emailAddress = ?1")
    DataSubjectsEntity getCustomerRecordFromEMAIL(String emailAddress);

    @Query("SELECT DSE FROM DataSubjectsEntity DSE WHERE DSE.mobileNumber = ?1")
    DataSubjectsEntity getCustomerRecordFromMOBILE(String mobileNumber);

    @Query("SELECT DSE FROM DataSubjectsEntity DSE WHERE DSE.customerId = ?1")
    DataSubjectsEntity getCustomerRecordFromCUSTOMERID(String customerId);

    @Modifying
    @Query("UPDATE DataSubjectsEntity DSE SET DSE.otpCode=?1 WHERE DSE.id=?2 AND DSE.status.code=?3 AND DSE.isVerified=0")
    int updateTheOtpCodeColumnIfUserVerificationIsFalse(String otpCode,Integer dsCode, String statusCode);

    @Query("SELECT DSE FROM DataSubjectsEntity DSE WHERE DSE.id=?1 AND DSE.status.code=?2")
    DataSubjectsEntity findDataSubjectByIdAndActiveStatus(Integer dsCode, String statusCode);
}

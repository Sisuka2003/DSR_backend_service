package com.iit.dsr.repository;

import com.iit.dsr.entity.DataControllerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DataControllerRepository extends JpaRepository<DataControllerEntity,Integer> {

    @Query("SELECT DCE FROM DataControllerEntity DCE WHERE DCE.orgStatus.code = ?1")
    List<DataControllerEntity> getAllActiveDataControllers(String status);

    @Query("SELECT DCE FROM DataControllerEntity DCE WHERE DCE.id=?1 AND DCE.orgStatus.code = ?2")
    DataControllerEntity getControllerData(Integer dcCode, String statusCode);

    @Query("SELECT DCE FROM DataControllerEntity DCE WHERE (DCE.orgUsername=?1 AND DCE.orgPassword=?2) AND DCE.orgStatus.code = ?3 ")
    DataControllerEntity getOrganizationByUsernameAndPassword(String username,String password,String status);

    Optional<DataControllerEntity> findById(int id);


    @Modifying
    @Query("UPDATE DataControllerEntity DCE SET DCE.orgStatus.id=?2 WHERE DCE.id =?1")
    int deleteControllerRecord(Integer dcCode, Integer StatusCode);

    @Modifying
    @Query("UPDATE DataControllerEntity DCE SET DCE.orgUsername = ?1, DCE.orgPassword = ?2  WHERE DCE.id = ?3 AND DCE.orgStatus.code=?4")
    int updateDataController(String username, String password, Integer dcCode,String statusCode);


    @Query("SELECT COUNT(DCE.id) FROM DataControllerEntity DCE WHERE DCE.orgStatus.code =?1")
    int getAllActiveDataControllerCount(String statusCode);

}

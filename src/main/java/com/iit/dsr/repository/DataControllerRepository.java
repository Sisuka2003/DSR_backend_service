package com.iit.dsr.repository;

import com.iit.dsr.entity.DataControllerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DataControllerRepository extends JpaRepository<DataControllerEntity,Integer> {

    @Query("SELECT DCE FROM DataControllerEntity DCE WHERE DCE.orgStatus.code = ?1")
    List<DataControllerEntity> getAllActiveDataControllers(String status);

    @Query("SELECT DCE FROM DataControllerEntity DCE WHERE (DCE.orgUsername=?1 AND DCE.orgPassword=?2) AND DCE.orgStatus.code = ?3 ")
    DataControllerEntity getOrganizationByUsernameAndPassword(String username,String password,String status);

    Optional<DataControllerEntity> findById(int id);

}

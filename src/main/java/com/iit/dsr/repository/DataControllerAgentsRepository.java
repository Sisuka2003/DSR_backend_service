package com.iit.dsr.repository;

import com.iit.dsr.entity.DataControllerAgentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataControllerAgentsRepository extends JpaRepository<DataControllerAgentsEntity,Integer> {

    @Query("SELECT DCAE FROM DataControllerAgentsEntity DCAE WHERE DCAE.username=?1 AND DCAE.password=?2 AND DCAE.status.code=?3")
    DataControllerAgentsEntity checkAgentAvailability(String username, String password, String active);

    @Modifying
    @Query("UPDATE DataControllerAgentsEntity DCAE SET DCAE.isLoggedIn=?1 WHERE DCAE.id=?2")
    int updateAdminsLoggedStatusToTrue(Integer isLoggedIn,Integer adminId);

    @Modifying
    @Query("UPDATE DataControllerAgentsEntity DCAE SET DCAE.username=?1, DCAE.password=?2 WHERE DCAE.id=?3")
    int updateAgentRecord(String username,String password, Integer agentId);

    @Modifying
    @Query("UPDATE DataControllerAgentsEntity DCAE SET DCAE.status.id=?1,DCAE.isLoggedIn = 0 WHERE DCAE.id=?2")
    int deactivateAgentRecord(Integer deactAgentStatus, Integer agentId);

    @Query("SELECT DCAE FROM DataControllerAgentsEntity DCAE WHERE DCAE.dataController.id =?1 AND DCAE.status.code=?2")
    List<DataControllerAgentsEntity> getAllAgentsFromActiveStatus(Integer dcCode,String activeStatusCode);

    @Modifying
    @Query("UPDATE DataControllerAgentsEntity DCAE SET DCAE.notifications = ?1 WHERE DCAE.id = ?2 AND DCAE.status.code = ?3")
    int updateAgentNotificationCount(Integer count,Integer agentId, String activeStatusCode);
}

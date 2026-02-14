package com.iit.dsr.repository;

import com.iit.dsr.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity,Integer> {

    @Query("SELECT ADM FROM AdminEntity ADM WHERE ADM.username=?1 AND ADM.password=?2")
    AdminEntity getAdminFromUsernameAndPassword(String username, String password);


    @Modifying
    @Query("UPDATE AdminEntity ADM SET ADM.firstName=?1, ADM.lastName=?2, ADM.username=?3, ADM.password =?4 WHERE ADM.id=?5")
    int updateAdminData(String fname,String lname, String username, String password, Integer id);

    @Query("SELECT COUNT(ADM.id) FROM AdminEntity ADM WHERE ADM.status.code=?1")
    int getAllRecordsCount(String statusCode);



}

package com.iit.dsr.repository;

import com.iit.dsr.entity.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<StatusEntity,Integer> {

    @Query("SELECT SE FROM StatusEntity SE WHERE SE.code =?1")
    StatusEntity getStatusRecordFromCode(String statusCode);
}

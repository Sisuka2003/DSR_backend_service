package com.iit.dsr.repository;

import com.iit.dsr.entity.IdentificationKeyTypesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeyIdenticationKeyRepository extends JpaRepository<IdentificationKeyTypesEntity, Integer> {

    @Query("SELECT IKTE FROM IdentificationKeyTypesEntity IKTE")
    List<IdentificationKeyTypesEntity> getAllActiveIdentifiers();
}

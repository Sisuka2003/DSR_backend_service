package com.iit.dsr.repository;

import com.iit.dsr.entity.DataSubjectsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<DataSubjectsEntity, Integer> {
}

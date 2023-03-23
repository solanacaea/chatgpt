package com.paic.gpt.repository;

import com.paic.gpt.model.LimitConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LimitConfigRepo extends JpaRepository<LimitConfig, Long> {
    List<LimitConfig> findByConfigType(String type);

}

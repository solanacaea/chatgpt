package com.paic.gpt.repository;

import com.paic.gpt.model.GptAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GptAuditRepo extends JpaRepository<GptAudit, Long> {
    List<GptAudit> findByUsername(String username);
}

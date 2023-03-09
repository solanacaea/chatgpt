package com.paic.gpt.repository;

import com.paic.gpt.model.Role;
import com.paic.gpt.model.RoleCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByCode(RoleCode roleCode);
}

package com.paic.gpt.repository;

import com.paic.gpt.model.GptUserReqTrace;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReqTraceRepository extends JpaRepository<GptUserReqTrace, Long> {

    Optional<GptUserReqTrace> findById(Long pollId);

    Optional<GptUserReqTrace> findByUser(String user);

//    Page<GptUserReqTrace> findByCreatedBy(Long userId, Pageable pageable);

//    long countByCreatedBy(Long userId);

    List<GptUserReqTrace> findByIdIn(List<Long> pollIds);

    List<GptUserReqTrace> findByIdIn(List<Long> pollIds, Sort sort);
}

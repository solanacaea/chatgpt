package com.paic.gpt.repository;

import com.paic.gpt.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepo extends JpaRepository<Conversation, Long> {
//    Optional<Conversation> findByUsername(String username);
    List<Conversation> findByUsername(String username);

}

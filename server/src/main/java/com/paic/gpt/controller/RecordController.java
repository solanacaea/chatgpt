package com.paic.gpt.controller;

import com.paic.gpt.model.Conversation;
import com.paic.gpt.repository.ConversationRepo;
import com.paic.gpt.repository.ReqTraceRepository;
import com.paic.gpt.security.CurrentUser;
import com.paic.gpt.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RecordController {

    @Autowired
    private ReqTraceRepository reqTraceRepository;

    @Autowired
    private ConversationRepo converRepo;

    @GetMapping("/history")
    public List<Conversation> getUserHistory(@CurrentUser UserPrincipal currentUser) {
        String username = currentUser.getUsername();
        List<Conversation> list = converRepo.findByUsername(username);
        list.forEach(a -> a.setId(0L));
        return list;
    }

}

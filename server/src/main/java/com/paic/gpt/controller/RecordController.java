package com.paic.gpt.controller;

import com.paic.gpt.exception.ResourceNotFoundException;
import com.paic.gpt.model.User;
import com.paic.gpt.payload.UserProfile;
import com.paic.gpt.repository.ReqTraceRepository;
import com.paic.gpt.security.CurrentUser;
import com.paic.gpt.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RecordController {

    @Autowired
    private ReqTraceRepository reqTraceRepository;

    @GetMapping("/history")
    public UserProfile getUserHistory(@CurrentUser UserPrincipal currentUser) {
        String username = currentUser.getUsername();

        return null;
    }


}

package com.paic.gpt.service;

import com.paic.gpt.security.UserPrincipal;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public boolean checkUserDosage(UserPrincipal currentUser) {
//        currentUser.getUsername();
        return false;
    }

}

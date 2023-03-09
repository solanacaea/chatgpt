package com.paic.gpt.controller;

import com.paic.gpt.exception.ResourceNotFoundException;
import com.paic.gpt.model.User;
import com.paic.gpt.payload.UserIdentityAvailability;
import com.paic.gpt.payload.UserProfile;
import com.paic.gpt.payload.UserSummary;
import com.paic.gpt.repository.UserRepository;
import com.paic.gpt.security.UserPrincipal;
import com.paic.gpt.service.ReqTraceService;
import com.paic.gpt.security.CurrentUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReqTraceService reqTraceService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('User')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(
                currentUser.getId(), currentUser.getUsername(), currentUser.getName(),
                currentUser.getTotalCount(), currentUser.getCurrCount());
        return userSummary;
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/users/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        int currCount = reqTraceService.getUserTodayCount(username);
        int maxCount = user.getMember().getReqCount();
        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(),
                user.getCreatedAt(), currCount, maxCount);

        return userProfile;
    }

}

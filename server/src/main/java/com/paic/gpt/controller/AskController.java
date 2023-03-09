package com.paic.gpt.controller;

import com.paic.gpt.payload.ApiResponse;
import com.paic.gpt.payload.AskRequest;
import com.paic.gpt.payload.AskResponse;
import com.paic.gpt.payload.PagedResponse;
import com.paic.gpt.repository.UserRepository;
import com.paic.gpt.security.CurrentUser;
import com.paic.gpt.security.UserPrincipal;
import com.paic.gpt.service.AskService;
import com.paic.gpt.service.ReqTraceService;
import com.paic.gpt.service.UserService;
import com.paic.gpt.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/ask")
public class AskController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReqTraceService reqTraceService;

    @Autowired
    private AskService askService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AskController.class);

    @GetMapping
    public PagedResponse<AskResponse> getPolls(@CurrentUser UserPrincipal currentUser,
                                               @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                               @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return reqTraceService.getAllPolls(currentUser, page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<?> ask(@CurrentUser UserPrincipal currentUser,
                                 @Valid @RequestBody AskRequest askRequest) {

        boolean overloaded = userService.checkUserDosage(currentUser);
        if (overloaded) {
            return ResponseEntity.ok(new ApiResponse(false, "流量超出限制，明天见啦！"));
        }
        String resp = askService.ask(currentUser, askRequest);

//        URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest().path("/{pollId}")
//                .buildAndExpand(gptUserReqTrace.getId()).toUri();

        return ResponseEntity.ok(new ApiResponse(true, resp));
    }


}

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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.PushBuilder;
import javax.validation.Valid;

import static com.paic.gpt.util.AppConstants.ERROR_RESP_MSG;

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
        logger.info(currentUser.getUsername() + " 请求：" + askRequest);
        String overloaded = userService.checkUserDosage(currentUser);
        if (overloaded != null) {
            logger.info(currentUser.getUsername() + "..." + overloaded);
            return ResponseEntity.ok(
                    new ApiResponse(false, overloaded,
                            askRequest.getConversationId(),
                            askRequest.getMsgId()));
        }

        String resp;
        try {
            int currReq = userService.plus();
//            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//            HttpServletRequest httpReq = attr.getRequest();
//            PushBuilder pushBuilder = httpReq.newPushBuilder();
//            pushBuilder.path("/answer")
//                    .addHeader("content-type", "text")
//                    .push();

            resp = askService.ask(currentUser, askRequest);
        } catch (Exception e) {
            resp = ERROR_RESP_MSG;
        } finally {
            int currReq = userService.minus();
            logger.info("当前并行请求数量：" + currReq);
        }

//        URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest().path("/{pollId}")
//                .buildAndExpand(gptUserReqTrace.getId()).toUri();

        return ResponseEntity.ok(
                new ApiResponse(true, resp,
                        askRequest.getConversationId(),
                        askRequest.getMsgId()));
    }


}

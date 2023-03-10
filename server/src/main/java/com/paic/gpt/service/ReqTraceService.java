package com.paic.gpt.service;

import com.paic.gpt.exception.BadRequestException;
import com.paic.gpt.model.GptUserReqTrace;
import com.paic.gpt.payload.PagedResponse;
import com.paic.gpt.payload.AskResponse;
import com.paic.gpt.repository.ReqTraceDao;
import com.paic.gpt.repository.ReqTraceRepository;
import com.paic.gpt.repository.UserRepository;
import com.paic.gpt.security.UserPrincipal;
import com.paic.gpt.util.AppConstants;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Service
public class ReqTraceService {

    @Autowired
    private ReqTraceRepository reqTraceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReqTraceDao rtDao;

    private static final Logger logger = LoggerFactory.getLogger(ReqTraceService.class);

    public void syncTrace(ChatCompletionResult result, String conversionId,
                          String question, String answer, String username) {
        CompletableFuture.runAsync(() -> {
            long createdAt = result.getCreated();
            GptUserReqTrace gptUserReqTrace = GptUserReqTrace.builder()
                    .question(question)
                    .answer(answer)
                    .reqTokens((int)result.getUsage().getPromptTokens())
                    .respTokens((int)result.getUsage().getCompletionTokens())
                    .totalTokens((int)result.getUsage().getTotalTokens())
                    .count(1)
                    .gptStatus(1)
                    .conversationId(conversionId)
                    .user(username)
                    .msgId(result.getId())
                    .timeCost((int)(System.currentTimeMillis() / 1000 - createdAt))
                    .build();
            reqTraceRepository.save(gptUserReqTrace);
        });
    }

    public int getUserTodayCount(String currentUser) {
        return rtDao.getUserTodayCount(currentUser);
    }

    public PagedResponse<AskResponse> getAllPolls(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<GptUserReqTrace> polls = reqTraceRepository.findAll(pageable);

        if(polls.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), polls.getNumber(),
                    polls.getSize(), polls.getTotalElements(), polls.getTotalPages(), polls.isLast());
        }

        // Map Polls to PollResponses containing vote counts and poll creator details
//        List<Long> pollIds = polls.map(GptUserReqTrace::getId).getContent();

//        List<PollResponse> pollResponses = polls.map(gptUserReqTrace -> {
//            return ModelMapper.mapPollToPollResponse(gptUserReqTrace,
//                    choiceVoteCountMap,
//                    creatorMap.get(gptUserReqTrace.getCreatedBy()),
//                    pollUserVoteMap == null ? null : pollUserVoteMap.getOrDefault(gptUserReqTrace.getId(), null));
//        }).getContent();

        return new PagedResponse<>(null, polls.getNumber(),
                polls.getSize(), polls.getTotalElements(), polls.getTotalPages(), polls.isLast());
    }

    private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }


}

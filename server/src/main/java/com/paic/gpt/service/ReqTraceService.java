package com.paic.gpt.service;

import com.paic.gpt.exception.BadRequestException;
import com.paic.gpt.model.Conversation;
import com.paic.gpt.model.GptUserReqTrace;
import com.paic.gpt.model.UserUsage;
import com.paic.gpt.payload.PagedResponse;
import com.paic.gpt.payload.AskResponse;
import com.paic.gpt.repository.ReqTraceDao;
import com.paic.gpt.repository.ReqTraceRepository;
import com.paic.gpt.repository.UserRepository;
import com.paic.gpt.security.UserPrincipal;
import com.paic.gpt.util.AppConstants;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Service
public class ReqTraceService {

    @Autowired
    private ReqTraceRepository reqTraceRepository;

    @Autowired
    private ReqTraceDao rtDao;

    @Autowired
    private AdaModelService adaModelService;

    private static final Logger logger = LoggerFactory.getLogger(ReqTraceService.class);

    public void syncTrace(ChatCompletionResult result, String conversionId, String msgId,
                          String question, String answer, UserPrincipal user, String questionType, String model) {
        try {
            int timeCost;
            int reqToken = 0;
            int respToken = 0;
            int totalToken = 0;
            int status = 1;
            if (result == null) {
                timeCost = (int)AskService.timeout;
                status = 0;
            } else {
                reqToken = (int) result.getUsage().getPromptTokens();
                respToken = (int) result.getUsage().getCompletionTokens();
                totalToken = (int) result.getUsage().getTotalTokens();
                long createdAt = result.getCreated();
                timeCost = (int) (System.currentTimeMillis() / 1000 - createdAt);
                model = result.getModel();
            }
            int finalReqToken = reqToken;
            int finalRespToken = respToken;
            int finalTotalToken = totalToken;
            int finalStatus = status;
            String finalModel = model;
            CompletableFuture.runAsync(() -> {
                try {
                    Conversation currConv = rtDao.getUserConversationById(conversionId);
                    if (currConv == null) {
//                    String topic = adaModelService.getTopic(user, question, answer);
                        String topic;
                        if (StringUtils.length(question) > 20) {
                            topic = question.substring(0, 20);
                        } else {
                            topic = question;
                        }
                        Conversation conv = new Conversation(user.getUsername(), conversionId, topic);
                        rtDao.updateUserConversation(conv);
                    } else {
                        currConv.setUpdatedAt(new Date());
                        rtDao.updateUserConversation(currConv);
                    }
                } catch (Exception e1) {
                    logger.error("syncTrace-saveConversation异常："+ ExceptionUtils.getStackTrace(e1));
                }
                try {
                    GptUserReqTrace gptUserReqTrace = GptUserReqTrace.builder()
                            .question(question)
                            .answer(answer)
                            .reqTokens(finalReqToken)
                            .respTokens(finalRespToken)
                            .totalTokens(finalTotalToken)
                            .count(1)
                            .gptStatus(finalStatus)
                            .conversationId(conversionId)
                            .user(user.getUsername())
                            .msgId(msgId)
                            .timeCost(timeCost)
                            .model(finalModel)
                            .questionType(questionType)
                            .build();
                    reqTraceRepository.save(gptUserReqTrace);
                } catch (Exception e) {
                    logger.error("syncTrace-saveGptUserReqTrace异常："+ ExceptionUtils.getStackTrace(e));
                }
                try {
                    if (result != null) {
                        UserUsage uu = user.getUsage();
                        uu.setAskCount(uu.getAskCount() + 1);
                        uu.setTokenCount(uu.getTokenCount() + finalTotalToken);
                        uu.setUpdatedAt(new Date());
                        rtDao.updateUsage(uu);
                    }
                } catch (Exception e) {
                    logger.error("syncTrace-saveUserUsage异常："+ ExceptionUtils.getStackTrace(e));
                }
            });
        } catch (Exception e) {
            logger.error("syncTrace异常："+ ExceptionUtils.getStackTrace(e));
        }
    }

    public void syncTrace_GPT4(CompletionResult result, String conversionId, String msgId,
                               String question, String answer, String username, String questionType) {
        try {
            int timeCost;
            int reqToken = 0;
            int respToken = 0;
            int totalToken = 0;
            if (result == null) {
                timeCost = 30;
            } else {
                reqToken = (int) result.getUsage().getPromptTokens();
                respToken = (int) result.getUsage().getCompletionTokens();
                totalToken = (int) result.getUsage().getTotalTokens();
                long createdAt = result.getCreated();
                timeCost = (int) (System.currentTimeMillis() / 1000 - createdAt);
            }
            int finalReqToken = reqToken;
            int finalRespToken = respToken;
            int finalTotalToken = totalToken;
            CompletableFuture.runAsync(() -> {
                GptUserReqTrace gptUserReqTrace = GptUserReqTrace.builder()
                        .question(question)
                        .answer(answer)
                        .reqTokens(finalReqToken)
                        .respTokens(finalRespToken)
                        .totalTokens(finalTotalToken)
                        .count(1)
                        .gptStatus(1)
                        .conversationId(conversionId)
                        .user(username)
                        .msgId(msgId)
                        .timeCost(timeCost)
                        .build();
                reqTraceRepository.save(gptUserReqTrace);
            });
        } catch (Exception e) {
            logger.error("syncTrace异常："+ ExceptionUtils.getStackTrace(e));
        }
    }

    public UserUsage getUsage(String currentUser) {
        return rtDao.getUsage(currentUser);
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

    public void updateName(String username, String name) {
        rtDao.updateName(username, name);
    }

}

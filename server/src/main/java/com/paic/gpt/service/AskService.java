package com.paic.gpt.service;

import com.paic.gpt.model.Conversation;
import com.paic.gpt.model.GptUserReqTrace;
import com.paic.gpt.payload.AskRequest;
import com.paic.gpt.repository.ReqTraceRepository;
import com.paic.gpt.security.UserPrincipal;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.SocketTimeoutException;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.paic.gpt.util.AppConstants.ERROR_RESP_MSG;

@Service
public class AskService {

    private static final Logger logger = LoggerFactory.getLogger(AskService.class);

    @Value("${openai.api-key1}")
    private String apiKey1;

    @Value("${openai.api-key2}")
    private String apiKey2;

    @Value("${openai.api-key3}")
    private String apiKey3;

    @Value("${openai.model.chat}")
    private String chatModel;

    private static final int maxLength = 1000;
    private static final long timeout = 60L;

    @Autowired
    private ReqTraceService rtService;

    private String getApiKey() {
        int idx = new Random().nextInt(3);
        switch (idx) {
            case 0: return apiKey1;
            case 1: return apiKey2;
            case 2: return apiKey3;
        }
        return apiKey1;
    }

    public String ask(UserPrincipal user, AskRequest askReq) {
        String question = askReq.getQuestion();
        OpenAiService service = new OpenAiService(getApiKey(), Duration.ofSeconds(timeout));
        ChatMessage cm = new ChatMessage(ChatMessageRole.USER.value(), question);
        ChatCompletionRequest req = ChatCompletionRequest.builder()
                .model(chatModel)
//                .n(1)
                .topP(1.0)
//                .temperature(0.5)
//                .frequencyPenalty(0.5D)
//                .presencePenalty(0D)
                .maxTokens(maxLength)
                .user(user.getUsername())
                .messages(Collections.singletonList(cm))
                .build();
        String convId = conversationHandler(user.getUsername(), askReq.getConversationId());
        String msgId = askReq.getMsgId();
        try {
            logger.info("question=" + req);
            ChatCompletionResult result = service.createChatCompletion(req);
            long createdAt = result.getCreated();
            int timeCost = (int) (System.currentTimeMillis() / 1000 - createdAt);
            logger.info("user=[" + user.getUsername() + "], time cost=[" + timeCost + "] on result of [" + question + "]");
            logger.info("answer=" + result.toString());
            String resultText = result.getChoices().get(0).getMessage().getContent();
            rtService.syncTrace(result, convId, msgId,
                    question, resultText, user);
            return resultText;
        } catch (Exception e) {
            logger.error("ask异常："+ ExceptionUtils.getStackTrace(e));
            String resultText = ERROR_RESP_MSG;
            rtService.syncTrace(null, convId, msgId,
                    question, resultText, user);
            return resultText;
        }
    }

    private String conversationHandler(String username, String convId) {
        if (StringUtils.isBlank(convId)) {
            convId = UUID.randomUUID().toString();
            Conversation c = new Conversation(username, convId);
        } else {

        }

        return convId;
    }

    public String astGpt4(UserPrincipal user, AskRequest askReq) {
        String question = askReq.getQuestion();
        OpenAiService service = new OpenAiService(getApiKey(), Duration.ofSeconds(timeout));
        CompletionRequest req = CompletionRequest.builder()
                .model("gpt-4")
//                .n(1)
                .topP(1.0)
//                .temperature(0.5)
//                .frequencyPenalty(0.5D)
//                .presencePenalty(0D)
                .maxTokens(maxLength)
                .user(user.getUsername())
                .prompt(question)
                .build();
        String convId = conversationHandler(user.getUsername(), askReq.getConversationId());
        String msgId = askReq.getMsgId();
        try {
            CompletionResult result = service.createCompletion(req);
            long createdAt = result.getCreated();
            int timeCost = (int) (System.currentTimeMillis() / 1000 - createdAt);
            logger.info("user=[" + user.getUsername() + "], time cost=[" + timeCost + "] on result of [" + question + "]");
            logger.info(result.toString());
            String resultText = result.getChoices().get(0).getText();
            rtService.syncTrace_GPT4(result, convId, msgId,
                    question, resultText, user.getUsername());
            return resultText;
        } catch (Exception e) {
            logger.error("ask-gpt4 异常："+ ExceptionUtils.getStackTrace(e));
            String resultText = ERROR_RESP_MSG;
            rtService.syncTrace(null, convId, msgId,
                    question, resultText, user);
            return resultText;
        }
    }


}

package com.paic.gpt.service;

import com.paic.gpt.model.Conversation;
import com.paic.gpt.model.GptUserReqTrace;
import com.paic.gpt.payload.AskRequest;
import com.paic.gpt.repository.ReqTraceRepository;
import com.paic.gpt.security.UserPrincipal;
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
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class AskService {

    private static final Logger logger = LoggerFactory.getLogger(AskService.class);

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.model.chat}")
    private String chatModel;

    @Autowired
    private ReqTraceService rtService;

    public String ask(UserPrincipal user, AskRequest askReq) {
        String question = askReq.getQuestion();
        OpenAiService service = new OpenAiService(apiKey, Duration.ofSeconds(30L));
        ChatMessage cm = new ChatMessage(ChatMessageRole.USER.value(), question);
        ChatCompletionRequest req = ChatCompletionRequest.builder()
                .model(chatModel)
//                .n(1)
                .topP(1.0)
//                .temperature(0.5)
//                .frequencyPenalty(0.5D)
//                .presencePenalty(0D)
                .user(user.getUsername())
                .messages(Collections.singletonList(cm))
                .build();
        String convId = conversationHandler(user.getUsername(), askReq.getConversationId());
        try {
            ChatCompletionResult result = service.createChatCompletion(req);
            logger.info("user=[" + user.getUsername() + "] on result of [" + question + "]");
            logger.info(result.toString());
            String resultText = result.getChoices().get(0).getMessage().getContent();
            rtService.syncTrace(result, convId,
                    question, resultText, user.getUsername());
            return resultText;
        } catch (Exception e) {
            logger.error("ask异常："+ ExceptionUtils.getStackTrace(e));
            String resultText = "暂时无法回答，请稍后再试。";
            rtService.syncTrace(null, convId,
                    question, resultText, user.getUsername());
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


}

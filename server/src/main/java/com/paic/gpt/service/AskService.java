package com.paic.gpt.service;

import com.paic.gpt.model.GptUserReqTrace;
import com.paic.gpt.repository.ReqTraceRepository;
import com.paic.gpt.security.UserPrincipal;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Service
public class AskService {

    private static final Logger logger = LoggerFactory.getLogger(AskService.class);

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.model.chat}")
    private String chatModel;

    @Autowired
    private ReqTraceRepository reqTraceRepository;

    public String ask(UserPrincipal user, String question) {
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
        ChatCompletionResult result = service.createChatCompletion(req);
        logger.info(result.toString());
        String resultText = result.getChoices().get(0).getMessage().getContent();

        CompletableFuture.runAsync(() -> {
            long createdAt = result.getCreated();
            GptUserReqTrace gptUserReqTrace = GptUserReqTrace.builder()
                    .question(question)
                    .answer(resultText)
                    .reqTokens((int)result.getUsage().getPromptTokens())
                    .respTokens((int)result.getUsage().getCompletionTokens())
                    .totalTokens((int)result.getUsage().getTotalTokens())
                    .count(1)
                    .gptStatus(1)
                    .sessionId(result.getId())
                    .user(user.getUsername())
                    .msgId("")
                    .timeCost((int)(System.currentTimeMillis() - createdAt))
                    .build();
            reqTraceRepository.save(gptUserReqTrace);
        });
        return resultText;
    }

}

package com.paic.gpt.service;

import com.alibaba.fastjson.JSONObject;
import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.models.*;
import com.paic.gpt.model.Conversation;
import com.paic.gpt.payload.AskRequest;
import com.paic.gpt.security.UserPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.paic.gpt.util.AppConstants.ERROR_RESP_MSG;

@Service
public class AskService {

    private static final Logger logger = LoggerFactory.getLogger(AskService.class);

//    @Value("${openai.api-key1}")
//    private String apiKey1;

//    @Value("${openai.api-key2}")
//    private String apiKey2;

//    @Value("${openai.api-key3}")
//    private String apiKey3;

    @Value("${openai.model.chat}")
    private String chatModel;

    @Value("${openai.gpt35.deploymentId}")
    private String deploymentOrModelId;

    public static final int maxLength = 1000;
    public static final long timeout = 60L;

    @Autowired
    private ReqTraceService rtService;

    @Autowired
    private OpenAIClient client;
//    private OpenAiClient client;

//    public String getApiKey() {
//        int idx = new Random().nextInt(3);
//        switch (idx) {
//            case 0: return apiKey1;
//            case 1: return apiKey2;
//            case 2: return apiKey3;
//        }
//        return apiKey1;
//    }

    public String ask(UserPrincipal user, AskRequest askReq) {
        List<ChatMessage> chatMessages = new ArrayList<>();
        if ("chatWithContext".equalsIgnoreCase(askReq.getQuestionType())) {
            chatMessages = askReq.getContext().stream().map(a -> new ChatMessage(ChatRole.fromString(a.getRole()))
                    .setContent(a.getContent()))
                    .collect(Collectors.toList());
        }
        chatMessages.add(new ChatMessage(ChatRole.USER).setContent(askReq.getQuestion()));

        ChatCompletionsOptions opt = new ChatCompletionsOptions(chatMessages);
        opt.setMaxTokens(maxLength);
        opt.setModel(chatModel);
        opt.setN(1);

        String convId = askReq.getConversationId();
        String msgId = askReq.getMsgId();
        try {
            logger.info("question=" + askReq.getQuestion());
            ChatCompletions response = client.getChatCompletions(deploymentOrModelId, opt);
            logger.info(JSONObject.toJSONString(response));

            ChatChoice result = response.getChoices().get(0);
            String resultText = result.getMessage().getContent();

            long createdAt = response.getCreated();
            int timeCost = (int) (System.currentTimeMillis() / 1000 - createdAt);
            logger.info("user=[" + user.getUsername() + "], time cost=[" + timeCost + "] on result of [" + askReq.getQuestion() + "]");
            logger.info("answer=" + response.toString());
            rtService.syncTrace(response, convId, msgId,
                    askReq.getQuestion(), resultText, user, askReq.getQuestionType(), chatModel);
            return resultText;
        } catch (Exception e) {
            logger.error("ask异常："+ ExceptionUtils.getStackTrace(e));
            String resultText = ERROR_RESP_MSG;
            rtService.syncTrace(null, convId, msgId,
                    askReq.getQuestion(), resultText, user, askReq.getQuestionType(), chatModel);
            return resultText;
        }
    }

    /*
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.chat.ChatChoice;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
    public String ask(UserPrincipal user, AskRequest askReq) {
        Message currCM = Message.builder().role(Message.Role.USER).content(askReq.getQuestion()).build();
        List<Message> cms;
        if ("chatWithContext".equalsIgnoreCase(askReq.getQuestionType())) {
            cms = askReq.getContext().stream().map(a -> new Message(a.getRole(), a.getContent(), null))
                    .collect(Collectors.toList());
            cms.add(currCM);
        } else {
            cms = Collections.singletonList(currCM);
        }

        ChatCompletion chatCompletion = ChatCompletion.builder()
                .maxTokens(maxLength)
                .model(chatModel)
                .messages(cms)
//                .user(user.getUsername())
                .build();

        String convId = askReq.getConversationId();
        String msgId = askReq.getMsgId();
        try {
            logger.info("question=" + askReq.getQuestion());
            ChatCompletionResponse response = client.chatCompletion(chatCompletion);
            logger.info(response.toString());

            ChatChoice result = response.getChoices().get(0);
            String resultText = result.getMessage().getContent();

            long createdAt = response.getCreated();
            int timeCost = (int) (System.currentTimeMillis() / 1000 - createdAt);
            logger.info("user=[" + user.getUsername() + "], time cost=[" + timeCost + "] on result of [" + askReq.getQuestion() + "]");
            logger.info("answer=" + response.toString());
            rtService.syncTrace(response, convId, msgId,
                    askReq.getQuestion(), resultText, user, askReq.getQuestionType(), response.getModel());
            return resultText;
        } catch (Exception e) {
            logger.error("ask异常："+ ExceptionUtils.getStackTrace(e));
            String resultText = ERROR_RESP_MSG;
            rtService.syncTrace(null, convId, msgId,
                    askReq.getQuestion(), resultText, user, askReq.getQuestionType(), chatModel);
            return resultText;
        }
    }
*/
//    public String ask(UserPrincipal user, AskRequest askReq) {
//        ChatMessage currCM = new ChatMessage(ChatMessageRole.USER.value(), askReq.getQuestion());
//        List<ChatMessage> cms;
//        if ("chatWithContext".equalsIgnoreCase(askReq.getQuestionType())) {
//            cms = askReq.getContext().stream().map(a -> new ChatMessage(a.getRole(), a.getContent()))
//                    .collect(Collectors.toList());
//            cms.add(currCM);
//        } else {
//            cms = Collections.singletonList(currCM);
//        }
//
//        String question = askReq.getQuestion();
//        ChatCompletionRequest req = ChatCompletionRequest.builder()
////                .stream(true)
//                .model(chatModel)
////                .n(1)
//                .topP(1.0)
////                .temperature(0.5)
////                .frequencyPenalty(0.5D)
////                .presencePenalty(0D)
//                .maxTokens(maxLength)
////                .user(user.getUsername())
//                .messages(cms)
//                .build();
//        String convId = askReq.getConversationId();
//        String msgId = askReq.getMsgId();
//        try {
//            logger.info("question=" + req);
//            OpenAiService service = new OpenAiService(getApiKey(), Duration.ofSeconds(timeout));
//            ChatCompletionResult result = service.createChatCompletion(req);
//            long createdAt = result.getCreated();
//            int timeCost = (int) (System.currentTimeMillis() / 1000 - createdAt);
//            logger.info("user=[" + user.getUsername() + "], time cost=[" + timeCost + "] on result of [" + question + "]");
//            logger.info("answer=" + result.toString());
//            String resultText = result.getChoices().get(0).getMessage().getContent();
//            rtService.syncTrace(result, convId, msgId,
//                    question, resultText, user, askReq.getQuestionType(), chatModel);
//            return resultText;
//        } catch (Exception e) {
//            logger.error("ask异常："+ ExceptionUtils.getStackTrace(e));
//            String resultText = ERROR_RESP_MSG;
//            rtService.syncTrace(null, convId, msgId,
//                    question, resultText, user, askReq.getQuestionType(), chatModel);
//            return resultText;
//        }
//    }

    private String conversationHandler(String username, String convId) {
        if (StringUtils.isBlank(convId)) {
            convId = UUID.randomUUID().toString();
            Conversation c = new Conversation(username, convId);
        } else {

        }

        return convId;
    }

//    public String astGpt4(UserPrincipal user, AskRequest askReq) {
//        String question = askReq.getQuestion();
//        OpenAiService service = new OpenAiService(getApiKey(), Duration.ofSeconds(timeout));
//        CompletionRequest req = CompletionRequest.builder()
//                .model("gpt-4")
////                .n(1)
//                .topP(1.0)
////                .temperature(0.5)
////                .frequencyPenalty(0.5D)
////                .presencePenalty(0D)
//                .maxTokens(maxLength)
//                .user(user.getUsername())
//                .prompt(question)
//                .build();
//        String convId = conversationHandler(user.getUsername(), askReq.getConversationId());
//        String msgId = askReq.getMsgId();
//        try {
//            CompletionResult result = service.createCompletion(req);
//            long createdAt = result.getCreated();
//            int timeCost = (int) (System.currentTimeMillis() / 1000 - createdAt);
//            logger.info("user=[" + user.getUsername() + "], time cost=[" + timeCost + "] on result of [" + question + "]");
//            logger.info(result.toString());
//            String resultText = result.getChoices().get(0).getText();
//            rtService.syncTrace_GPT4(result, convId, msgId,
//                    question, resultText, user.getUsername(), askReq.getQuestionType());
//            return resultText;
//        } catch (Exception e) {
//            logger.error("ask-gpt4 异常："+ ExceptionUtils.getStackTrace(e));
//            String resultText = ERROR_RESP_MSG;
//            rtService.syncTrace_GPT4(null, convId, msgId,
//                    question, resultText, user.getUsername(), askReq.getQuestionType());
//            return resultText;
//        }
//    }


}

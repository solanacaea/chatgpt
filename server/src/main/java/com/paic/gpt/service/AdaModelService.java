package com.paic.gpt.service;

import com.paic.gpt.payload.AskRequest;
import com.paic.gpt.security.UserPrincipal;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Service
public class AdaModelService {

    private static final Logger logger = LoggerFactory.getLogger(AskService.class);

    @Autowired
    private AskService askService;

//    public String getTopic(UserPrincipal user, String question, String answer) {
//        OpenAiService service = new OpenAiService(askService.getApiKey(), Duration.ofSeconds(AskService.timeout));
//        CompletionRequest req = CompletionRequest.builder()
//                .model("gpt-4")
////                .n(1)
//                .topP(1.0)
////                .temperature(0.5)
////                .frequencyPenalty(0.5D)
////                .presencePenalty(0D)
//                .maxTokens(AskService.maxLength)
////                .user(user.getUsername())
//                .prompt("帮我提取以下内容主题，不超过10个字：" + question)
//                .build();
//        try {
//            CompletionResult result = service.createCompletion(req);
//            long createdAt = result.getCreated();
//            int timeCost = (int) (System.currentTimeMillis() / 1000 - createdAt);
//            logger.info("user=[" + user.getUsername() + "], time cost=[" + timeCost + "] getTopic of [" + question + "]");
//            logger.info(result.toString());
//            String resultText = result.getChoices().get(0).getText();
//            return resultText;
//        } catch (Exception e) {
//            logger.error("getTopic 异常："+ ExceptionUtils.getStackTrace(e));
//            return question.substring(0, 10);
//        }
//    }
}

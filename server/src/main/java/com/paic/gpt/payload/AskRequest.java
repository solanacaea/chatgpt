package com.paic.gpt.payload;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@ToString
@Data
public class AskRequest {

    @Size(max = 4000)
    private String question;
    private String msgId;
    private String conversationId;
    private List<ContextRequest> context;
    private String questionType;
    private String systemMessage;
}

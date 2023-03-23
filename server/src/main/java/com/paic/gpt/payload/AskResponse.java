package com.paic.gpt.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AskResponse {
    private String msgId;
    private String answer;
}

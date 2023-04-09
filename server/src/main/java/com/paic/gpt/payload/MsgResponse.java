package com.paic.gpt.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MsgResponse {
    private String status;
    private String message;
}

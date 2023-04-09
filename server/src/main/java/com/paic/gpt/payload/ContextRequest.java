package com.paic.gpt.payload;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class ContextRequest {
    private String role;
    private String content;
}

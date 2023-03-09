package com.paic.gpt.payload;

import lombok.Data;

@Data
public class UserSummary {
    private Long id;
    private String username;
    private String name;
    private Integer maxCount;
    private Integer currCount;

    public UserSummary(Long id, String username, String name,
                       Integer maxCount, Integer currCount) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.maxCount = maxCount;
        this.currCount = currCount;
    }

}

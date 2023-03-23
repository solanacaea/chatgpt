package com.paic.gpt.payload;

import com.paic.gpt.model.Membership;
import com.paic.gpt.model.UserUsage;
import lombok.Data;

@Data
public class UserSummary {
    private Long id;
    private String username;
    private String name;
    private Membership memberInfo;
    private UserUsage usage;

    public UserSummary(Long id, String username, String name,
                       Membership memberInfo, UserUsage usage) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.memberInfo = memberInfo;
        this.usage = usage;
    }

}

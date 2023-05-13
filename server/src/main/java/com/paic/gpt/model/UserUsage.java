package com.paic.gpt.model;

import com.paic.gpt.model.audit.DateAudit;

import javax.persistence.*;

@Entity
@Table(name = "t_user_usage")
public class UserUsage extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private Integer msgDay;
    private Integer askCount;
    private Integer tokenCount;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMsgDay() {
        return msgDay;
    }

    public void setMsgDay(Integer msgDay) {
        this.msgDay = msgDay;
    }

    public Integer getAskCount() {
        return askCount;
    }

    public void setAskCount(Integer askCount) {
        this.askCount = askCount;
    }

    public Integer getTokenCount() {
        return tokenCount;
    }

    public void setTokenCount(Integer tokenCount) {
        this.tokenCount = tokenCount;
    }
}

package com.paic.gpt.model;

import com.paic.gpt.model.audit.DateAudit;

import javax.persistence.*;

@Entity
@Table(name = "t_gpt_membership")
public class Membership extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String comments;
    private String name;
    private Integer reqCount;
    private Integer maxToken;
    private Integer tokenPerReq;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getReqCount() {
        return reqCount;
    }

    public void setReqCount(Integer reqCount) {
        this.reqCount = reqCount;
    }

    public Integer getMaxToken() {
        return maxToken;
    }

    public void setMaxToken(Integer maxToken) {
        this.maxToken = maxToken;
    }

    public Integer getTokenPerReq() {
        return tokenPerReq;
    }

    public void setTokenPerReq(Integer tokenPerReq) {
        this.tokenPerReq = tokenPerReq;
    }
}

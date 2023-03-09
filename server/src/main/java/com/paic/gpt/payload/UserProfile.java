package com.paic.gpt.payload;

import java.util.Date;

public class UserProfile {
    private Long id;
    private String username;
    private String name;
    private Date joinedAt;
    private Integer currCount;
    private Integer totalCount;

    public UserProfile(Long id, String username, String name, Date joinedAt, Integer currCount, Integer totalCount) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.joinedAt = joinedAt;
        this.currCount = currCount;
        this.totalCount = totalCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Date joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Integer getCurrCount() {
        return currCount;
    }

    public void setCurrCount(Integer currCount) {
        this.currCount = currCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}

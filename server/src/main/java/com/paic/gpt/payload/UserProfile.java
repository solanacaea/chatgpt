package com.paic.gpt.payload;

import com.paic.gpt.model.Membership;
import com.paic.gpt.model.UserUsage;

import java.util.Date;

public class UserProfile {
    private Long id;
    private String username;
    private String name;
    private Date joinedAt;
    private UserUsage usage;
    private Membership membership;

    public UserProfile(Long id, String username, String name, Date joinedAt, UserUsage usage, Membership membership) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.joinedAt = joinedAt;
        this.usage = usage;
        this.membership = membership;
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

    public UserUsage getUsage() {
        return usage;
    }

    public void setUsage(UserUsage usage) {
        this.usage = usage;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }
}

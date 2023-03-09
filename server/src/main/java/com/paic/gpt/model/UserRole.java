package com.paic.gpt.model;

import com.paic.gpt.model.audit.DateAudit;

import javax.persistence.*;

@Entity
@Table(name = "t_user_roles")
public class UserRole extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String code;

    public UserRole(String username, String code) {
        this.username = username;
        this.code = code;
    }

    public UserRole() {

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

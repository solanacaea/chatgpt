package com.paic.gpt.model;

import com.paic.gpt.model.audit.DateAudit;
import org.hibernate.annotations.NaturalId;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "t_user_paic", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "username"
        })
})
public class User extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 100)
    private String username;

    @NaturalId
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(max = 100)
    private String pwd;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "acc_type")
    private Membership member;


    public User() {

    }

    public User(String name, String username, String email, String pwd) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.pwd = pwd;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String password) {
        this.pwd = password;
    }

    public Membership getMember() {
        return member;
    }

    public void setMember(Membership member) {
        this.member = member;
    }
}
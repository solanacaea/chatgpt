package com.paic.gpt.model;

//import org.hibernate.annotations.NaturalId;

import com.paic.gpt.model.audit.DateAudit;

import javax.persistence.*;

@Entity
@Table(name = "t_gpt_role")
public class Role extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @Enumerated(EnumType.STRING)
//    @NaturalId
    @Column(length = 60)
    private String code;

    public Role() {

    }

    public Role(String code) {
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String name) {
        this.code = name;
    }

}

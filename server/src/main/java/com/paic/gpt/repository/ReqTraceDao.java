package com.paic.gpt.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;

@Repository
public class ReqTraceDao {

    @PersistenceContext
    private EntityManager em;

    public int getUserTodayCount(String user) {
        String hql = "select count(1) from GptUserReqTrace r where r.user = :user and createdAt > :today";
        Query q = em.createQuery(hql);
        q.setParameter("user", user);
        q.setParameter("today", new Date());
        Long count = (Long) q.getSingleResult();
        return count.intValue();
    }

}
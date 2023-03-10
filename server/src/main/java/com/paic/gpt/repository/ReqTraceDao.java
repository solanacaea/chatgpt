package com.paic.gpt.repository;

import com.paic.gpt.model.Conversation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

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

    public List<Conversation> getUserConversation(String username) {
        String hql = "from Conversation r where r.username = :username and createdAt > :today";
        Query q = em.createQuery(hql);
        q.setParameter(username, username);
        q.setParameter("today", new Date());
        List<Conversation> count = q.getResultList();
        return count;
    }

    public void updateUserConversation(String username) {
        String hql = "from Conversation r where r.username = :username";
        Query q = em.createQuery(hql);
        q.setParameter(username, username);
        q.setParameter("today", new Date());
        q.executeUpdate();
    }

}
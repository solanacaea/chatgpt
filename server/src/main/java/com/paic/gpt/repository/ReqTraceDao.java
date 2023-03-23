package com.paic.gpt.repository;

import com.paic.gpt.model.Conversation;
import com.paic.gpt.model.UserUsage;
import com.paic.gpt.util.AppConstants;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class ReqTraceDao {

    @PersistenceContext
    private EntityManager em;

    public UserUsage getUsage(String user) {
        String hql = "from UserUsage r where r.username = :user and msgDay = :today";
        String msgDay = AppConstants.DF_yyyyMMdd.format(new Date());
        Integer msgDayInt = Integer.parseInt(msgDay);
        Query q = em.createQuery(hql);
        q.setParameter("user", user);
        q.setParameter("today", msgDayInt);
        List<?> list =  q.getResultList();
        UserUsage curr = null;
        if (CollectionUtils.isEmpty(list)) {
            UserUsage uu = new UserUsage();
            uu.setUsername(user);
            uu.setMsgDay(Integer.parseInt(msgDay));
            uu.setAskCount(0);
            uu.setTokenCount(0);
            curr = em.merge(uu);
        } else {
            curr = (UserUsage) list.get(0);
        }
        return curr;
    }

    @Transactional
    @Modifying
    public void updateUsage(UserUsage u) {
        em.merge(u);
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

    public UserUsage getTotalUsage(Integer day) {
        String hql = "from UserUsage r where msgDay = :today";
        Query q = em.createQuery(hql);
        q.setParameter("today", day);
        UserUsage total = new UserUsage();
        total.setUsername("total");
        total.setMsgDay(day);
        List<UserUsage> list = q.getResultList();
        if (CollectionUtils.isEmpty(list)) {
            total.setTokenCount(0);
            total.setAskCount(0);
        } else {
            Integer ask = list.stream().mapToInt(UserUsage::getAskCount).sum();
            Integer token = list.stream().mapToInt(UserUsage::getTokenCount).sum();
            total.setTokenCount(token);
            total.setAskCount(ask);
        }
        return total;
    }

}
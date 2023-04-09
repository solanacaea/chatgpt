package com.paic.gpt.repository;

import com.paic.gpt.model.Conversation;
import com.paic.gpt.model.UserUsage;
import com.paic.gpt.util.AppConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

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

    @Transactional
    @Modifying
    public void updateUserConversation(Conversation conversation) {
        em.merge(conversation);
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

    public Conversation getUserConversationById(String convId) {
        String hql = "from Conversation r where r.conversationId = :convId";
        Query q = em.createQuery(hql);
        q.setParameter("convId", convId);
        List<Conversation> count = q.getResultList();
        if (CollectionUtils.isEmpty(count)) {
            return null;
        }
        return count.get(0);
    }

    @Transactional
    @Modifying
    public void updateName(String username, String name) {
        String hql = "update User set name =:name, updatedAt=:updatedAt where username = :username";
        Query q = em.createQuery(hql);
        q.setParameter("name", name);
        q.setParameter("username", username);
        q.setParameter("updatedAt", new Date());
        q.executeUpdate();
    }


}
package com.paic.gpt.repository;

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
public class UserDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Modifying
    public void updatePwd(String username, String pwd) {
        String hql = "update User set pwd =:pwd, updatedAt=:updatedAt where username = :username";
        Query q = em.createQuery(hql);
        q.setParameter("pwd", pwd);
        q.setParameter("username", username);
        q.setParameter("updatedAt", new Date());
        q.executeUpdate();
    }

    public boolean checkUsrPwd(String username, String pwd) {
        String hql = "select 1 from User where username = :username and pwd = :pwd";
        Query q = em.createQuery(hql);
        q.setParameter("pwd", pwd);
        q.setParameter("username", username);
        List<?> list = q.getResultList();
        return CollectionUtils.isNotEmpty(list);
    }
}

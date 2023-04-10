package com.paic.gpt.repository;

import com.paic.gpt.model.User;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        String hql = "select u from User u where u.username = :username";
        Query q = em.createQuery(hql);
        q.setParameter("username", username);
        List<?> list = q.getResultList();
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        User u = (User) list.get(0);
        return passwordEncoder.matches(pwd, u.getPwd());
    }
}

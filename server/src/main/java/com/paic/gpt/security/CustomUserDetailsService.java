package com.paic.gpt.security;

import com.paic.gpt.exception.ResourceNotFoundException;
import com.paic.gpt.model.User;
import com.paic.gpt.model.UserRole;
import com.paic.gpt.repository.ReqTraceDao;
import com.paic.gpt.repository.UserRepository;
import com.paic.gpt.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private ReqTraceDao rtDao;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        // Let people login with either username or email
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username : " + username)
        );
        List<UserRole> roles = userRoleRepository.findByUsername(username);
        int currCount = rtDao.getUserTodayCount(username);
        return UserPrincipal.create(user, roles, currCount);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("User", "id", id)
        );
        List<UserRole> roles = userRoleRepository.findByUsername(user.getUsername());
        int currCount = rtDao.getUserTodayCount(user.getUsername());
        return UserPrincipal.create(user, roles, currCount);
    }
}
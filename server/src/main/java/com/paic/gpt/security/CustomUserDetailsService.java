package com.paic.gpt.security;

import com.paic.gpt.exception.ResourceNotFoundException;
import com.paic.gpt.model.User;
import com.paic.gpt.model.UserRole;
import com.paic.gpt.model.UserUsage;
import com.paic.gpt.repository.ReqTraceDao;
import com.paic.gpt.repository.UserRepository;
import com.paic.gpt.repository.UserRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@CacheConfig(cacheNames = CustomUserDetailsService.CACHE_NAME)
public class CustomUserDetailsService implements UserDetailsService {
    protected static final String CACHE_NAME = "CustomUserDetailsService";
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private ReqTraceDao rtDao;

    @Override
    @Transactional
    @Cacheable(unless = "#result == null")
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        // Let people login with either username or email
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username : " + username)
        );
        List<UserRole> roles = userRoleRepository.findByUsername(username);
        UserUsage curr = rtDao.getUsage(username);
        return UserPrincipal.create(user, roles, curr);
    }

    @Transactional
    @Cacheable(unless = "#result == null")
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("User", "id", id)
        );
        List<UserRole> roles = userRoleRepository.findByUsername(user.getUsername());
        UserUsage curr = rtDao.getUsage(user.getUsername());
        return UserPrincipal.create(user, roles, curr);
    }

    @CacheEvict(allEntries = true)
    public void cacheEvict() {
        logger.info("cacheEvict cacheNames: {} ...", CACHE_NAME);
    }

    public boolean cacheEvict(Long id) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache == null) {
            logger.info("cacheEvict cacheNames: {} id=[{}], not exist...", CACHE_NAME, id);
            return false;
        }
        boolean res = cache.evictIfPresent("com.paic.gpt.security.CustomUserDetailsService.loadUserById." + id);
        logger.info("cacheEvict cacheNames: {} id=[{}]...", CACHE_NAME, id);
        return res;
    }

    public boolean cacheEvict(String username) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache == null) {
            logger.info("cacheEvict cacheNames: {} id=[{}], not exist...", CACHE_NAME, username);
            return false;
        }
        boolean res = cache.evictIfPresent("com.paic.gpt.security.CustomUserDetailsService.loadUserByUsername." + username);
        logger.info("cacheEvict cacheNames: {} id=[{}]...", CACHE_NAME, username);
        return res;
    }
}
package com.paic.gpt.service;

import com.paic.gpt.model.LimitConfig;
import com.paic.gpt.model.UserUsage;
import com.paic.gpt.repository.LimitConfigRepo;
import com.paic.gpt.repository.ReqTraceDao;
import com.paic.gpt.security.CustomUserDetailsService;
import com.paic.gpt.util.AppConstants;
import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.paic.gpt.util.AppConstants.LIMIT_CONFIG_TYPE;

@Service
@CacheConfig(cacheNames = ConfigService.CACHE_NAME)
public class ConfigService {
    protected static final String CACHE_NAME = "ConfigService";
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private ReqTraceDao rtDao;

    @Autowired
    private LimitConfigRepo limitRepo;

    @Cacheable(unless = "#result == null")
    public UserUsage getTotalUsage() {
        String msgDay = AppConstants.DF_yyyyMMdd.format(new Date());
        return rtDao.getTotalUsage(Integer.parseInt(msgDay));
    }

    @CacheEvict(allEntries = true)
    public void cacheEvict() {
        logger.info("cacheEvict cacheNames: {} ...", CACHE_NAME);
    }

    @Cacheable(unless = "#result == null")
    public Map<String, LimitConfig> getLimitConfig() {
        List<LimitConfig> list = limitRepo.findByConfigType(LIMIT_CONFIG_TYPE);
        return ListUtils.emptyIfNull(list).stream().collect(
                Collectors.toMap(LimitConfig::getConfigKey, v -> v));
    }
}

package com.paic.gpt.service;

import com.paic.gpt.model.LimitConfig;
import com.paic.gpt.model.Membership;
import com.paic.gpt.model.UserUsage;
import com.paic.gpt.repository.ReqTraceDao;
import com.paic.gpt.security.UserPrincipal;
import com.paic.gpt.util.AppConstants;
import com.theokanning.openai.Usage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserService {

    @Autowired
    private ConfigService confService;

    private static final String USER_USAGE_LIMIT_COUNT = "USER_USAGE_LIMIT_COUNT";
    private static final String USER_USAGE_LIMIT_TOKEN = "USER_USAGE_LIMIT_TOKEN";
    private static final String MAX_TOKENS = "MAX_TOKENS";
    private static final String PARALLEL_CALLS = "PARALLEL_CALLS";

    private AtomicInteger count = new AtomicInteger();

    public String checkUserDosage(UserPrincipal currentUser) {
        UserUsage currUU = currentUser.getUsage();
        Membership currMS = currentUser.getMemberInfo();
        Map<String, LimitConfig> currLimit = getLimit();
        if (currUU.getAskCount() >= currMS.getReqCount()) {
            return currLimit.get(USER_USAGE_LIMIT_COUNT).getUtterance();
        }
        if (currUU.getTokenCount() > currMS.getMaxToken()) {
            return currLimit.get(USER_USAGE_LIMIT_TOKEN).getUtterance();
        }

        UserUsage uu = getTotalUsage();
        int currTtCount = uu.getAskCount();
        int maxToken = Integer.parseInt(currLimit.get(MAX_TOKENS).getConfigs());
        if (currTtCount >= maxToken) {
            return currLimit.get(MAX_TOKENS).getUtterance();
        }

        int maxThreads = Integer.parseInt(currLimit.get(PARALLEL_CALLS).getConfigs());
        if (get() >= 1) {
            return currLimit.get(PARALLEL_CALLS).getUtterance();
        }
        return null;
    }


    private UserUsage getTotalUsage() {
        return confService.getTotalUsage();
    }

    public Map<String, LimitConfig> getLimit() {
        return confService.getLimitConfig();
    }

    public int get() {
        return count.get();
    }

    public int plus() {
        return count.incrementAndGet();
    }

    public int minus() {
        return count.decrementAndGet();
    }


}

package com.paic.gpt.service;

import com.paic.gpt.ip.IpService;
import com.paic.gpt.model.GptAudit;
import com.paic.gpt.repository.GptAuditRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AuditService {

    @Autowired
    private IpService ipService;

    @Autowired
    private GptAuditRepo auditRepo;

    public void auditLogin(HttpServletRequest req, String username, String operation) {
        String ip = IpService.getIpAddr(req);
//        String ipInfo = ipService.getCityInfo(ip);
        GptAudit audit = new GptAudit();
        audit.setUsername(username);
        audit.setIp(ip);
//        audit.setIpInfo(ipInfo);
        audit.setOthers(operation);
        auditRepo.save(audit);
    }
}

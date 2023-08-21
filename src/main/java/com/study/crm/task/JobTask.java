package com.study.crm.task;

import com.study.crm.service.CustomerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/***
 * 实现定时刷新客户流失信息
 */
@Component
public class JobTask {
    @Resource
    private CustomerService customerService;

    //每2秒执行一次
    @Scheduled(cron = "0/2 * * * * ?")
    public void job(){
        customerService.updateCustomerState();
    }
}

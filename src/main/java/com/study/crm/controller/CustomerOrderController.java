package com.study.crm.controller;

import com.study.crm.base.BaseController;
import com.study.crm.query.CustomerOrderQuery;
import com.study.crm.service.CustomerOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("customer_order")
public class CustomerOrderController extends BaseController {
    @Resource
    private CustomerOrderService customerOrderService;
    @ResponseBody
    @RequestMapping("list")
    public Map<String,Object> queryCustomerOrderAll(CustomerOrderQuery customerOrderQuery){
        return customerOrderService.queryCustomerOrderAll(customerOrderQuery);
    }
}

package com.study.crm.controller;

import com.study.crm.base.BaseController;
import com.study.crm.query.OrderDetailsQuery;
import com.study.crm.service.OrderDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("order_details")
public class OrderDetailsController extends BaseController {

    @Resource
    private OrderDetailsService orderDetailsService;
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryOrderDetailsAll(OrderDetailsQuery orderDetailsQuery){
        return orderDetailsService.queryOrderDetailsAll(orderDetailsQuery);
    }
}

package com.study.crm.controller;

import com.study.crm.base.BaseController;
import com.study.crm.base.ResultInfo;
import com.study.crm.query.CustomerLossQuery;
import com.study.crm.service.CustomerLossService;
import com.study.crm.service.CustomerReprieveService;
import com.study.crm.vo.CustomerLoss;
import com.study.crm.vo.CustomerReprieve;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("customer_loss")
public class CustomerLossController extends BaseController {
    @Resource
    private CustomerLossService customerLossService;

    @Resource
    private CustomerReprieveService customerReprieveService;

    @RequestMapping("index")
    public String index(){
        return "customerLoss/customer_loss";
    }

    @ResponseBody
    @RequestMapping("list")
    public Map<String,Object> queryCustomerLossAll(CustomerLossQuery customerLossQuery){
        return customerLossService.queryCustomerLossAll(customerLossQuery);
    }

    @RequestMapping("toCustomerReprPage")
    public String toCustomerReprPage(Integer id, HttpServletRequest request){
        CustomerLoss customerLoss=customerLossService.selectByPrimaryKey(id);
        request.setAttribute("customerLoss",customerLoss);
        return "customerLoss/customer_rep";
    }

    @GetMapping("addOrUpdateCustomerReprPage")
    public String addOrUpdateCustomerReprPage(Integer id, Integer lossId, Model model){
        CustomerReprieve customerReprieve=customerReprieveService.selectByPrimaryKey(id);
        model.addAttribute("customerRep",customerReprieve);
        model.addAttribute("lossId",lossId);
        return "customerLoss/customer_rep_add_update";
    }


    @PostMapping("updateCustomerLossStateById")
    @ResponseBody
    public ResultInfo updateCustomerLossStateById(Integer id, String lossReason){
        customerLossService.updateCustomerLossStateById(id,lossReason);
        return success("客户确认流失成功!");
    }
}

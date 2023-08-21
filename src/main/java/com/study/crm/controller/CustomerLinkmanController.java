package com.study.crm.controller;

import com.study.crm.base.BaseController;
import com.study.crm.base.ResultInfo;
import com.study.crm.service.CustomerLinkmanService;
import com.study.crm.utils.AssertUtil;
import com.study.crm.vo.CustomerLinkman;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("customer_linkman")
public class CustomerLinkmanController extends BaseController {
    @Resource
    private CustomerLinkmanService customerLinkmanService;

    @RequestMapping("linkCustomerPage")
    public String toCustomerLinkmanPage(Integer cid, HttpServletRequest request){
        System.out.println(cid);

        CustomerLinkman customerLinkman=customerLinkmanService.queryLinkManByCustomerId(cid);
//        AssertUtil.isTrue(customerLinkman==null,"该用户没有联系人");
//        request.setAttribute("customer",customerLinkman);
//        return "customer/customer_link";
        if(customerLinkman!=null){
            request.setAttribute("customer",customerLinkman);
            return "customer/customer_link";
        }else{
            System.out.println("linkpage为空");
            return "customer/customer_link_kong";
        }
    }

    @ResponseBody
    @RequestMapping("update")
    public ResultInfo updateCustomerLinkman(CustomerLinkman customerLinkman){
        customerLinkmanService.updateLinkman(customerLinkman);
        return success("客户联系人信息更新成功");
    }


}

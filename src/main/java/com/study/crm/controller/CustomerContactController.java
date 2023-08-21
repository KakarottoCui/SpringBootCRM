package com.study.crm.controller;

import com.study.crm.base.BaseController;
import com.study.crm.base.ResultInfo;
import com.study.crm.query.CustomerContactQuery;
import com.study.crm.service.CustomerContactService;
import com.study.crm.vo.CustomerContact;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("customer_contact")
public class CustomerContactController extends BaseController {
    @Resource
    private CustomerContactService customerContactService;

    @RequestMapping("customerContactPage")
    public String customerContactPage(Integer cid, HttpServletRequest request){
        System.out.println(cid);
        request.setAttribute("cusId",cid);
        return "customerContact/customer_contact";
    }
    @ResponseBody
    @RequestMapping("list")
    public Map<String,Object> queryCustomerContactAll(CustomerContactQuery customerContactQuery){
        return customerContactService.queryCustomerContactAll(customerContactQuery);
    }

    @RequestMapping("addOrUpdateCustomerContactPage")
    public String addOrUpdateCustomerContactPage(Integer cusId,Integer id,HttpServletRequest request){
       // System.out.println(cusId+"cusId");
        request.setAttribute("cusId",cusId);
        CustomerContact customerContact=customerContactService.selectByPrimaryKey(id);
        if(customerContact!=null){
            request.setAttribute("customerContact",customerContact);
        }
        return "customerContact/add_update";
    }
    @ResponseBody
    @PostMapping("add")
    public ResultInfo addCustomerContact(CustomerContact customerContact){
        customerContactService.addCustomerContact(customerContact);
        return success("添加客户联系信息成功");
    }
    @ResponseBody
    @PostMapping("update")
    public ResultInfo updateCustomerContact(CustomerContact customerContact){
        customerContactService.updateCustomerContact(customerContact);
        return success("更新客户联系信息成功");
    }
    @ResponseBody
    @PostMapping("delete")
    public ResultInfo deleteCustomerContact(Integer id){
        customerContactService.deleteCustomerContact(id);
        return success("删除客户联系信息成功");
    }
}

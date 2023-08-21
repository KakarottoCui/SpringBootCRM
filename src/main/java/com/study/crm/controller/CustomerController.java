package com.study.crm.controller;

import com.study.crm.base.BaseController;
import com.study.crm.base.ResultInfo;
import com.study.crm.query.CustomerQuery;
import com.study.crm.query.UserQuery;
import com.study.crm.service.CustomerLinkmanService;
import com.study.crm.service.CustomerOrderService;
import com.study.crm.service.CustomerService;
import com.study.crm.vo.Customer;
import com.study.crm.vo.CustomerLinkman;
import com.study.crm.vo.CustomerOrder;
import com.study.crm.vo.OrderDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("customer")
public class CustomerController extends BaseController {
    @Resource
    private CustomerService customerService;

    @Resource
    private CustomerOrderService customerOrderService;

    @Resource
    private CustomerLinkmanService customerLinkmanService;

    @RequestMapping("index")
    public String index(){
        return "customer/customer";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> selectByParams(CustomerQuery customerQuery){
        return customerService.queryByParamsForTable(customerQuery);
    }
    @RequestMapping("addOrUpdateCustomerPage")
    public String addOrUpdateCustomerPage(Integer id, HttpServletRequest request){
        if(id!=null){
            Customer customer=customerService.selectByPrimaryKey(id);
            request.setAttribute("customer",customer);
        }
        return "customer/add_update";
    }

    @ResponseBody
    @RequestMapping("add")
    public ResultInfo addCustomer(Customer customer){
        customerService.addCustomer(customer);
        return success("添加客户信息成功");
    }

    @ResponseBody
    @RequestMapping("update")
    public ResultInfo updateCustomer(Customer customer){
        customerService.updateCustomer(customer);
        return success("更新客户信息成功");
    }
    @ResponseBody
    @RequestMapping("delete")
    public ResultInfo deleteCustomer(Integer id){
        customerService.deleteCustomer(id);
        return success("删除客户信息成功");
    }

//    @RequestMapping("linkCustomerPage")
//    public String linkCustomerPage(Integer cid,HttpServletRequest request){
//        CustomerLinkman customerLinkman=customerLinkmanService.queryLinkManByCustomerId(cid);
//        request.setAttribute("customer",customerLinkman);
//        return "customer/customer_link";
//    }


    @RequestMapping("orderInfoPage")
    public String orderInfoPage(Integer cid,HttpServletRequest request){
        request.setAttribute("customer",customerService.selectByPrimaryKey(cid));
        return "customer/customer_order";
    }
    @RequestMapping("orderDetailPage")
    public String orderDetailPage(Integer orderId,HttpServletRequest request){
        OrderDetails orderDetails=customerOrderService.queryOrderByOrderId(orderId);
        request.setAttribute("order",orderDetails);
        return "customer/customer_order_detail";
    }
    @ResponseBody
    @RequestMapping("queryCustomerContributionByParams")
    public Map<String,Object> queryCustomerContributionByParams(CustomerQuery customerQuery){
        return customerService.queryCustomerContributionByParams(customerQuery);
    }


    @PostMapping("countCustomerMake")
    @ResponseBody
    public Map<String,Object> countCustomerMake(){
        return customerService.countCustomerMake();
    }


    @PostMapping("countCustomerMake02")
    @ResponseBody
    public Map<String,Object> countCustomerMake02(){
        return customerService.countCustomerMake02();
    }


    @PostMapping("countCustomerServe")
    @ResponseBody
    public Map<String,Object> countCustomerServe(){
        return customerService.countCustomerServe();
    }


    @PostMapping("countCustomerServe02")
    @ResponseBody
    public Map<String,Object> countCustomerServe02(){
        return customerService.countCustomerServe02();
    }

}

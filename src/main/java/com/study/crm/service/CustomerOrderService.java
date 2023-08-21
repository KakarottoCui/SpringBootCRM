package com.study.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.crm.base.BaseService;
import com.study.crm.dao.CustomerOrderMapper;
import com.study.crm.dao.OrderDetailsMapper;
import com.study.crm.query.CustomerContactQuery;
import com.study.crm.query.CustomerOrderQuery;
import com.study.crm.vo.CustomerContact;
import com.study.crm.vo.CustomerOrder;
import com.study.crm.vo.OrderDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerOrderService extends BaseService<CustomerOrder,Integer> {
    @Resource
    private CustomerOrderMapper customerOrderMapper;

    @Resource
    private OrderDetailsMapper orderDetailsMapper;
    /***
     * 查询t_customer_contact
     * @param customerOrderQuery
     * @return
     */
    public Map<String, Object> queryCustomerOrderAll(CustomerOrderQuery customerOrderQuery) {
        Map<String,Object> map=new HashMap<String,Object>();
        PageHelper.startPage(customerOrderQuery.getPage(),customerOrderQuery.getLimit());
        PageInfo<CustomerContact> pageInfo=new PageInfo<CustomerContact>(customerOrderMapper.queryCustomerOrderAll(customerOrderQuery));
        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return  map;
    }

    public OrderDetails queryOrderByOrderId(Integer orderId){
          return orderDetailsMapper.selectByPrimaryKey(orderId);
    }
}

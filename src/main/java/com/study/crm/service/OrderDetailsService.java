package com.study.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.crm.base.BaseService;
import com.study.crm.dao.OrderDetailsMapper;
import com.study.crm.query.CustomerContactQuery;
import com.study.crm.query.OrderDetailsQuery;
import com.study.crm.vo.CustomerContact;
import com.study.crm.vo.OrderDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderDetailsService extends BaseService<OrderDetails,Integer> {
    @Resource
    private OrderDetailsMapper orderDetailsMapper;

    /***
     * 查询t_customer_contact
     * @param customerContactQuery
     * @return
     */
    public Map<String, Object> queryOrderDetailsAll(OrderDetailsQuery orderDetailsQuery) {
        //  System.out.println(customerContactQuery.getCusId()+"cutomer");
        Map<String,Object> map=new HashMap<String,Object>();
        PageHelper.startPage(orderDetailsQuery.getPage(),orderDetailsQuery.getLimit());
        PageInfo<CustomerContact> pageInfo=new PageInfo<CustomerContact>(orderDetailsMapper.queryOrderDetailsAll(orderDetailsQuery));
        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return  map;
    }
}

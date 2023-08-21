package com.study.crm.dao;

import com.study.crm.query.OrderDetailsQuery;
import com.study.crm.vo.CustomerContact;
import com.study.crm.vo.OrderDetails;

import java.util.List;

public interface OrderDetailsMapper  extends BaseMapper<OrderDetails,Integer>{

    List<CustomerContact> queryOrderDetailsAll(OrderDetailsQuery orderDetailsQuery);
}
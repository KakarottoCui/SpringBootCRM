package com.study.crm.dao;

import com.study.crm.query.CustomerOrderQuery;
import com.study.crm.vo.CustomerContact;
import com.study.crm.vo.CustomerOrder;

import java.util.List;

public interface CustomerOrderMapper extends BaseMapper<CustomerOrder,Integer> {

    List<CustomerContact> queryCustomerOrderAll(CustomerOrderQuery customerOrderQuery);

    CustomerOrder queryLossCustomerOrderByCustomerId(Integer id);
}
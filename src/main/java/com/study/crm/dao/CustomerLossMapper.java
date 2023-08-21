package com.study.crm.dao;

import com.study.crm.query.CustomerLossQuery;
import com.study.crm.vo.CustomerContact;
import com.study.crm.vo.CustomerLoss;

import java.util.List;

public interface CustomerLossMapper extends BaseMapper<CustomerLoss,Integer> {

    List<CustomerContact> queryCustomerLossAll(CustomerLossQuery customerLossQuery);

}
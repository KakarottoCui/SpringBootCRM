package com.study.crm.dao;

import com.study.crm.query.CustomerContactQuery;
import com.study.crm.vo.CustomerContact;

import java.util.List;

public interface CustomerContactMapper extends BaseMapper<CustomerContact,Integer> {

    List<CustomerContact> queryCustomerContactAll(CustomerContactQuery customerContactQuery);
}
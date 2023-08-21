package com.study.crm.dao;

import com.study.crm.base.BaseController;
import com.study.crm.query.CustomerReprQuery;
import com.study.crm.vo.CustomerContact;
import com.study.crm.vo.CustomerReprieve;

import java.util.List;

public interface CustomerReprieveMapper extends BaseMapper<CustomerReprieve,Integer> {

    List<CustomerContact> queryCustomerReprieveByParams(CustomerReprQuery customerReprQuery);
}
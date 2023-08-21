package com.study.crm.dao;

import com.study.crm.query.CustomerQuery;
import com.study.crm.vo.Customer;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CustomerMapper extends BaseMapper<Customer,Integer> {
    public Customer queryCustomerByName(@Param("name") String name);

    List<Customer> queryLossCustomer();

    int updateCustomerStateByIds(List<Integer> lossCustomerList);

    //查询客户贡献
    public List<Map<String,Object>> queryCustomerContributionByParams(CustomerQuery customerQuery);

    //统计客户构成
    public List<Map<String,Object>>  countCustomerMake();

    //统计客户服务
    public List<Map<String,Object>>  countCustomerServe();
}
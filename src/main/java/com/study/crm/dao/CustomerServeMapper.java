package com.study.crm.dao;

import com.study.crm.query.CustomerServeQuery;
import com.study.crm.vo.CustomerServe;

import java.util.List;

public interface CustomerServeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerServe record);

    int insertSelective(CustomerServe record);

    CustomerServe selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerServe record);

    int updateByPrimaryKey(CustomerServe record);

    List<CustomerServe> selectByParams(CustomerServeQuery customerServeQuery);
}
package com.study.crm.dao;

import com.study.crm.vo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User,Integer> {
     /**
      * 通过username查询用户
      * 返回user对象
      * @param userName
      * @return
      */
     public User queryByUserByName(String userName);


    List<Map<String, Object>> queryAllCustomerManager();
}
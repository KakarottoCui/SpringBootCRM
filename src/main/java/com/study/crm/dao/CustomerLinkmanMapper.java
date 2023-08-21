package com.study.crm.dao;

import com.study.crm.vo.CustomerLinkman;
import org.apache.ibatis.annotations.Param;

public interface CustomerLinkmanMapper extends BaseMapper<CustomerLinkman,Integer> {


    CustomerLinkman queryLinkManByCustomerId(@Param("cid") Integer cid);
}
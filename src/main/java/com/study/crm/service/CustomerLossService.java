package com.study.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.crm.base.BaseService;
import com.study.crm.dao.CustomerLossMapper;
import com.study.crm.query.CustomerLossQuery;
import com.study.crm.utils.AssertUtil;
import com.study.crm.vo.CustomerContact;
import com.study.crm.vo.CustomerLoss;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerLossService extends BaseService<CustomerLoss,Integer> {
    @Resource
    private CustomerLossMapper customerLossMapper;

    public Map<String, Object> queryCustomerLossAll(CustomerLossQuery customerLossQuery) {
        Map<String,Object> map=new HashMap<String,Object>();
        PageHelper.startPage(customerLossQuery.getPage(),customerLossQuery.getLimit());
        PageInfo<CustomerContact> pageInfo=new PageInfo<CustomerContact>(customerLossMapper.queryCustomerLossAll(customerLossQuery));
        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return  map;
    }

    //添加流失顾客

    /***
     * 将state该为0
     * @param id
     * @param lossReason
     */
    public void updateCustomerLossStateById(Integer id, String lossReason) {
        CustomerLoss customerLoss = customerLossMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(null == customerLoss,"待流失的客户记录不存在!");
        customerLoss.setState(0);// 0.确认流失
        customerLoss.setLossReason(lossReason);
        customerLoss.setConfirmLossTime(new Date());
        customerLoss.setUpdateDate(new Date());
        AssertUtil.isTrue(customerLossMapper.updateByPrimaryKeySelective(customerLoss) < 1,"确认流失失败!");
    }
}

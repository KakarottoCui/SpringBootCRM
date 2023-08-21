package com.study.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.crm.base.BaseService;
import com.study.crm.dao.CustomerContactMapper;
import com.study.crm.query.CustomerContactQuery;
import com.study.crm.utils.AssertUtil;
import com.study.crm.vo.CustomerContact;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerContactService extends BaseService<CustomerContact,Integer> {
    @Resource
    private CustomerContactMapper customerContactMapper;

    /***
     * 查询t_customer_contact
     * @param customerContactQuery
     * @return
     */
    public Map<String, Object> queryCustomerContactAll(CustomerContactQuery customerContactQuery) {
      //  System.out.println(customerContactQuery.getCusId()+"cutomer");
        Map<String,Object> map=new HashMap<String,Object>();
        PageHelper.startPage(customerContactQuery.getPage(),customerContactQuery.getLimit());
        PageInfo<CustomerContact> pageInfo=new PageInfo<CustomerContact>(customerContactMapper.queryCustomerContactAll(customerContactQuery));
        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return  map;
    }

    /***
     * 增加客户联系记录
     * 1. 参数检验
     *    contact_time 非空
     *    address 非空
     * 2. 设置默认值
     *    updateDate
     * 3.调用dao层方法
     * @param customerContact
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCustomerContact(CustomerContact customerContact){
        AssertUtil.isTrue(customerContact.getContactTime()==null,"交往时间不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(customerContact.getAddress()),"交往地址不能为空");
        customerContact.setUpdateDate(new Date());
        customerContact.setUpdateDate(new Date());
        AssertUtil.isTrue(customerContactMapper.insertSelective(customerContact)!=1,"添加用户联系信息失败");

    }

    /***
     * 更新联系信息
     * @param customerContact
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCustomerContact(CustomerContact customerContact) {
        AssertUtil.isTrue(null==customerContact.getId(),"待更新用户不存在");
        AssertUtil.isTrue(null==customerContactMapper.selectByPrimaryKey(customerContact.getId()),"待更新用户不存在");
        AssertUtil.isTrue(customerContact.getContactTime()==null,"交往时间不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(customerContact.getAddress()),"交往地址不能为空");
        customerContact.setUpdateDate(new Date());
        AssertUtil.isTrue(customerContactMapper.updateByPrimaryKeySelective(customerContact)!=1,"更新用户联系信息失败");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteCustomerContact(Integer id) {
        AssertUtil.isTrue(null==id,"待删除用户不存在");
        CustomerContact customerContact=customerContactMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(null==customerContact,"待删除用户不存在");
        customerContact.setIsValid(0);
        AssertUtil.isTrue(customerContactMapper.updateByPrimaryKeySelective(customerContact)!=1,"删除用户失败");

    }
}

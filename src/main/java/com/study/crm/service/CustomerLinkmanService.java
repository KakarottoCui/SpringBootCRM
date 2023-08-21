package com.study.crm.service;

import com.study.crm.base.BaseService;
import com.study.crm.dao.CustomerLinkmanMapper;
import com.study.crm.dao.CustomerMapper;
import com.study.crm.utils.AssertUtil;
import com.study.crm.utils.PhoneUtil;
import com.study.crm.vo.CustomerLinkman;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class CustomerLinkmanService extends BaseService<CustomerLinkman,Integer> {
    @Resource
    private CustomerLinkmanMapper customerLinkmanMapper;

    /***
     * 通过customer_id查询linkman
     * @param cid
     * @return
     */
    public CustomerLinkman queryLinkManByCustomerId(Integer cid) {
        AssertUtil.isTrue(cid==null,"客户信息不存在");
        CustomerLinkman customerLinkman=customerLinkmanMapper.queryLinkManByCustomerId(cid);
        if(customerLinkman!=null){
            System.out.println("11");
            return customerLinkman;
        }else{
            System.out.println("22");
            return null;
        }

    }

    /***
     * 更新联系人信息
     * 1.检验参数
     *   linkName不为空
     *   phone不为空且格式正确
     *   position
     * 2.设置默认值
     *  updateDate
     * 3.调用mapper方法
     * @param customerLinkman
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateLinkman(CustomerLinkman customerLinkman){
        AssertUtil.isTrue(StringUtils.isBlank(customerLinkman.getLinkName()),"请指定联系人名称!");
        AssertUtil.isTrue(StringUtils.isBlank(customerLinkman.getPhone()),"手机号码不能为空");
        AssertUtil.isTrue(!(PhoneUtil.isMobile(customerLinkman.getPhone())),"手机号格式非法!");
        AssertUtil.isTrue(StringUtils.isBlank(customerLinkman.getPosition()),"请指定联系人职位!");
        customerLinkman.setUpdateDate(new Date());
        AssertUtil.isTrue(customerLinkmanMapper.updateByPrimaryKeySelective(customerLinkman)!=1,"客户联系人信息更新失败");
    }
}

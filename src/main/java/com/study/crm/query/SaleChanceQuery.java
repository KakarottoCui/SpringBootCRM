package com.study.crm.query;

import com.study.crm.base.BaseQuery;

public class SaleChanceQuery extends BaseQuery {
    //分页参数
    //条件查询
    private String customerName;
    private String createMan;
    private Integer state;//分配状态  0表示未分配，1表示已分配
    //客户开发计划中的条件查询
    private String devResult;
    private Integer assignMan;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getDevResult() {
        return devResult;
    }

    public void setDevResult(String devResult) {
        this.devResult = devResult;
    }

    public Integer getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(Integer assignMan) {
        this.assignMan = assignMan;
    }
}

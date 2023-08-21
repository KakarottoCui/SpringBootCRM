package com.study.crm.query;

import com.study.crm.base.BaseQuery;

public class CustomerServeQuery extends BaseQuery {

    private String customer;
    // 服务类型
    private Integer serveType;
    // 服务状态
    private String state;

    // 服务分配人
    private Integer assigner;

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Integer getServeType() {
        return serveType;
    }

    public void setServeType(Integer serveType) {
        this.serveType = serveType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getAssigner() {
        return assigner;
    }

    public void setAssigner(Integer assigner) {
        this.assigner = assigner;
    }
}

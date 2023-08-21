package com.study.crm.query;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.study.crm.base.BaseQuery;

public class CustomerContactQuery extends BaseQuery {
    private Integer cusId;

    public Integer getCusId() {
        return cusId;
    }

    public void setCusId(Integer cusId) {
        this.cusId = cusId;
    }
}

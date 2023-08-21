package com.study.crm.query;

import com.study.crm.base.BaseQuery;

public class CustomerQuery extends BaseQuery {
    private String Name;
    private String customerId;
    private String level;

    private String time;

    // 金额区间  1:0-1000   2:1000-3000  3:3000-5000 4:5000+
    private Integer type;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}

package com.study.crm.model;

/***
 * 树性结构所需要的字段
 * id就是子段id
 * pId就是父节点的id
 * name就是子段名称
 */
public class TreeModel {
    private Integer id;
    private Integer pId;
    private String name;
    //复选框是否选中
    private boolean checked=false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}

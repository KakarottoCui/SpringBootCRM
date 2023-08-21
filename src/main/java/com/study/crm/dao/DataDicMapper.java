package com.study.crm.dao;

import com.study.crm.query.DataDicQuery;
import com.study.crm.vo.DataDic;

public interface DataDicMapper extends BaseMapper<DataDic,Integer> {

    Object selectDataDicByParams(DataDicQuery dataDicQuery);
}
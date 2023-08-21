package com.study.crm.dao;

import com.study.crm.model.TreeModel;
import com.study.crm.vo.Module;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModuleMapper  extends BaseMapper<Module,Integer>{
    public List<TreeModel> queryAllModules();

    public List<Module> queryAllModuleList();

    Module queryModuleByGradeAndModuleName(@Param("grade") Integer grade, @Param("moduleName") String moduleName);

    Module queryModuleByGradeAndUrl(@Param("grade")Integer grade, @Param("url")String url);

    Module queryModuleByOptValue(@Param("optValue")String optValue);

    Integer queryModuleByParentId(Integer id);
}
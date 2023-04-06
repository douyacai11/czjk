package com.douya.service;


//服务接口

import com.douya.entity.PageResult;
import com.douya.entity.QueryPageBean;
import com.douya.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
     void add(CheckItem checkItem);

     //分页
    PageResult pageQuery(QueryPageBean queryPageBean);

    void deleteById(Integer id);



    //编辑信息的回显操作
    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);

    //查询所有的检查项
    List<CheckItem> findAll();
}

package com.douya.service;

import com.douya.entity.PageResult;
import com.douya.entity.QueryPageBean;
import com.douya.pojo.CheckGroup;

import java.util.List;


public interface CheckGroupService {
     void add(CheckGroup checkGroup, Integer[] checkitemIds);

     PageResult pageQuery(QueryPageBean queryPageBean);

     CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    void deleteById(Integer id);

    List<CheckGroup> findAll();
}

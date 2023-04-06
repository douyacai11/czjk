package com.douya.dao;

import com.douya.pojo.CheckGroup;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
    void add(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(Map map);

    Page<CheckGroup> findByCondition(String queryString);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup);
    void deleteAssocication(Integer id);


    void deleteById(Integer id);
    long findCountByCheckGroupId(Integer id);  //在中间表中查找检查项和检查组是否已经关联了

    List<CheckGroup> findAll();

}

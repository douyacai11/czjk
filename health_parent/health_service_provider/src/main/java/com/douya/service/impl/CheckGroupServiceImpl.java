package com.douya.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.douya.dao.CheckGroupDao;
import com.douya.entity.PageResult;
import com.douya.entity.QueryPageBean;
import com.douya.pojo.CheckGroup;
import com.douya.service.CheckGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**检查组服务实现
 * */

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;


    //建立检查组和检查项的多对多的关系
    public void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds){
        if (checkitemIds !=null && checkitemIds.length > 0){
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> map=new HashMap<String, Integer>();
                map.put("checkgroup_id",checkGroupId);
                map.put("checkitem_id",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

     /**  新增检查组,同时需要让检查组关联检查项
      * 1.新增检查组 ，操作t_checkgroup表
      * 2.设置检查组和检查项的多对多的关系，操作的是中间表t_checkgroup_checkitem */
     public void add(CheckGroup checkGroup, Integer[] checkitemIds) {

         checkGroupDao.add(checkGroup);

         //获取刚刚插入的检查组的id 需要在dao层使用mybatis框架设置selectKey获取
         Integer checkGroupId = checkGroup.getId();
         this.setCheckGroupAndCheckItem(checkGroupId,checkitemIds);

     }

     //分页助手
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //调用分页助手
        PageHelper.startPage(currentPage,pageSize);
        //再掉dao层的
        Page<CheckGroup> page= checkGroupDao.findByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());

    }


    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }


    //根据检查组id查询关联的检查项id  (查询中间表
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }



    /** 编辑检查组信息，同时需要关联检查项
     *  1.修改检查组的基本信息  t_checkgroup
     *  2.请理检查组关联的检查项  t_checkgroup_checkitem
     *  3.重新建立检查组和检查项的关联关系  （新增已经写过 直接复制*/
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.edit(checkGroup);
        checkGroupDao.deleteAssocication(checkGroup.getId());

        //获取刚刚插入的检查组的id 需要在dao层使用mybatis框架设置selectKey获取
        Integer checkGroupId = checkGroup.getId();
        this.setCheckGroupAndCheckItem(checkGroupId,checkitemIds);
    }

    /**删除检查组
     * 先判断检查项是否已经关联了检查组，若有关联则不允许删除
     * 即 中间表中可以查到检查组的id 这条检查组就不能删除*/
    public void deleteById(Integer id) {
        //判断检查组是否已经关联到检查项了 ，若检查项中有，则不能删除
        long count = checkGroupDao.findCountByCheckGroupId(id);
        if (count>0){
            //count>0 说明检查项和检查组是关联的不可以删除
            new RuntimeException();
        }

        checkGroupDao.deleteById(id);
    }

    public List<CheckGroup> findAll() {
       return checkGroupDao.findAll();
    }
}

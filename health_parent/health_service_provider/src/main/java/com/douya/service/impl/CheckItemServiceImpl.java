package com.douya.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.douya.dao.CheckItemDao;
import com.douya.entity.PageResult;
import com.douya.entity.QueryPageBean;
import com.douya.entity.Result;
import com.douya.pojo.CheckItem;
import com.douya.service.CheckItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**检查项服务
 * */

@Service(interfaceClass = CheckItemService.class)
@Transactional  //事务
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;


    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
/**
 *  完成分页查询，基于Mybatis分页助手插件实现分页
        select * from t_checkitem limit 0,10 (这里的0是currentPage，10是pageSize
        PageHelper.startPage(currentPage,pageSize); 这里的参数是执行上面的查询语句的
*/
        PageHelper.startPage(currentPage,pageSize);

        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        //分页助手它也会帮我们查询到rows+total
        long total = page.getTotal();
        List<CheckItem> rows = page.getResult();
        return new PageResult(total,rows);
    }

    public void deleteById(Integer id) {
        //判断检查项是否已经关联到检查组了 ，若检查组中有，则不能删除
        long count = checkItemDao.findCountByCheckItemId(id);
        if (count>0){
            //count>0 说明检查项和检查组是关联的不可以删除
            new RuntimeException();
        }
        checkItemDao.deleteById(id);
    }

    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}

package com.douya.service.impl;

/**套餐
 * */
import com.alibaba.dubbo.config.annotation.Service;
import com.douya.constant.RedisConstant;
import com.douya.dao.CheckGroupDao;
import com.douya.dao.SetmealDao;
import com.douya.entity.PageResult;
import com.douya.entity.QueryPageBean;
import com.douya.pojo.Setmeal;
import com.douya.service.SetmealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass =SetmealService.class )
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${out_put_path}")
    private  String outPutPath;   //从属性文件中读取生成对应的html目录

    /** 新增套餐:（套餐Setmeal是很多个检查组的集合，他们是多对多的关系
     * 1.insert插入
     * 2.设置两表之间的多对多的关联*/
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.add(setmeal);
        Integer setmealId = setmeal.getId();     //返回数据库中套餐插入的数据的 id
        this.setSetmealAndCheckGroup(setmealId,checkgroupIds);   //设置两表之间的多对多的关联

        //将图片名称保存到redis集合
        String fileName = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,fileName);


        //2.新增套餐后需要重新生成静态页面  (套餐详情+套餐列表
        generateMobileStaticHtml();


    }

    //用于当前方法所需的静态页面  (套餐详情+套餐列表
    public void   generateMobileStaticHtml(){
        //准备模板文件中所需的数据
        List<Setmeal> list = setmealDao.findAll();

        //生成套餐列表静态页面
        generateMobileSetmealListHtml(list);
        //生成套餐详情静态页面（多个）
        generateMobileSetmealDetailHtml(list);
    }

    //生成套餐列表静态页面
    public void generateMobileSetmealListHtml(List<Setmeal> list){
       Map map = new HashMap();
       //为模板提供数据  ，用于生成静态页面
       map.put("setmealList",list);
       generateHtml("mobile_setmeal.ftl","m_setmeal.html",map);
    }

    //生成套餐详情静态页面（多个）
    public void generateMobileSetmealDetailHtml(List<Setmeal> list){
        for (Setmeal setmeal : list) {
            Map map=new HashMap();
            map.put("setmeal",setmealDao.findById(setmeal.getId()));
            generateHtml("mobile_setmeal_detail.ftl","setmeal_detail_"+setmeal.getId()+".html",map);
        }
    }
    //通用的方法,用于生成静态页面
    public void generateHtml(String templateName, String htmlPageName, Map map){
        Configuration configuration = freeMarkerConfigurer.getConfiguration();  //获得page对象
        Writer out =null;
        try {
            // 加载模版文件
            Template template = configuration.getTemplate(templateName);
            //构造输出流
            out=new FileWriter(new File(outPutPath+"/"+htmlPageName));
            //输出文件
            template.process(map, out);

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //设置套餐和检查组的多对多关系，操作的是中间表 t_setmeal_checkgroup
    public void setSetmealAndCheckGroup(Integer setmealId,Integer[] checkgroupIds){
       if (checkgroupIds!=null && checkgroupIds.length>0){

           for (Integer checkgroupId : checkgroupIds) {
               HashMap<String, Integer> map = new HashMap<String, Integer>();
               map.put("setmealId",setmealId);
               map.put("checkgroupId",checkgroupId);
               setmealDao.setSetmealAndCheckGroup(map);
           }

       }
    }


    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setmealDao.findByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }


    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }


/**  根据id查询套餐详情 (套餐基本信息 +套餐包含的检查组+ 检查项
 *   设计多张表  sql语句比较麻烦 */
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }
}

package com.douya.controller;


/** 体检套餐管理( 是检查组的一个集合
 * */
import com.alibaba.dubbo.config.annotation.Reference;
import com.douya.constant.MessageConstant;
import com.douya.constant.RedisConstant;
import com.douya.entity.PageResult;
import com.douya.entity.QueryPageBean;
import com.douya.entity.Result;
import com.douya.pojo.Setmeal;
import com.douya.service.SetmealService;
import com.douya.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference   //远程查找服务
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;


    //使用JedisPool
    //文件上传
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile){
        System.out.println(imgFile);
        String originalFilename = imgFile.getOriginalFilename();  //图片全称
        int index = originalFilename.lastIndexOf(".");        //截取图片全称的后缀名
        String extention = originalFilename.substring(index - 1);  //.jpg
        String fileName= UUID.randomUUID().toString()+extention;
        try {
            //将文件上传到七牛云服务器
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);  //fileName赋值给Result中的data了
    }


    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,  Integer[] checkgroupIds){
        try {
            setmealService.add(setmeal,checkgroupIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
        return  new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }


    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return   setmealService.findPage(queryPageBean);
    }
}

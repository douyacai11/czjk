package com.douya.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.douya.constant.MessageConstant;
import com.douya.entity.PageResult;
import com.douya.entity.QueryPageBean;
import com.douya.entity.Result;
import com.douya.pojo.CheckGroup;
import com.douya.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 检查组管理 (是检查项的一个集合
 * */


@RestController               // 把返回的java对象转化为json对象响应到页面中
@RequestMapping("/checkgroup")
public class CheckGroupController {
     @Reference               //查找CheckGroupService服务
     private CheckGroupService checkGroupService;

    //新增检查组
    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
       try {
           checkGroupService.add(checkGroup,checkitemIds);
       }catch (Exception e){
           e.printStackTrace();
           return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
       }
          return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }


    //分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return   checkGroupService.pageQuery(queryPageBean);
    }

    //编辑操作的第一步 查询检查组信息回显 根据id查询检查组信息 回显信息findById,回显数据都需要返回结果
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
         CheckGroup checkGroup=checkGroupService.findById(id);
         return  new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    //编辑操作的第二步 复选框回显 根据检查组id查询检查组包含的多个检查项id  findCheckItemIdsByCheckGroupId
    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(Integer id){
        try {
          List<Integer> checkitemIds =checkGroupService.findCheckItemIdsByCheckGroupId(id);
            return  new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkitemIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }


    //编辑操作的第三步 点击确定，把修改的数据提交给后端
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try {
            checkGroupService.edit(checkGroup,checkitemIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }


    //删除操作
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            checkGroupService.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }


    //查询所有的检查组信息
    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<CheckGroup> list=checkGroupService.findAll();
            return  new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
}

package com.douya.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.douya.pojo.Permission;
import com.douya.pojo.Role;
import com.douya.pojo.User;
import com.douya.dao.PermissionDao;
import com.douya.dao.RoleDao;
import com.douya.dao.UserDao;
import com.douya.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * 用户服务
 * */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;


//    根据用户名查询数据库中的用户信息和关联的角色信息，同时需要查询角色关联的权限信息
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);   //查询用户基本信息，不包含用户的角色
        if (user==null){
            return null;
        }

        Integer userId = user.getId();
        //根据用户id查询对应的角色
        Set<Role> roles = roleDao.findByUserId(userId);
        for (Role role : roles) {
            Integer roleId = role.getId();
            //根据角色id来查权限
            Set<Permission> permissions = permissionDao.findByRoleId(roleId);
            role.setPermissions(permissions);   //让角色关联权限
        }

        user.setRoles(roles);    //让用户关联角色
        return user;
    }
}

package com.douya.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.douya.pojo.Permission;
import com.douya.pojo.Role;
import com.douya.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {
    //使用dubbo远程调用服务提供方
    @Reference
    private UserService userService;

    //根据用户名查询数据库获取用户信息
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user==null){  //用户名不存在
            return null;
        }

        //动态为用户授权
        List<GrantedAuthority> list=new ArrayList<GrantedAuthority>();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            //01遍历role角色集合，为用户授予角色
            list.add(new SimpleGrantedAuthority(role.getKeyword()));


            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                //02遍历permission权限集合，为用户授权
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        org.springframework.security.core.userdetails.User securityUser=new org.springframework.security.core.userdetails.User(username,user.getPassword(),list);
        return securityUser;
    }
}

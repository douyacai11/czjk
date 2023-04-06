package com.douya.dao;

import com.douya.pojo.Role;

import java.util.Set;

public interface RoleDao {
    public Set<Role> findByUserId(Integer id);
}

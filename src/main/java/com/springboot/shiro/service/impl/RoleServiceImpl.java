package com.springboot.shiro.service.impl;

import com.springboot.shiro.dao.RoleDao;
import com.springboot.shiro.domain.Role;
import com.springboot.shiro.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;

    @Override
    public Role findById(Integer id) {
        return roleDao.selectById(id);
    }
}

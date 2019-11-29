package com.springboot.shiro.service.impl;

import com.springboot.shiro.dao.PermissionDao;
import com.springboot.shiro.domain.Permission;
import com.springboot.shiro.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionDao permissionDao;
    @Override
    public List<Permission> findByRoleId(Integer roleId) {
        return permissionDao.selectPermissionListByRoleId(roleId);
    }
}

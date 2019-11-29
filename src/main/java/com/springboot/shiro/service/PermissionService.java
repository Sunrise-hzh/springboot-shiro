package com.springboot.shiro.service;

import com.springboot.shiro.domain.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> findByRoleId(Integer roleId);
}

package com.springboot.shiro.dao;

import com.springboot.shiro.domain.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionDao {

    @Select("select * from t_permission where role_id = #{roleId}")
    List<Permission> selectPermissionListByRoleId(Integer roleId);
}

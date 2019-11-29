package com.springboot.shiro.dao;

import com.springboot.shiro.domain.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RoleDao {

    @Select("select * from t_role where id = #{id}")
    Role selectById(@Param("id") Integer id);
}

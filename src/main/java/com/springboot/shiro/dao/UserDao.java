package com.springboot.shiro.dao;

import com.springboot.shiro.domain.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

@Mapper
public interface UserDao {
    @Select("select * from t_user where username = #{username}")
    User selectByUsername(String username);

    //批量删除
    @Update({"<script>",
        "update t_user set dr=1",
        "<where>",
        "   dr = 0 AND",
        "   username IN ",
        "       <foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>",
        "           #{item}",
        "       </foreach>",
        "</where>",
    "</script>"})
    Integer deleteAll(List<String> usernameList);
}

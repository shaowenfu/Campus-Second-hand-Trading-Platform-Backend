package com.trade.mapper;

import com.trade.entity.Marketer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {

    /**
     * 根据用户名查询管理员
     * @param username
     * @return
     */
    @Select("select * from marketer where authority = 1 and username = #{username}")
    Marketer getByUsername(String username);

}

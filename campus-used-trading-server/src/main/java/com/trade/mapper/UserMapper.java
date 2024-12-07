package com.trade.mapper;

import com.trade.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {
    /**
     * 查询用户和新增用户数量（全平台）
     * @param map
     * @return
     */
    Integer countByMap(Map map);

    @Select("select * from user where id = #{id}")
    User getUserById(Long id);
}

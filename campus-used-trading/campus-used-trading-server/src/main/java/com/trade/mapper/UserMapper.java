package com.trade.mapper;

import com.trade.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user WHERE openid = #{openid}")
    User getByOpenid(String openid);

    void insert(User user);
    @Select("select * from user where id = #{id}")
    User getById(Long userId);
}

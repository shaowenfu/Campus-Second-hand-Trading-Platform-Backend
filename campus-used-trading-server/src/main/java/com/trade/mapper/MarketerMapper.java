package com.trade.mapper;

import com.trade.entity.Marketer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MarketerMapper {

    /**
     * 查找普通商家
     * @param username
     * @return
     */
    @Select("select * from marketer where authority = 0 and username = #{username}")
    public Marketer getByUsername(String username);

    /**
     * 更新信息
     * @param marketer
     */
    void update(Marketer marketer);

    /**
     * 根据Id查商户
     * @param marketerId
     * @return
     */
    @Select("select * from marketer where id = #{markererId}")
    Marketer getById(Long marketerId);
}

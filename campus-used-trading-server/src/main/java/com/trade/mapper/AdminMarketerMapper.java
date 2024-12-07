package com.trade.mapper;

import com.github.pagehelper.Page;
import com.trade.dto.MarketerPageQueryDTO;
import com.trade.entity.Marketer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMarketerMapper {

    /**
     * 通过商户Id查找商户对象
     * @param marketerId
     * @return
     */
    @Select("select * from marketer where authority = 0 and id = #{marketerId}")
    Marketer getById(Long marketerId);

    /**
     * 更新对象
     * @param marketer
     */
    void update(Marketer marketer);

    /**
     * 分类查询
     * @param marketerPageQueryDTO
     * @return
     */
    Page<Marketer> pageQuery(MarketerPageQueryDTO marketerPageQueryDTO);

    @Insert("insert into marketer(name, username, password, phone, status, id_number, authority, create_time, update_time)"+
            "values " +
            "(#{name}, #{username}, #{password}, #{phone}, #{status}, #{idNumber}, #{authority}, #{createTime},#{updateTime})")
    void insert(Marketer marketer);
}

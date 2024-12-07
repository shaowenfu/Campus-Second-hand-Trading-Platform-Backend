package com.trade.mapper;

import com.github.pagehelper.Page;
import com.trade.dto.CategoryPageQueryDTO;
import com.trade.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 更新分类信息
     * @param category
     */
    void update(Category category);

    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 新增类别
     * @param category
     */
    @Insert("insert into category(name, sort, status, create_time, update_time, create_user, update_user)" +
            " VALUES" +
            " (#{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Category category);

    /**
     * 删除分类
     * @param id
     */
    @Delete("delete from category where id = #{id}")
    void delete(Long id);

    List<Category> list();

    @Select("select * from category where name like concat('%',#{name},'%')")
    Category getByName(String name);
}

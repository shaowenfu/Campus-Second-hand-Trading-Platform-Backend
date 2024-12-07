package com.trade.mapper;

import com.github.pagehelper.Page;
import com.trade.dto.CategoryPageQueryDTO;
import com.trade.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CategoryMapper {

    @Select("SELECT * FROM category WHERE sort = #{type}")
    List<Category> getCategoriesByType(Integer type);

    void update(Category category);

    void deleteById(Long id);

    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);
}

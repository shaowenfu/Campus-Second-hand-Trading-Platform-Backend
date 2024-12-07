package com.trade.service;

import com.trade.dto.CategoryDTO;
import com.trade.dto.CategoryPageQueryDTO;
import com.trade.entity.Category;
import com.trade.result.PageResult;

import java.util.List;

public interface CategoryService {
    /**
     * 修改分类信息
     * @param categoryDTO
     */
    void update(CategoryDTO categoryDTO);

    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 更改分类状态（启用/禁止）
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 新增分类
     * @param categoryDTO
     */
    void save(CategoryDTO categoryDTO);

    /**
     * 删除某个分类
     * @param id
     */
    void delete(Long id);

    /**
     * 查询所有分类信息
     * @return
     */
    List<Category> list();
}

package com.trade.service.impl;

import ch.qos.logback.core.status.StatusManager;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.trade.constant.MessageConstant;
import com.trade.constant.StatusConstant;
import com.trade.context.BaseContext;
import com.trade.dto.CategoryDTO;
import com.trade.dto.CategoryPageQueryDTO;
import com.trade.entity.Category;
import com.trade.exception.CategoryNameIsFound;
import com.trade.exception.DeletionNotAllowedException;
import com.trade.mapper.CategoryMapper;
import com.trade.mapper.ThingMapper;
import com.trade.result.PageResult;
import com.trade.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ThingMapper thingMapper;

    /**
     * 修改分类信息
     */
    public void update(CategoryDTO categoryDTO){
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        //修改时间
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.update(category);
    }

    /**
     * 分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO){
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 更新分类状态
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id){
        Category category = Category.builder()
                .status(status)
                .id(id)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();

        categoryMapper.update(category);
    }

    /**
     * 新增分类
     * @param categoryDTO
     */
    public void save(CategoryDTO categoryDTO){
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .sort(categoryDTO.getSort())
                .updateTime(LocalDateTime.now())
                .createTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .createUser(BaseContext.getCurrentId())
                //分类状态默认为禁用状态1
                .status(StatusConstant.ENABLE)
                .build();

        Category byName = categoryMapper.getByName(categoryDTO.getName());
        if(byName != null){
            throw new CategoryNameIsFound(MessageConstant.CATEGORY_NAME_IS_FOUND);
        }

        categoryMapper.insert(category);
    }

    /**
     * 删除某个分类
     * @param id
     */
    public void delete(Long id){
        Integer count = thingMapper.countByCategoryId(id);

        if(count > 0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_THING);
        }

        categoryMapper.delete(id);
    }

    /**
     * 查询所有分类信息
     * @return
     */
    public List<Category> list(){
        return categoryMapper.list();
    }
}

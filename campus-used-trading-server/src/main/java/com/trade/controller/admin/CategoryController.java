package com.trade.controller.admin;

import com.trade.dto.CategoryDTO;
import com.trade.dto.CategoryPageQueryDTO;
import com.trade.entity.Category;
import com.trade.result.PageResult;
import com.trade.result.Result;
import com.trade.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminCategoryController")
@RequestMapping("/admin/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PutMapping
    public Result udpate(@RequestBody CategoryDTO categoryDTO) {
        log.info("{}", categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("categoryPageQueryDTO:{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    public Result status(@PathVariable("status") Integer status, Long id) {
        categoryService.startOrStop(status,id);
        return Result.success();
    }

    @PostMapping("save")
    public Result save(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增类:{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(Long id) {
        categoryService.delete(id);
        return Result.success();
    }

    @GetMapping
    public Result<List<Category>> list() {
        List<Category> categoryList = categoryService.list();
        return Result.success(categoryList);
    }
}

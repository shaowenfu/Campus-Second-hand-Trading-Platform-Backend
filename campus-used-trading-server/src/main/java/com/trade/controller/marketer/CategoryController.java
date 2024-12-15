package com.trade.controller.marketer;


import com.trade.entity.Category;
import com.trade.result.Result;
import com.trade.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("marketerCategoryController")
@RequestMapping("/marketer/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 用于market PageQuery期间使用
     * @return
     */
    @GetMapping
    public Result<List<Category>> list() {
        List<Category> categoryList = categoryService.list();
        return Result.success(categoryList);
    }
}

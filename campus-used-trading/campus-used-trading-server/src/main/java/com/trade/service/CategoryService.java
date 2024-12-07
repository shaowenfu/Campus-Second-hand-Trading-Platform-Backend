package com.trade.service;

import com.trade.entity.Category;
import java.util.List;

public interface CategoryService {
    List<Category> list(Integer type);
}

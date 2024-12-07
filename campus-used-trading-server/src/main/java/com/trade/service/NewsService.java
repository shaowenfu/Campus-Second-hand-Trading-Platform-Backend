package com.trade.service;

import com.trade.dto.NewsPageQueryDTO;
import com.trade.entity.News;
import com.trade.result.PageResult;

import java.util.List;

public interface NewsService {
    /**
     * 删除新闻
     * @param id
     */
    void delete(Long id);

    /**
     * 新增新闻
     * @param news
     */
    void save(News news);

    /**
     * 分页查找
     * @param newsPageQueryDTO
     * @return
     */
    PageResult pageQuery(NewsPageQueryDTO newsPageQueryDTO);

    /**
     * 根据id查询新闻
     * @param id
     * @return
     */
    News getById(Long id);

    /**
     * 修改新闻
     * @param id
     * @param details
     */
    void update(Long id, String details);

    /**
     * 查询所有信息
     * @return
     */
    List<News> list();
}

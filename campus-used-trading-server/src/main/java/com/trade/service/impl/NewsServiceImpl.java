package com.trade.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.trade.constant.MessageConstant;
import com.trade.dto.NewsPageQueryDTO;
import com.trade.entity.News;
import com.trade.exception.NewsNotFoundException;
import com.trade.mapper.NewsMapper;
import com.trade.result.PageResult;
import com.trade.result.Result;
import com.trade.service.NewsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsMapper newsMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    //需要更新redis
    public void delete(Long id) {
        newsMapper.delete(id);
        //redis清空缓存
        Set keys = redisTemplate.keys("news_*");
        redisTemplate.delete(keys);
    }

    //新增新闻（清空redis缓存）
    public void save(News news){
        newsMapper.save(news);
        //redis清空缓存
        Set keys = redisTemplate.keys("news_*");
        redisTemplate.delete(keys);
    }

    /**
     * 更新新闻状态
     * @param news
     */
    public void updateStatus(News news){
        News news1 = newsMapper.getById(news.getId());
        if(news == null){
            throw new NewsNotFoundException(MessageConstant.NEWS_NOT_FOUND);
        }
        news1.setStatus(news.getStatus());
        newsMapper.update(news1);
    }

    /**
     * 分页查找（不用清空redis）
     * @param newsPageQueryDTO
     * @return
     */
    public PageResult pageQuery(NewsPageQueryDTO newsPageQueryDTO){
        PageHelper.startPage(newsPageQueryDTO.getPage(),newsPageQueryDTO.getPageSize());
        Page<News> page =  newsMapper.pageQuery(newsPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根据id查询新闻
     * @return
     */
    public News getById(Long id){
        return newsMapper.getById(id);
    }

    /**
     * 修改新闻
     * @param id
     * @param detail
     */
    public void update(Long id, String detail){
        News news = newsMapper.getById(id);
        if(news == null){
            throw new NewsNotFoundException(MessageConstant.NEWS_NOT_FOUND);
        }

        News news1 = new News();
        BeanUtils.copyProperties(news,news1);
        news1.setDetail(detail);
        newsMapper.update(news1);
    }

    /**
     * 查看所有新闻
     * @return
     */
    public List<News> list(){
        return newsMapper.list();
    }
}

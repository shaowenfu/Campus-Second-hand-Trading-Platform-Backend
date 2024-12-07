package com.trade.controller.user;

import com.trade.entity.News;
import com.trade.result.Result;
import com.trade.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userNewsController")
@RequestMapping("/user/news")
@Slf4j
public class NewsController {
    @Autowired
    private NewsService newsService;

    @GetMapping("/list")
    public Result<List<News>> list(){
        return Result.success(newsService.list());
    }

    /**
     * 查询某个具体新闻
     * @param id
     * @return
     */
    @GetMapping("/list/{id}")
    public Result<News> getByid(@PathVariable("id") Long id) {
        return Result.success(newsService.getById(id));
    }
}

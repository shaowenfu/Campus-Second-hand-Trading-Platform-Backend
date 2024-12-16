package com.trade.controller.admin;

import com.trade.dto.NewsPageQueryDTO;
import com.trade.entity.News;
import com.trade.result.PageResult;
import com.trade.result.Result;
import com.trade.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminNewsController")
@RequestMapping("/admin/news")
@Slf4j
public class NewsController {
    @Autowired
    private NewsService newsService;

    @DeleteMapping
    public Result delete(Long id) {
        newsService.delete(id);
        return Result.success();
    }

    @PostMapping("/save")
    public Result save(News news) {
        newsService.save(news);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult> pgageQuery(NewsPageQueryDTO newsPageQueryDTO) {
        PageResult page = newsService.pageQuery(newsPageQueryDTO);
        return Result.success(page);
    }

    @PostMapping("/updateStatus")
    public Result updateStatus(@RequestBody Long id, @RequestBody Integer status) {
        News news = News.builder()
                .id(id)
                .status(status)
                .build();
        newsService.updateStatus(news);
        return Result.success();
    }

    /**
     * 查看所有新闻
     * @param
     * @return
     */
    @GetMapping("/list")
    public Result<List<News>> list() {
        return Result.success(newsService.list());
    }

    @GetMapping("/list/{id}")
    public Result<News> getByid(@PathVariable("id") Long id) {
        return Result.success(newsService.getById(id));
    }

    /**
     * 修改新闻
     * @param id
     * @param detail
     * @return
     */
    @PutMapping("/{id}")
    public Result update(@PathVariable("id") Long id, @RequestBody String detail){
        newsService.update(id, detail);
        return Result.success();
    }
}

package com.trade.controller.admin;

import com.trade.dto.ThingDTO;
import com.trade.dto.ThingPageQueryDTO;
import com.trade.entity.Thing;
import com.trade.result.PageResult;
import com.trade.result.Result;
import com.trade.service.ThingService;
import com.trade.vo.ThingVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController("adminThingController")
@RequestMapping("/admin/thing")
@Slf4j
public class ThingController {

    @Autowired
    private ThingService thingService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 修改商品
     * @param thingDTO
     * @return
     */
    @PutMapping
    public Result update(ThingDTO thingDTO) {
        log.info("修改商品 {}", thingDTO);
        thingService.update(thingDTO);
        //redis清空缓存
        Set keys = redisTemplate.keys("thing_*");
        redisTemplate.delete(keys);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable("status") Integer status, Long id) {
        thingService.startOrStop(status,id);
        Set keys = redisTemplate.keys("thing_*");
        redisTemplate.delete(keys);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<ThingVO> getById(@PathVariable("id") Long id) {
        log.info("根据id查询菜品：{}",id);
        ThingVO thingVO = thingService.getByid(id);
        return Result.success(thingVO);
    }

    @GetMapping("/list")
    public Result<List<Thing>> list(Long categoryId) {
        log.info("查询categoryId：{}",categoryId);
        List<Thing> thingList = thingService.getBycategoryId(categoryId,null);
        return Result.success(thingList);
    }

    @GetMapping("/page")
    public Result<PageResult> page(ThingPageQueryDTO thingPageQueryDTO) {
        log.info("商品分页查询：{}",thingPageQueryDTO);
        PageResult pageResult = thingService.pageQuery(thingPageQueryDTO);
        return Result.success(pageResult);
    }
}

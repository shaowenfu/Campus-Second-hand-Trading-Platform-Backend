package com.trade.controller.marketer;

import com.trade.context.BaseContext;
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

@RestController("marketerThingController")
@RequestMapping("/marketer/thing")
@Slf4j
public class ThingController {
    @Autowired
    private ThingService thingService;
    @Autowired
    private RedisTemplate redisTemplate;

    @DeleteMapping
    public Result delete(List<Long> ids) {
        log.info("菜品批量删除");
        thingService.deleteBatch(ids);
        //将所有的菜品缓存数据清理掉

        Set keys = redisTemplate.keys("thing_*");
        redisTemplate.delete(keys);
        return Result.success();
    }

    @PostMapping
    public Result save(ThingDTO thingDTO) {
        log.info("新增商品：{}", thingDTO);
        thingService.save(thingDTO);

        //清理缓存数据
        String key = "thing_" + thingDTO.getCategoryId();
        redisTemplate.delete(key);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<ThingVO> hetById(@PathVariable("id") Long id) {
        log.info("根据id查询菜品：{}",id);
        ThingVO thingVO = thingService.getByid(id);
        return Result.success(thingVO);
    }

    @GetMapping("/list")
    public Result<List<Thing>> list(Long categoryId) {
        log.info("查询categoryId：{}",categoryId);
        List<Thing> thingList = thingService.getBycategoryId(categoryId, BaseContext.getCurrentId());
        return Result.success(thingList);
    }

    @GetMapping("/page")
    public Result<PageResult> page(ThingPageQueryDTO thingPageQueryDTO) {
        log.info("商品分页查询：{}",thingPageQueryDTO);
        PageResult pageResult = thingService.pageQuery(thingPageQueryDTO, BaseContext.getCurrentId());
        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable("status") Integer status, Long id) {
        thingService.startOrStop(status,id);
        Set keys = redisTemplate.keys("thing_*");
        redisTemplate.delete(keys);
        return Result.success();
    }

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
}

package com.trade.controller.user;

import com.trade.constant.StatusConstant;
import com.trade.entity.Thing;
import com.trade.result.Result;
import com.trade.service.ThingService;
import com.trade.vo.ThingVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userThingController")
@RequestMapping("/user/thing")
@Slf4j
@Api(tags = "C端-商品浏览接口")
public class ThingController {
    @Autowired
    private ThingService thingService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查商品")
    public Result<List<ThingVO>> list(Long categoryId) {
        //Redis保存的是ThingVO
        String key="thing_"+categoryId;
        List<ThingVO> list = (List<ThingVO>) redisTemplate.opsForValue().get(key);
        if(list != null && list.size()>0){
            return Result.success(list);
        }

        Thing thing = new Thing();
        thing.setCategoryId(categoryId);
        thing.setStatus(StatusConstant.ENABLE);//查询起售中的商品
        list = thingService.list(thing);

        //更新redis缓存
        redisTemplate.opsForValue().set(key,list);
        return Result.success(list);
    }

}

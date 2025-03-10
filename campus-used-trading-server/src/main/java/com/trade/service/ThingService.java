package com.trade.service;

import com.trade.dto.ThingDTO;
import com.trade.dto.ThingPageQueryDTO;
import com.trade.entity.Thing;
import com.trade.result.PageResult;
import com.trade.vo.ThingVO;

import java.util.List;

public interface ThingService {

    /**
     * 更新菜品
     * @param thingDTO
     */
    void update(ThingDTO thingDTO);

    /**
     * 更新商品状态
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据id查询商品
     * @param id
     */
    ThingVO getByid(Long id);

    /**
     * 根据categoryId查询Thing list
     * @param categoryId
     * @return
     */
    List<Thing> getBycategoryId(Long categoryId, Long marketerId);

    /**
     * 分页查找
     * @param thingPageQueryDTO
     * @return
     */
    PageResult pageQuery(ThingPageQueryDTO thingPageQueryDTO);

    /**
     * 批量删除
     * @param id
     */
    void delete(Long id);

    /**
     * 新增商品
     * @param thingDTO
     */
    void save(ThingDTO thingDTO);

    List<ThingVO> list(Thing thing);
}

package com.trade.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.trade.constant.MessageConstant;
import com.trade.constant.StatusConstant;
import com.trade.context.BaseContext;
import com.trade.dto.ThingDTO;
import com.trade.dto.ThingPageQueryDTO;
import com.trade.entity.Thing;
import com.trade.exception.DeletionNotAllowedException;
import com.trade.mapper.ThingMapper;
import com.trade.result.PageResult;
import com.trade.service.ThingService;
import com.trade.vo.ThingVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ThingServiceImpl implements ThingService {

    @Autowired
    private ThingMapper thingMapper;

    public void update(ThingDTO thingDTO){
        Thing thing = new Thing();
        BeanUtils.copyProperties(thingDTO,thing);
        thingMapper.update(thing);
    }

    /**
     * 更新商品状态
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id){
        Thing thing = Thing.builder().status(status).id(id).build();
        thingMapper.update(thing);
    }

    /**
     * 根据id查询商品
     * @param id
     * @return
     */
    public ThingVO getByid(Long id){
        Thing thing = thingMapper.getById(id);
        ThingVO thingVO = new ThingVO();
        BeanUtils.copyProperties(thing,thingVO);
        return thingVO;
    }

    /**
     * 根据categoryId查询商品List
     * @param categoryId
     * @return
     */
    public List<Thing> getBycategoryId(Long categoryId, Long marketerId){
        return thingMapper.getBycategoryId(categoryId, marketerId);
    }

    /**
     * 分页查找
     * @param thingPageQueryDTO
     * @return
     */
    public PageResult pageQuery(ThingPageQueryDTO thingPageQueryDTO, Long marketerId){
        PageHelper.startPage(thingPageQueryDTO.getPage(),thingPageQueryDTO.getPageSize());
        Page<ThingVO> page = thingMapper.pageQuery(thingPageQueryDTO, marketerId);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 批量删除
     * @param ids
     */
    public void deleteBatch(List<Long> ids){
        //判断当前是否能够删除 -- 是否在售
        for (Long id : ids) {
            Thing dish = thingMapper.getById(id);
            if(dish.getStatus() == StatusConstant.ENABLE) {
                //当前菜品售卖中
                throw new DeletionNotAllowedException(MessageConstant.ThING_ON_SALE);
            }
        }
        //删除商品表中的菜品数据
        for (Long id : ids) {
            thingMapper.delete(id);
        }
    }

    /**
     * 新增商品
     * @param thingDTO
     */
    public void save(ThingDTO thingDTO){
        Thing thing = new Thing();
        BeanUtils.copyProperties(thingDTO, thing);
        thing.setCreateTime(LocalDateTime.now());
        thing.setUpdateTime(LocalDateTime.now());
        thing.setCreateUser(BaseContext.getCurrentId());
        thing.setUpdateUser(BaseContext.getCurrentId());
        thingMapper.insert(thing);
    }
}

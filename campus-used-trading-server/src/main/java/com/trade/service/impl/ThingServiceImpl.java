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
import java.util.ArrayList;
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
    public PageResult pageQuery(ThingPageQueryDTO thingPageQueryDTO){
        PageHelper.startPage(thingPageQueryDTO.getPage(),thingPageQueryDTO.getPageSize());
        Page<ThingVO> page = thingMapper.pageQuery(thingPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 批量删除
     * @param id
     */
    public void delete(Long id){
        //判断当前是否能够删除 -- 是否在售
        Thing thing = thingMapper.getById(id);
        if(thing.getStatus() == StatusConstant.ENABLE) {
            //当前菜品售卖中
            throw new DeletionNotAllowedException(MessageConstant.ThING_ON_SALE);
        }
        //删除商品表中的菜品数据
        thingMapper.delete(id);
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

    @Override
    public List<ThingVO> list(Thing thing) {
        // 从数据库查询符合条件的商品列表
        List<Thing> thingList = thingMapper.list(thing);

        // 创建返回的 ThingVO 列表
        List<ThingVO> thingVOList = new ArrayList<>();

        // 遍历查询到的商品列表，将每个 Thing 转换为 ThingVO
        for (Thing d : thingList) {
            ThingVO thingVO = new ThingVO();
            BeanUtils.copyProperties(d, thingVO);  // 将 Thing 的属性复制到 ThingVO
            thingVOList.add(thingVO);
        }

        // 返回所有的 ThingVO 数据
        return thingVOList;
    }
}

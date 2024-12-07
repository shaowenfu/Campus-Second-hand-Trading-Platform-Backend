package com.trade.service.impl;

import com.trade.entity.Thing;
import com.trade.mapper.ThingMapper;
import com.trade.service.ThingService;
import com.trade.vo.ThingVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ThingServiceImpl implements ThingService {  // 将 'abstract' 移除

    @Autowired
    private ThingMapper thingMapper;

    @Override
    public List<Thing> list(long categoryId) {
        return null;
    }

    @Override
    public List<ThingVO> listWithFlavor(Thing thing) {
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

    private ThingVO convertToThingVO(Thing thing) {
        ThingVO thingVO = new ThingVO();
        thingVO.setId(thing.getId());
        thingVO.setName(thing.getName());
        thingVO.setDescription(thing.getDescription());
        thingVO.setPrice(thing.getPrice());
        return thingVO;
    }
}

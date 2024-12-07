package com.trade.service.impl;

import com.trade.constant.StatusConstant;
import com.trade.context.BaseContext;
import com.trade.entity.Orders;
import com.trade.mapper.OrderMapper;
import com.trade.mapper.ThingMapper;
import com.trade.mapper.UserMapper;
import com.trade.service.WorkspaceService;
import com.trade.vo.BusinessDataVO;
import com.trade.vo.OrderOverViewVO;
import com.trade.vo.ThingOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ThingMapper thingMapper;

    /**
     * 根据时间段统计营业数据
     * @param begin
     * @param end
     * @return
     */
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end){
        /**
         * 营业额：当日已完成订单的总金额
         * 有效订单：当日已完成订单的数量
         * 新增用户：当日新增用户的数量
         */

        Map map = new HashMap();
        map.put("begin",begin);
        map.put("end",end);

        //查询总订单数
        Integer totalOrderCount = orderMapper.countByMap(map);

        map.put("status", Orders.COMPLETED);
        //营业额
        Double turnover = orderMapper.sumByMap(map);
        turnover = turnover == null? 0.0 : turnover;

        //有效订单数
        Integer validOrderCount = orderMapper.countByMap(map);

        Double unitPrice = 0.0;
        Double orderCompletionRate = 0.0;
        if(totalOrderCount != 0 && validOrderCount != 0){
            //订单完成率
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
            //平均客单价
            unitPrice = turnover / validOrderCount;
        }

        //新增用户数(但是这里不能用，因为他不适合（这是全平台新增人数）)
        Integer newUsers = userMapper.countByMap(map);

        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * 获取商品总览
     *
     * @return
     */
    public ThingOverViewVO getOverViewThing(){
        Map map = new HashMap();
        map.put("marketerId", BaseContext.getCurrentId());
        map.put("status", StatusConstant.ENABLE);
        Integer sold = thingMapper.countByMap(map);

        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = thingMapper.countByMap(map);

        return ThingOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * 获取商品售卖情况
     * @return
     */
    public OrderOverViewVO getOverViewOrders(){
        Map map = new HashMap();
        map.put("begin", LocalDateTime.now().with(LocalTime.MIN));
        map.put("status", Orders.TO_BE_CONFIRMED);
        map.put("marketerId", BaseContext.getCurrentId());

        //待处理（已付款）
        Integer unsolvedOrders = orderMapper.countByMap(map);

        //交易中
        map.put("status", Orders.TRADING);
        Integer tradingOrders = orderMapper.countByMap(map);

        //已完成
        map.put("status", Orders.COMPLETED);
        Integer completedOrders = orderMapper.countByMap(map);

        //已取消
        map.put("status", Orders.CANCELLED);
        Integer cancelledOrders = orderMapper.countByMap(map);

        //全部订单
        map.put("status", null);
        Integer allOrders = orderMapper.countByMap(map);

        return OrderOverViewVO.builder()
                .unsolvedOrders(unsolvedOrders)
                .tradingOrders(tradingOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }
}

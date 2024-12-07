package com.trade.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.trade.constant.MessageConstant;
import com.trade.dto.OrdersCancelDTO;
import com.trade.dto.OrdersPageQueryDTO;
import com.trade.entity.OrderDetail;
import com.trade.entity.Orders;
import com.trade.exception.OrderBusinessException;
import com.trade.mapper.OrderDetailMapper;
import com.trade.mapper.OrderMapper;
import com.trade.result.PageResult;
import com.trade.service.OrderService;
import com.trade.vo.OrderStatisticsVO;
import com.trade.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    /**
     * 根据id找order
     * @param ordersCancelDTO
     * @return
     */
    public void cancel(OrdersCancelDTO ordersCancelDTO){
        Orders ordersDB = orderMapper.getById(ordersCancelDTO.getId());

        Integer payStatus = ordersDB.getPayStatus();
        if (payStatus == 1) {
//            //用户已支付，需要退款
//            String refund = weChatPayUtil.refund(
//                    ordersDB.getNumber(),
//                    ordersDB.getNumber(),
//                    new BigDecimal(0.01),
//                    new BigDecimal(0.01));
            log.info("申请退款：{}");
        }

        //更新原因、时间、账单状态
        Orders orders = new Orders();
        orders.setId(ordersCancelDTO.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason(ordersCancelDTO.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * 查看order状态的数量
     */
    public OrderStatisticsVO statistics(Long id){
        Integer toBeConfirmed = orderMapper.countStatus(Orders.TO_BE_CONFIRMED, id);
        Integer confirmed = orderMapper.countStatus(Orders.TRADING, id);

        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setConfirmed(confirmed);
        return orderStatisticsVO;
    }

    /**
     * 根据id查村订单
     * @param id
     * @return
     */
    public OrderVO detials(Long id){
        Orders orders = orderMapper.getById(id);
        List<OrderDetail> orderDetailList = orderDetailMapper.getById(id);

        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailList);
        return orderVO;
    }

    /**
     * 订单搜索
     * @param ordersPageQueryDTO
     * @return
     */
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO){
        PageHelper.startPage(ordersPageQueryDTO.getPage(),ordersPageQueryDTO.getPageSize());
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);

        List<OrderVO> orderVOList = new ArrayList<>();
        List<Orders> orders = page.getResult();
        for (Orders order : orders) {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            //找到对应order对应的orderDetail
            List<OrderDetail> orderDetailList = orderDetailMapper.getById(order.getId());

            List<String> orderThingsList = orderDetailList.stream().map(x->
            {
                String orderThing = x.getName() + "*" + x.getNumber() + ";";
                return orderThing;
            }).collect(Collectors.toList());

            orderVO.setOrderThings(String.join("", orderThingsList));
            orderVOList.add(orderVO);
        }
        return new PageResult(page.getTotal(),orderVOList);
    }

    /**
     * 完成订单
     * @param id
     */
    public void complete(Long id){
        Orders ordersDB = orderMapper.getById(id);

        // 校验订单是否存在，并且状态为3(即为交易中)
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.TRADING)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        // 更新订单状态,状态转为完成
        orders.setStatus(Orders.COMPLETED);
        orderMapper.update(orders);
    }

    /**
     * 接单
     * @param id
     */
    public void confirm(Long id){
        Orders ordersDB = orderMapper.getById(id);

        // 校验订单是否存在，并且状态为3(即为交易中)
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.TRADING);
        orderMapper.update(orders);
    }
}

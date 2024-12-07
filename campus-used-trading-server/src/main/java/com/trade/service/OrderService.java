package com.trade.service;

import com.trade.dto.OrdersCancelDTO;
import com.trade.dto.OrdersPageQueryDTO;
import com.trade.dto.OrdersPaymentDTO;
import com.trade.dto.OrdersSubmitDTO;
import com.trade.result.PageResult;
import com.trade.vo.OrderPaymentVO;
import com.trade.vo.OrderStatisticsVO;
import com.trade.vo.OrderSubmitVO;
import com.trade.vo.OrderVO;
import org.springframework.core.annotation.Order;

public interface OrderService {

    /**
     *
     * @param ordersCancelDTO
     * @return
     */
    void cancel(OrdersCancelDTO ordersCancelDTO);

    /**
     * 统计order订单中的各类数量
     */
    OrderStatisticsVO statistics(Long id);

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    OrderVO detials(Long id);

    /**
     * 订单搜索
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 完成订单
     * @param id
     */
    void complete(Long id);

    /**
     * 接受订单（已付款->交易中）
     * @param id
     */
    void confirm(Long id);
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);
    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);
}

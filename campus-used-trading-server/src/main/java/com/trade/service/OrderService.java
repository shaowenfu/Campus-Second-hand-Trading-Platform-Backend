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

import java.util.List;

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

    /**
     * 提交订单
     * @param ordersSubmitDTO
     * @return
     */
    List<OrderSubmitVO> submitOrder(OrdersSubmitDTO ordersSubmitDTO);
    /**
     * 订单支付
     * @param ordersPaymentDTOList
     * @return
     */
    List<OrderPaymentVO> payment(List<OrdersPaymentDTO> ordersPaymentDTOList) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 查看订单详情
     * @param id
     * @return
     */
    OrderVO details(Long id);

    /**
     * 分页查找
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    PageResult pageQuery4User(int page, int pageSize, Integer status);

    /**
     * 取消订单
     * @param id
     */
    void userCancelById(Long id);
}

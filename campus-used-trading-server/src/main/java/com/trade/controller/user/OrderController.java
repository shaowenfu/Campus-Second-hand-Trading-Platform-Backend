package com.trade.controller.user;

import com.trade.dto.OrdersPaymentDTO;
import com.trade.dto.OrdersSubmitDTO;
import com.trade.result.PageResult;
import com.trade.result.Result;
import com.trade.service.OrderService;
import com.trade.vo.OrderPaymentVO;
import com.trade.vo.OrderSubmitVO;
import com.trade.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags="用户订单相关接口")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<List<OrderSubmitVO>> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("用户下单，参数为：{}",ordersSubmitDTO);
        List<OrderSubmitVO> orderSubmitVOList = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVOList);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTOList
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<List<OrderPaymentVO>> payment(@RequestBody List<OrdersPaymentDTO> ordersPaymentDTOList) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTOList);
        List<OrderPaymentVO> orderPaymentVOList = orderService.payment(ordersPaymentDTOList);
        log.info("生成预支付交易单：{}", orderPaymentVOList);
        return Result.success(orderPaymentVOList);
    }

    /**
     * 历史订单查询
     *
     * @param page
     * @param pageSize
     * @param status   订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
     * @return
     */
    @GetMapping("/historyOrders")
    @ApiOperation("历史订单查询")
    public Result<PageResult> page(int page, int pageSize, Integer status) {
        PageResult pageResult = orderService.pageQuery4User(page, pageSize, status);
        return Result.success(pageResult);
    }

    /**
     * 查询订单详情
     *
     * @param id
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> details(@PathVariable("id") Long id) {
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }

    /**
     * 用户取消订单
     *
     * @return
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public Result cancel(@PathVariable("id") Long id) throws Exception {
        orderService.userCancelById(id);
        return Result.success();
    }

}

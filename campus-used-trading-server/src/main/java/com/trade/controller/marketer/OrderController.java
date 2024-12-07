package com.trade.controller.marketer;


import com.trade.context.BaseContext;
import com.trade.dto.OrdersCancelDTO;
import com.trade.dto.OrdersConfirmDTO;
import com.trade.dto.OrdersPageQueryDTO;
import com.trade.result.PageResult;
import com.trade.result.Result;
import com.trade.service.OrderService;
import com.trade.vo.OrderStatisticsVO;
import com.trade.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("marketerOrderController")
@RequestMapping("/marketer/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 取消订单
     * @param ordersCancelDTO
     * @return
     */
    @PutMapping("/cancel")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        orderService.cancel(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 完成订单
     * @param id
     * @return
     */
    @PutMapping("/complete/{id}")
    public Result complete(@PathVariable Long id) {
        orderService.complete(id);
        return Result.success();
    }

    @PutMapping("/confirm")
    public Result confirm(@RequestBody Long id) {
        orderService.confirm(id);
        return Result.success();
    }

    @GetMapping("/conditionSearch")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> statistics() {
        OrderStatisticsVO orderStatisticsVO = orderService.statistics(BaseContext.getCurrentId());
        return Result.success(orderStatisticsVO);
    }

    @GetMapping("/details/{id}")
    public Result<OrderVO> details(@PathVariable("id") Long id) {
        OrderVO orderVO = orderService.detials(id);
        return Result.success(orderVO);
    }
}

package com.trade.controller.marketer;

import com.trade.context.BaseContext;
import com.trade.result.Result;
import com.trade.service.WorkspaceService;
import com.trade.vo.BusinessDataVO;
import com.trade.vo.OrderOverViewVO;
import com.trade.vo.ThingOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@RestController
@RequestMapping("/marketer/workspace")
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    @GetMapping("/businessData")
    public Result<BusinessDataVO> getBusinessData() {
        //获得当天的开始时间
        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        //获得当天的结束时间
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);
        BusinessDataVO businessData = workspaceService.getBusinessData(begin, end);
        return Result.success(businessData);
    }

    @GetMapping("/overviewThings")
    public Result<ThingOverViewVO> getOverviewThings() {
        ThingOverViewVO overView = workspaceService.getOverViewThing();
        return Result.success(overView);
    }

    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> getOverviewOrders() {
        OrderOverViewVO orderOverViewVO = workspaceService.getOverViewOrders();
        return Result.success(orderOverViewVO);
    }
}

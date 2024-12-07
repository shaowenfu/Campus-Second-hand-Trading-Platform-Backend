package com.trade.service;

import com.trade.vo.BusinessDataVO;
import com.trade.vo.OrderOverViewVO;
import com.trade.vo.ThingOverViewVO;

import java.time.LocalDateTime;

public interface WorkspaceService {

    /**
     * 查询今日营业数据
     * @return
     */
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    /**
     * 获取商品（禁止、启用数量）
     *
     * @return
     */
    ThingOverViewVO getOverViewThing();

    /**
     * 获取商品售卖情况
     * @return
     */
    OrderOverViewVO getOverViewOrders();
}

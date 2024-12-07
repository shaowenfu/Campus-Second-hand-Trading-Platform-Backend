package com.trade.service;

import com.trade.vo.OrderReportVO;
import com.trade.vo.SalesTop10ReportVO;
import com.trade.vo.TurnoverReportVO;

import java.time.LocalDate;

public interface ReportService {

    /**
     * 查找销售前10
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);

    /**
     * 营业额数据统计
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO getTurnoverReport(LocalDate begin, LocalDate end);

    /**
     * 订单数据统计
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);
}

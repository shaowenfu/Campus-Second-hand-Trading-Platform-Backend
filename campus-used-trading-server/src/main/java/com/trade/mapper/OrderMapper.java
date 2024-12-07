package com.trade.mapper;

import com.github.pagehelper.Page;
import com.trade.dto.GoodsSalesDTO;
import com.trade.dto.OrdersPageQueryDTO;
import com.trade.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.core.annotation.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    /**
     * 根据id找order
     * @param id
     * @return
     */
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    /**
     * 更新订单
     * @param orders
     */
    void update(Orders orders);

    /**
     * 统计数据
     * @param status
     * @return
     */
    Integer countStatus(Integer status, Long id);

    /**
     * 分页查找
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据动态条件统计订单数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);

    /**
     * 当日营业额
     * @param map
     * @return
     */
    Double sumByMap(Map map);

    /**
     * 统计指定时间区间内的销量排名前10
     * @param beginTime
     * @param endTime
     * @return
     */
    List<GoodsSalesDTO> getSalesTop10(LocalDateTime beginTime, LocalDateTime endTime);
}

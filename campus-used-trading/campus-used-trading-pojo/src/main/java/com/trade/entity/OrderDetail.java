package com.trade.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单明细
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;  // 主键，自增
    private String name;  // 商品名称
    private String image;  // 商品图片路径
    private Long orderId;  // 订单ID，逻辑外键
    private Long thingId;  // 物品ID，逻辑外键
    private Integer number;  // 商品数量
    private BigDecimal amount;  // 商品单价
}

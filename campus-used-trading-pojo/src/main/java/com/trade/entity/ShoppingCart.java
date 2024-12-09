package com.trade.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;  // 主键，自增
    private String name;  // 商品名称
    private String image;  // 商品图片路径
    private Long userId;  // 用户ID，逻辑外键
    private Long thingId;  // 物品ID，逻辑外键
    private Long marketerId; // 商户ID，逻辑外键
    private BigDecimal price;  // 商品单价
    private Long amount;  // 商品数量
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}

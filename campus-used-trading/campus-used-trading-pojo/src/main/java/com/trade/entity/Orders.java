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
 * 订单
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders implements Serializable {

    /**
     * 订单状态，1为待付款，2为已付款，3为交易中，4为已完成，5为已取消
     */
    public static final Integer PENDING_PAYMENT = 1;
    public static final Integer TO_BE_CONFIRMED = 2;
    public static final Integer TRADING = 3;
    public static final Integer COMPLETED = 4;
    public static final Integer CANCELLED = 5;

    /**
     * 支付状态 0未支付 1已支付 2退款
     */
    public static final Integer UN_PAID = 0;
    public static final Integer PAID = 1;
    public static final Integer REFUND = 2;

    private static final long serialVersionUID = 1L;

    private Long id;  // 主键，自增
    private String number;  // 订单号
    private Integer status;  // 订单状态，1为待付款，2为待接单，3为交易中，4为已完成，5为已取消
    private Long userId;  // 用户ID，逻辑外键
    private Long addressBookId;  // 地址ID，逻辑外键
    //下单时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderTime;
    //结账时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkoutTime;
    private Integer payMethod;  // 支付方式，1为微信支付，2为支付宝支付
    private Integer payStatus;  // 支付状态，0为未支付，1为已支付，2为退款
    private BigDecimal amount;  // 订单金额
    private String remark;  // 备注信息
    private String phone;  // 手机号
    private String address;  // 详细地址信息
    private String userName;  // 用户姓名
    private String consignee;  // 收货人姓名
    private String cancelReason;  // 订单取消原因
    private String rejectionReason;  // 拒单原因
    //订单取消时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cancelTime;
}

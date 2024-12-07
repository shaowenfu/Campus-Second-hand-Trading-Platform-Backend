package com.trade.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrdersConfirmDTO implements Serializable {

    private Long id;
    // 订单状态，1为待付款，2为待接单，3为交易中，4为已完成，5为已取消
    private Integer status;

}

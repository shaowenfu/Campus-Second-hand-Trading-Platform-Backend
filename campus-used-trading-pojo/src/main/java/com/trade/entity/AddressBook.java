package com.trade.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 地址簿
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;  // 主键，自增
    private Long userId;  // 用户ID，逻辑外键
    private String consignee;  // 收货人姓名
    private String sex;  // 收货人性别
    private String phone;  // 收货人手机号
    private String area;  // 所在园区
    private String dormitoriesId;  // 宿舍ID
    private String unitNumber;  // 单元号
    private String doorCode;  // 门牌号
    private Boolean isDefault;  // 是否默认地址，1为默认，0为非默认
}

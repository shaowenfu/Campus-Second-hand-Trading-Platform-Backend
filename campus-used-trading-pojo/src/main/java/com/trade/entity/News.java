package com.trade.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class News {
    private static final long serialVersionUID = 1L;
    private Long id;  // 主键，自增
    private String detail;  // 消息内容
    private String image;  // 相关图片路径
    private Integer sort;  // 排序字段，用于消息展示顺序
    private Integer status;  // 状态，1为启用，0为禁用
}

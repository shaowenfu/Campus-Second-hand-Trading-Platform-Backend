package com.trade.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 菜品
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Thing implements Serializable {

    private static final long serialVersionUID = 1L;
    @Getter
    private Long id;  // 主键，自增
    private String name;  // 物品名称，唯一
    private Long categoryId;  // 分类ID，逻辑外键
    private BigDecimal price;  // 物品价格
    private Long amount;  // 余量
    private String image;  // 图片路径
    private String description;  // 物品描述
    private Integer status;  // 售卖状态，1为起售，0为停售
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    private Long createUser;
    private Long updateUser;


}

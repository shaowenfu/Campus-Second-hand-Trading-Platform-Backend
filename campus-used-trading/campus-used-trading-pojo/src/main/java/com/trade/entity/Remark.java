package com.trade.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Remark implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id; // 主键，自增

    private String userId; // 用户ID

    private String marketId; // 商家ID

    private String detail; // 评论内容

    private String createTime;  // 创造时间

    private String updateTime;  //更新时间


}

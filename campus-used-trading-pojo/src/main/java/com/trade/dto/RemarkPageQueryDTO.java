package com.trade.dto;

import lombok.Data;

@Data
public class RemarkPageQueryDTO {
    //页码
    private int page;

    //每页记录数
    private int pageSize;

    //商家id
    private Long marketerId;

    //用户id
    private Long userId;

    //具体细节
    private String detail;
}

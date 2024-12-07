package com.trade.dto;

import lombok.Data;

@Data
public class RemarkPageQueryDTO {
    //页码
    private int page;

    //每页记录数
    private int pageSize;

    //商家id
    private int remarketId;

    //用户id
    private int userId;

    //具体细节
    private String detail;
}

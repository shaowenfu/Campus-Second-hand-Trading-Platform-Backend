package com.trade.dto;

import lombok.Data;

@Data
public class RemarkPageQueryDTO {
    //页码
    private int page;

    //每页记录数
    private int pageSize;

    //openid名称
    private String openId;

    //商家id
    private int remarketId;
}

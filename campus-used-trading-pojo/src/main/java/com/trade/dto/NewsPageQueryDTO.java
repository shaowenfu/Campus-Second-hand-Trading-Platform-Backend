package com.trade.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class NewsPageQueryDTO implements Serializable {

    //新闻状态
    private int status;

    //新闻细节
    private String details;

    //页码
    private int page;

    //每页显示记录数
    private int pageSize;
}

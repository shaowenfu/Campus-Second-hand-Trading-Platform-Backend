package com.trade.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemarkVO {

    //Id
    private Long Id;

    //时间
    private LocalDateTime Date;

    //评论
    private String detail;

    //商家用户名
    private String marketerUsername;

    //用户名
    private String userUsername;
}

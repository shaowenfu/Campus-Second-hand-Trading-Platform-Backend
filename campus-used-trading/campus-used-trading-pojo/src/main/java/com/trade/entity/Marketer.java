package com.trade.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Marketer implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;  // 主键，自增
    private String name;  // 姓名
    private String username;  // 用户名，唯一
    private String password;  // 密码
    private String phone;  // 手机号
    private String idNumber; // 身份证
    private Integer status;  // 权限状态，1为管理员，0为普通商家
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}

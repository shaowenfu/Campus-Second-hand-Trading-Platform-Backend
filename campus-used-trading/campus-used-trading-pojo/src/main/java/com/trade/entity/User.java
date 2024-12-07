package com.trade.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;  // 通过 @Getter 注解，自动生成 public 的 getId() 方法
    @Getter
    private String openid;  // 通过 @Getter 注解，自动生成 public 的 getOpenid() 方法
    private String name;
    private String phone;
    private String sex;
    private String idNumber;
    private String avatar;

    // 使用 lombok 的 @Getter 自动生成 public 方法，因此不再需要手动定义 getter 方法
    // @Getter 会自动为 id 和 openid 字段生成 public 的 getter 方法

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}


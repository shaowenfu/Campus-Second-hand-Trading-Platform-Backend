package com.trade.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MarketerDTO implements Serializable {

    private Long id;

    private String username;

    private String name;

    private String phone;

    private String idNumber;

}

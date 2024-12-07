package com.trade.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class ShoppingCartDTO implements Serializable {

    private Long thingId;
    private String thingFlavor;

}

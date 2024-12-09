package com.trade.service;

import com.trade.dto.ShoppingCartDTO;
import com.trade.entity.ShoppingCart;

import java.util.List;


@SuppressWarnings("ALL")
public interface ShoppingCartService {
    //    添加购物车
    void addshoppingCart(ShoppingCartDTO shoppingCartDTO);
    //    查看购物车
    List<ShoppingCart> showShoppingCart();

    void cleanShoppingCart();

    void removeShoppingCart(Long thingId);
}

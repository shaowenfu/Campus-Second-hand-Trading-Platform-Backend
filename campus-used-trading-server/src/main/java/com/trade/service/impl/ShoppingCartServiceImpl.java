package com.trade.service.impl;

import com.trade.context.BaseContext;
import com.trade.dto.ShoppingCartDTO;
import com.trade.entity.ShoppingCart;
import com.trade.entity.Thing;
import com.trade.mapper.ShoppingCartMapper;
import com.trade.mapper.ThingMapper;
import com.trade.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private ThingMapper thingMapper;

    public void addshoppingCart(ShoppingCartDTO shoppingCartDTO) {
        // 判断商品是否已经存在
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long userId = BaseContext.getCurrentId();
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        // 如果存在，只需要数量加一
        if (list != null && !list.isEmpty()) {
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(cart);
        } else {
            // 不存在，需要加入一条购物车数据
            Long thingId = shoppingCartDTO.getThingId();
            Thing thing = thingMapper.getById(thingId);
            shoppingCart.setName(thing.getName());
            shoppingCart.setImage(thing.getImage());
            shoppingCart.setAmount(thing.getPrice());
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
    }


    @Override
    public List<ShoppingCart> showShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(userId)
                .build();
        return shoppingCartMapper.list(shoppingCart);
    }

    @Override
    public void cleanShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userId);
    }

    @Override
    public void removeShoppingCart(Long shoppingCartId) {
        shoppingCartMapper.deleteById(shoppingCartId);
    }
}

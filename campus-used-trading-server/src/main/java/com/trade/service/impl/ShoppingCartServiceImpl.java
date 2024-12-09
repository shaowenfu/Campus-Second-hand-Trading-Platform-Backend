package com.trade.service.impl;

import com.trade.constant.MessageConstant;
import com.trade.constant.StatusConstant;
import com.trade.context.BaseContext;
import com.trade.dto.ShoppingCartDTO;
import com.trade.entity.ShoppingCart;
import com.trade.entity.Thing;
import com.trade.exception.ShoppingCartException;
import com.trade.mapper.ShoppingCartMapper;
import com.trade.mapper.ThingMapper;
import com.trade.service.ShoppingCartService;
import com.trade.vo.ThingVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private ThingMapper thingMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    public void addshoppingCart(ShoppingCartDTO shoppingCartDTO) {
        // 判断商品是否已经存在
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        //在购物车表找到对应的用户的购物表
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        //在Redis里找到对应的商品
        Thing thingInRedis = thingMapper.getByThingId(shoppingCartDTO.getThingId());
        //找到对应的Redis中的类别
        String key="thing_"+ thingInRedis.getCategoryId();
        List<ThingVO> list = (List<ThingVO>) redisTemplate.opsForValue().get(key);

        if(list == null) {
            list = new ArrayList<>();
            Thing thing = new Thing();
            thing.setCategoryId(thingInRedis.getCategoryId());
            thing.setStatus(StatusConstant.ENABLE);//查询起售中的商品
            List<Thing> thingList = thingMapper.list(thing);

            // 遍历查询到的商品列表，将每个 Thing 转换为 ThingVO
            for (Thing d : thingList) {
                ThingVO thingVO = new ThingVO();
                BeanUtils.copyProperties(d, thingVO);  // 将 Thing 的属性复制到 ThingVO
                list.add(thingVO);
            }
            //更新redis缓存
            redisTemplate.opsForValue().set(key,list);
        }

        // 如果存在，只需要数量加一
        if (shoppingCartList != null && !shoppingCartList.isEmpty()) {
            for (ThingVO thingVO : list) {
                if(Objects.equals(thingVO.getId(), shoppingCartDTO.getThingId())){
                    //查看余量是否大于当前的下单数量
                    //大于没问题
                    if(thingVO.getAmount() > shoppingCartList.get(0).getAmount()){
                        ShoppingCart cart = shoppingCartList.get(0);
                        cart.setAmount(cart.getAmount() + 1);
                        shoppingCartMapper.updateAmountById(cart);
                    }
                    //小于显示异常
                    else{
                        throw new ShoppingCartException(MessageConstant.AMOUNT_NOT_ENOUGH);
                    }
                    break;
                }
            }

        } else {
            // 不存在，需要加入一条购物车数据, 这里不需要修改Redis，不需要判断数量
            Long thingId = shoppingCartDTO.getThingId();
            Thing thing = thingMapper.getById(thingId);
            shoppingCart.setName(thing.getName());
            shoppingCart.setImage(thing.getImage());
            shoppingCart.setMarketerId(thingInRedis.getCreateUser());
            shoppingCart.setAmount(1L);
            shoppingCart.setPrice(thing.getPrice());
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setUserId(BaseContext.getCurrentId());
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
    public void removeShoppingCart(Long thingId) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setThingId(thingId);
        //在购物车表找到对应的商品
        ShoppingCart cart = shoppingCartMapper.list(shoppingCart).get(0);

        if(cart.getAmount() == 1){
            shoppingCartMapper.deleteById(thingId);
        }
        else{
            cart.setAmount(cart.getAmount() - 1);
            shoppingCartMapper.updateAmountById(cart);
        }
    }
}

package com.trade.mapper;

import com.trade.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    List<ShoppingCart> list(ShoppingCart shoppingCart);

    @Update("update shopping_cart set amount=#{amount} where id = #{id}")
    void updateAmountById(ShoppingCart shoppingCart);

    @Insert(value = "insert into shopping_cart(name,user_id,thing_id,marketer_id,price,amount,image,create_time )" +
            "values (#{name},#{userId},#{thingId},#{marketerId},#{price},#{amount},#{image},#{createTime})")
    void insert(ShoppingCart shoppingCart);

    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);

    @Delete("DELETE FROM shopping_cart WHERE thing_id = #{shoppingCartId}")
    void deleteById(Long shoppingCartId);

    @Select("select DISTINCT marketer_id from shopping_cart where user_id = #{userId} order by marketer_id")
    List<Long> getOrders(Long userId);

    /**
     * 根据userId和marketerId查询总共的金额
     * @param currentId
     * @param cart
     * @return
     */
    @Select("select sum(amount * price) from shopping_cart where user_id = #{currentId} and marketer_id = #{cart}")
    BigDecimal getAmountByuserIdAndMarketerId(Long currentId, Long cart);
}

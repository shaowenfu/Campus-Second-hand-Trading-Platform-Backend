package com.trade.mapper;

import com.trade.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    List<ShoppingCart> list(ShoppingCart shoppingCart);
    @Update("update shopping_cart set number=#{number} where id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);
    @Insert(value = "insert into shopping_cart(name,user_id,thing_id,number,amount,image,create_time )" +
            "values (#{name},#{userId},#{thingId},#{number},#{amount},#{image},#{createTime})")
    void insert(ShoppingCart shoppingCart);
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);

    @Delete("DELETE FROM shopping_cart WHERE id = #{shoppingCartId}")
    void deleteById(Long shoppingCartId);
}

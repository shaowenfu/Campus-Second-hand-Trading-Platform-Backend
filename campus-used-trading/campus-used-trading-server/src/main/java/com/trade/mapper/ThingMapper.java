package com.trade.mapper;

import com.trade.entity.Thing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ThingMapper {

    //@Select("SELECT * FROM thing WHERE id = #{thingId}")
    Thing getById(Long thingId);
    // 根据分类id和状态查询菜品
    @Select("SELECT * FROM thing WHERE category_id = #{categoryId} AND status = #{status}")

    List<Thing> list(Thing thing);

}

package com.trade.mapper;

import com.github.pagehelper.Page;
import com.trade.dto.ThingPageQueryDTO;
import com.trade.entity.Thing;
import com.trade.vo.ThingVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ThingMapper {

    /**
     * 更新商品
     * @param thing
     */
    void update(Thing thing);


    /**
     * 根据id查询商品
     * @param id
     */
    @Select(("select * from thing where id = #{id}"))
    Thing getById(Long id);

    /**
     * 根据categoryId查询商品
     * @param categoryId
     * @return
     */
    List<Thing> getBycategoryId(Long categoryId, Long marketerId);

    /**
     * 分页查找
     * @param thingPageQueryDTO
     * @return
     */
    Page<ThingVO> pageQuery(ThingPageQueryDTO thingPageQueryDTO);

    /**
     * 根据分类ID同居商品数量
     * @param id
     * @return
     */
    @Select("select count(id) from thing where category_id = #{id}")
    Integer countByCategoryId(Long id);

    /**
     * 查询商品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);

    @Delete("delete from thing where id = #{id}")
    void delete(Long id);

    /**
     * 插入数据
     * @param thing
     */
    void insert(Thing thing);

    // 根据分类id和状态查询菜品
    @Select("SELECT * FROM thing WHERE category_id = #{categoryId} AND status = #{status}")
    List<Thing> list(Thing thing);


    /**
     * 根据id查商品
     * @param thingId
     * @return
     */
    @Select("select * from thing where id = #{thingId}")
    Thing getByThingId(Long thingId);
}

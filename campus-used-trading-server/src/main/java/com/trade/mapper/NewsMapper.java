package com.trade.mapper;

import com.github.pagehelper.Page;
import com.trade.dto.NewsPageQueryDTO;
import com.trade.entity.News;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NewsMapper {

    /**
     * 删除
     * @param id
     */
    @Delete("delete from news where id = #{id}")
    void delete(Long id);

    /**
     * 删除
     * @param news
     */
    void save(News news);

    /**
     * 分页查询
     * @param newsPageQueryDTO
     * @return
     */
    Page<News> pageQuery(NewsPageQueryDTO newsPageQueryDTO);

    /**
     * 根据id查询新闻
     * @param id
     * @return
     */
    @Select("select * from news where id = #{id}")
    News getById(Long id);

    /**
     * 修改新闻
     * @param news
     */
    void update(News news);

    /**
     *
     * @return
     */
    @Select("select * from news order by sort asc ")
    List<News> list();
}

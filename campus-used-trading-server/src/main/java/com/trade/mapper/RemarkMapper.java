package com.trade.mapper;

import com.github.pagehelper.Page;
import com.trade.dto.RemarkPageQueryDTO;
import com.trade.entity.Remark;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RemarkMapper {

    /**
     * 根据id删除
     * @param id
     */
    @Delete("delete from remark where id = #{id}")
    void delete(Integer id);

    /**
     * 显示所有结果
     * @return
     */
    List<Remark> list(Long userId, Long marketerId);

    /**
     * 分页查找
     * @param remarkPageQueryDTO
     * @return
     */
    Page<Remark> pageQuery(RemarkPageQueryDTO remarkPageQueryDTO);

    @Select("select * from remark where id = #{id}")
    Remark getByid(Long id);

    /**
     * 更新评论
     * @param remark
     */
    void update(Remark remark);

    /**
     * 创建评论
     * @param remark
     */
    void insert(Remark remark);
}

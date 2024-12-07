package com.trade.service;

import com.trade.dto.RemarkDTO;
import com.trade.dto.RemarkPageQueryDTO;
import com.trade.result.PageResult;
import com.trade.vo.RemarkVO;

import java.util.List;

public interface RemarkService {

    void delete(Integer id);

    /**
     * 显示所有结果
     * @return
     */
    List<RemarkVO> list(Long userId,Long marketId);

    /**
     * 分页查找
     * @param remarkPageQueryDTO
     * @return
     */
    PageResult pageQuery(RemarkPageQueryDTO remarkPageQueryDTO);

    /**
     * 更新评价
     * @param id
     * @param detail
     */
    void update(Long id, String detail);

    /**
     * 新增评论
     * @param remarkDTO
     */
    void save(RemarkDTO remarkDTO);
}

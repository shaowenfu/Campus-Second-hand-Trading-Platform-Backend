package com.trade.service;

import com.trade.dto.MarketerDTO;
import com.trade.dto.MarketerPageQueryDTO;
import com.trade.dto.PasswordEditDTO;
import com.trade.entity.Marketer;
import com.trade.result.PageResult;

public interface AdminMarketerService {

    /**
     * 通过商户Id查找对象
     *
     * @param passwordEditDTO
     */
    void editPassword(PasswordEditDTO passwordEditDTO);


    /**
     * 改变状态
     *
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 分页查询
     * @param marketerPageQueryDTO
     * @return
     */
    PageResult pageQuery(MarketerPageQueryDTO marketerPageQueryDTO);

    /**
     * 新增商户
     * @param marketerDTO
     */
    void save(MarketerDTO marketerDTO);

    /**
     * 根据id查询商户
     * @param id
     * @return
     */
    Marketer getByid(Long id);

    /**
     * 编辑商户信息
     * @param marketerDTO
     */
    void update(MarketerDTO marketerDTO);
}

package com.trade.service;

import com.trade.dto.MarketerDTO;
import com.trade.dto.MarketerLoginDTO;
import com.trade.dto.PasswordEditDTO;
import com.trade.entity.Marketer;

public interface MarketerService {

    /**
     * 商家登录
     * @param marketerLoginDTO
     * @return
     */
    Marketer login(MarketerLoginDTO marketerLoginDTO);

    /**
     * 商家编辑信息
     * @param marketerDTO
     */
    void update(MarketerDTO marketerDTO);

    /**
     * 编辑新密码
     * @param passwordEditDTO
     */
    void editPassword(PasswordEditDTO passwordEditDTO);

    /**
     * 根据id查询marketer
     * @param id
     * @return
     */
    Marketer getMarketer(Long id);
}

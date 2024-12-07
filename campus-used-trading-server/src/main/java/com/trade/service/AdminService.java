package com.trade.service;

import com.trade.dto.MarketerLoginDTO;
import com.trade.entity.Marketer;

public interface AdminService {

    /**
     * 管理员登录
     * @param marketerLoginDTO
     * @return
     */
    Marketer login(MarketerLoginDTO marketerLoginDTO);

}

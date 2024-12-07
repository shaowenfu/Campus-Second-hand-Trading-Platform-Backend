package com.trade.service.impl;

import com.trade.constant.MessageConstant;
import com.trade.constant.StatusConstant;
import com.trade.dto.MarketerLoginDTO;
import com.trade.entity.Marketer;
import com.trade.exception.AccountLockedException;
import com.trade.exception.AccountNotFoundException;
import com.trade.exception.PasswordErrorException;
import com.trade.mapper.AdminMapper;
import com.trade.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param marketerLoginDTO
     * @return
     */
    public Marketer login(MarketerLoginDTO marketerLoginDTO) {
        String username = marketerLoginDTO.getUsername();
        String password = marketerLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Marketer marketer = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (marketer == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(marketer.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (marketer.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return marketer;
    }

}

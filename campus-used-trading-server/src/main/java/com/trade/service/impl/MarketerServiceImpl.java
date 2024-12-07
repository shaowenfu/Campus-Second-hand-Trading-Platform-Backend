package com.trade.service.impl;

import com.trade.constant.MessageConstant;
import com.trade.constant.StatusConstant;
import com.trade.dto.MarketerDTO;
import com.trade.dto.MarketerLoginDTO;
import com.trade.dto.PasswordEditDTO;
import com.trade.entity.Marketer;
import com.trade.exception.AccountLockedException;
import com.trade.exception.AccountNotFoundException;
import com.trade.exception.PasswordErrorException;
import com.trade.mapper.MarketerMapper;
import com.trade.service.MarketerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
public class MarketerServiceImpl implements MarketerService {

    @Autowired
    private MarketerMapper marketerMapper;

    public Marketer login(MarketerLoginDTO marketerLoginDTO) {
        String password = marketerLoginDTO.getPassword();
        String username = marketerLoginDTO.getUsername();

        //1、根据用户名查询数据库中的数据
        Marketer marketer = marketerMapper.getByUsername(username);

        if(marketer == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //对原始密码进行md5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        if(!password.equals(marketer.getPassword())) {
            throw new AccountNotFoundException(MessageConstant.PASSWORD_ERROR);
        }
        if(marketer.getStatus() == StatusConstant.DISABLE){
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        return marketer;
    }

    /**
     * 更新信息
     * @param marketerDTO
     */
    public void update(MarketerDTO marketerDTO){
        Marketer marketer = new Marketer();
        BeanUtils.copyProperties(marketerDTO,marketer);
        marketerMapper.update(marketer);
    }

    /**
     * 编辑新密码
     * @param passwordEditDTO
     */
    public void editPassword(PasswordEditDTO passwordEditDTO){
        Marketer marketer = marketerMapper.getById(passwordEditDTO.getMarketerId());

        if(!DigestUtils.md5DigestAsHex(passwordEditDTO.getOldPassword().getBytes()).equals(marketer.getPassword())){
            throw new PasswordErrorException(MessageConstant.PASSWORD_EDIT_FAILED);
        }

        //设置新密码
        marketer.setPassword(DigestUtils.md5DigestAsHex(passwordEditDTO.getNewPassword().getBytes()));
        //设置更新时间
        marketer.setUpdateTime(LocalDateTime.now());

        marketerMapper.update(marketer);
    }
}

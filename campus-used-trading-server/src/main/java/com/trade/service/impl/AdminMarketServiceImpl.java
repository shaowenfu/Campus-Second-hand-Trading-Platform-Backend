package com.trade.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.trade.constant.MessageConstant;
import com.trade.constant.PasswordConstant;
import com.trade.dto.MarketerDTO;
import com.trade.dto.MarketerPageQueryDTO;
import com.trade.dto.PasswordEditDTO;
import com.trade.entity.Marketer;
import com.trade.exception.PasswordErrorException;
import com.trade.mapper.AdminMarketerMapper;
import com.trade.mapper.MarketerMapper;
import com.trade.result.PageResult;
import com.trade.service.AdminMarketerService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class AdminMarketServiceImpl implements AdminMarketerService {
    @Autowired
    private AdminMarketerMapper adminMarketerMapper;
    @Autowired
    private MarketerMapper marketerMapper;

    /**
     * 修改密码
     * @param passwordEditDTO
     */
    public void editPassword(PasswordEditDTO passwordEditDTO){
        Marketer marketer = adminMarketerMapper.getById(passwordEditDTO.getMarketerId());

        if(!DigestUtils.md5DigestAsHex(passwordEditDTO.getOldPassword().getBytes()).equals(marketer.getPassword())){
            throw new PasswordErrorException(MessageConstant.PASSWORD_EDIT_FAILED);
        }

        //设置新密码
        marketer.setPassword(DigestUtils.md5DigestAsHex(passwordEditDTO.getNewPassword().getBytes()));
        //设置更新时间
        marketer.setUpdateTime(LocalDateTime.now());

        adminMarketerMapper.update(marketer);
    }

    /**
     * 禁用/开启商户
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id){
        Marketer marketer = Marketer.builder().status(status).id(id).build();
        adminMarketerMapper.update(marketer);
    }

    /**
     * 分页查询
     * @param marketerPageQueryDTO
     * @return
     */
    public PageResult pageQuery(MarketerPageQueryDTO marketerPageQueryDTO){
        //java中的自带的分页处理工具
        PageHelper.startPage(marketerPageQueryDTO.getPage(),marketerPageQueryDTO.getPageSize());
        //它可以获得数据库查询到对应的位置的marketer列表（但是他固定返回的时Page<>的形式）
        Page<Marketer> page = adminMarketerMapper.pageQuery(marketerPageQueryDTO);

        long total = page.getTotal();
        List<Marketer> records = page.getResult();
        return new PageResult(total,records);
    }

    /**
     * 新增商户
     * @param marketerDTO
     */
    public void save(MarketerDTO marketerDTO){
        Marketer marketer = new Marketer();

        //拷贝对象属性
        BeanUtils.copyProperties(marketerDTO,marketer);
        //设置其他属性
        marketer.setCreateTime(LocalDateTime.now());
        marketer.setUpdateTime(LocalDateTime.now());
        marketer.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        marketer.setAuthority(0); // 商户为0， 管理员为1
        marketer.setStatus(1); // 1表示正常， 0表示封禁

        adminMarketerMapper.insert(marketer);
    }

    /**
     * 根据id、查询商户
     * @param id
     * @return
     */
    public Marketer getByid(Long id){
        return adminMarketerMapper.getById(id);
    }

    /**
     * 编辑商户信息
     * @param marketerDTO
     */
    public void update(MarketerDTO marketerDTO){
        Marketer marketer = new Marketer();
        BeanUtils.copyProperties(marketerDTO,marketer);
        adminMarketerMapper.update(marketer);
    }
}

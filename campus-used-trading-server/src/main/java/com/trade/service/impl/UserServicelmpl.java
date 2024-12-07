package com.trade.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trade.constant.MessageConstant;
import com.trade.dto.UserLoginDTO;
import com.trade.entity.User;
import com.trade.exception.LoginFailedException;
import com.trade.mapper.UserMapper;
import com.trade.properties.WeChatProperties;
import com.trade.service.UserService;
import com.trade.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServicelmpl implements UserService {
    @Override
    public User wxlogin(UserLoginDTO userLoginDTO) {
        return null;
    }

    public static final String WX_LOGIN="";
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    public User wxlogin(UserLoginDTO userLoginDTO, String key) {
        String openid=getOpenid(userLoginDTO.getCode());
        if(openid==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        User user=userMapper.getByOpenid(openid);
        if(user==null){
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }

        return user;
    }
    private String getOpenid(String code){
        Map<String, String> map=new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String json=HttpClientUtil.doGet(WX_LOGIN,map);

        JSONObject jsonObject= JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}

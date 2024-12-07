package com.trade.controller.user;

import com.trade.constant.JwtClaimsConstant;
import com.trade.dto.UserLoginDTO;
import com.trade.entity.User;
import com.trade.properties.JwtProperties;
import com.trade.result.Result;
import com.trade.service.UserService;
import com.trade.utils.JwtUtil;
import com.trade.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/user")
@Api(tags="C端用户相关接口")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    private JwtProperties jwtProperties;
    @PostMapping("/login")
    @ApiOperation("微信登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("微信用户登录：{}",userLoginDTO.getCode());
        User user =userService.wxlogin(userLoginDTO);
        //String key;
        //userService.wxlogin(userLoginDTO, key);
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());
        String token=JwtUtil.createJWT(jwtProperties.getUserSecretKey(),jwtProperties.getUserTtl(),claims);
        UserLoginVO userLoginVO=UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();
        return Result.success(userLoginVO);
    }
}


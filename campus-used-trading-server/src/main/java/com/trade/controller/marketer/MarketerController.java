package com.trade.controller.marketer;

import com.trade.constant.JwtClaimsConstant;
import com.trade.dto.MarketerDTO;
import com.trade.dto.MarketerLoginDTO;
import com.trade.dto.PasswordEditDTO;
import com.trade.entity.Marketer;
import com.trade.properties.JwtProperties;
import com.trade.result.Result;
import com.trade.service.MarketerService;
import com.trade.utils.JwtUtil;
import com.trade.vo.MarketerLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 商家登录
 */
@Slf4j
@RestController
@RequestMapping("/marketer")
public class MarketerController {

    @Autowired
    private MarketerService marketerService;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/login")
    public Result<MarketerLoginVO> login(@RequestBody MarketerLoginDTO marketerLoginDTO) {
        log.info("商家登录：{}", marketerLoginDTO);

        Marketer marketer = marketerService.login(marketerLoginDTO);

        Map<String ,Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.ADMIN_ID,marketer.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getMarketerSecretKey(),
                jwtProperties.getMarketerTtl(),
                claims
        );

        MarketerLoginVO marketerLoginVO = MarketerLoginVO.builder()
                .id(marketer.getId())
                .userName(marketer.getUsername())
                .name(marketer.getName())
                .token(token)
                .build();

        log.info("{}", marketerLoginVO);
        return Result.success(marketerLoginVO);
    }

    /**
     * 退出登录
     * @return
     */
    @PostMapping("/logout")
    public Result logout() {
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody MarketerDTO marketerDTO) {
        log.info("编辑商家信息{}",marketerDTO);
        marketerService.update(marketerDTO);
        return Result.success();
    }

    @PutMapping("/editPassword")
    public Result editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        log.info("修改密码,[]",passwordEditDTO);
        marketerService.editPassword(passwordEditDTO);
        return Result.success();
    }
}

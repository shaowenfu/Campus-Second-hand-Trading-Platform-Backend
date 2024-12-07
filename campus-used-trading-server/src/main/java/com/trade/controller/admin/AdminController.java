package com.trade.controller.admin;

import com.trade.constant.JwtClaimsConstant;
import com.trade.dto.MarketerLoginDTO;
import com.trade.entity.Marketer;
import com.trade.properties.JwtProperties;
import com.trade.result.Result;
import com.trade.service.AdminService;
import com.trade.utils.JwtUtil;
import com.trade.vo.MarketerLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员管理
 */
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private AdminService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param marketerLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<MarketerLoginVO> login(@RequestBody MarketerLoginDTO marketerLoginDTO) {
        log.info("管理员登录：{}", marketerLoginDTO);

        Marketer admin = employeeService.login(marketerLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.ADMIN_ID, admin.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        MarketerLoginVO marketerLoginVO = MarketerLoginVO.builder()
                .id(admin.getId())
                .userName(admin.getUsername())
                .name(admin.getName())
                .token(token)
                .build();

        return Result.success(marketerLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

}

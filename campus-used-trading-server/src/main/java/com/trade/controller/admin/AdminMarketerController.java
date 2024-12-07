package com.trade.controller.admin;

import com.trade.dto.MarketerDTO;
import com.trade.dto.MarketerPageQueryDTO;
import com.trade.dto.PasswordEditDTO;
import com.trade.entity.Marketer;
import com.trade.result.PageResult;
import com.trade.result.Result;
import com.trade.service.AdminMarketerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/marketer")
@Slf4j
public class AdminMarketerController {

    @Autowired
    private AdminMarketerService adminMarketerService;

    @PutMapping("/editPassword")
    public Result editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        log.info("修改密码,[]",passwordEditDTO);
        adminMarketerService.editPassword(passwordEditDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    public Result status(@PathVariable("status") Integer status, Long id) {
        log.info("用户id：{}，修改状态：{}",id,status);
        adminMarketerService.startOrStop(status, id);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult> page(MarketerPageQueryDTO marketerPageQueryDTO) {
        log.info("商户分页查询：{}",marketerPageQueryDTO);
        PageResult pageResult = adminMarketerService.pageQuery(marketerPageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping
    public Result save(@RequestBody MarketerDTO marketerDTO) {
        log.info("新增员工:{}",marketerDTO);
        adminMarketerService.save(marketerDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Marketer> get(@PathVariable("id") Long id) {
        Marketer marketer = adminMarketerService.getByid(id);
        return Result.success(marketer);
    }

    @PutMapping
    public Result update(@RequestBody MarketerDTO marketerDTO) {
        log.info("编辑商家信息{}",marketerDTO);
        adminMarketerService.update(marketerDTO);
        return Result.success();
    }
}

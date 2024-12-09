package com.trade.controller.user;

import com.trade.dto.NewRemarkDTO;
import com.trade.dto.RemarkDTO;
import com.trade.dto.RemarkPageQueryDTO;
import com.trade.result.PageResult;
import com.trade.result.Result;
import com.trade.service.RemarkService;
import com.trade.vo.RemarkVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userRemarkController")
@RequestMapping("/user/remark")
@Slf4j
public class RemarkController {

    @Autowired
    private RemarkService remarkService;

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @DeleteMapping
    public Result delete(Integer id) {
        remarkService.delete(id);
        return Result.success();
    }

    /**
     * 检查userId 和 marleterId下的评论结果
     * @param userId
     * @param marketerId
     * @return
     */
    @GetMapping("/list")
    public Result<List<RemarkVO>> list(Long userId, Long marketerId) {
        List<RemarkVO> list = remarkService.list(userId,marketerId);
        return Result.success(list);
    }

    /**
     * 分页查找
     * @param remarkPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> pageQuery(RemarkPageQueryDTO remarkPageQueryDTO) {
        PageResult page = remarkService.pageQuery(remarkPageQueryDTO);
        return Result.success(page);
    }

    @PutMapping("/update")
    public Result update(@RequestBody NewRemarkDTO newremarkDTO) {
        remarkService.update(newremarkDTO);
        return Result.success();
    }

    @PostMapping("/save")
    public Result save(RemarkDTO remarkDTO) {
        remarkService.save(remarkDTO);
        return Result.success();
    }
}

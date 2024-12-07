package com.trade.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.trade.constant.MessageConstant;
import com.trade.context.BaseContext;
import com.trade.dto.NewRemarkDTO;
import com.trade.dto.RemarkDTO;
import com.trade.dto.RemarkPageQueryDTO;
import com.trade.entity.Remark;
import com.trade.exception.RemarkNotFoundException;
import com.trade.mapper.MarketerMapper;
import com.trade.mapper.RemarkMapper;
import com.trade.mapper.UserMapper;
import com.trade.result.PageResult;
import com.trade.service.NewsService;
import com.trade.service.RemarkService;
import com.trade.vo.RemarkVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RemarkServiceImpl implements RemarkService {

    @Autowired
    private RemarkMapper remarkMapper;
    @Autowired
    private MarketerMapper marketerMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 根据id删除
     * @param id
     */
    public void delete(Integer id){
        remarkMapper.delete(id);
    }

    /**
     * 显示所有评论
     * @return
     */
    public List<RemarkVO> list(Long userId, Long marketId){
        List<Remark> list = remarkMapper.list(userId,marketId);
        List<RemarkVO> remarkVOS = new ArrayList<RemarkVO>();
        for(Remark remark : list){
            RemarkVO remarkVO = new RemarkVO();
            //设定detail、商户名、用户名
            remarkVO.setDetail(remark.getDetail());
            remarkVO.setId(remark.getId());
            remarkVO.setDate(remark.getUpdateTime());
            remarkVO.setMarketerUsername(marketerMapper.getById(remark.getMarketerId()).getUsername());
            remarkVO.setUserUsername(userMapper.getUserById(remark.getUserId()).getName());
            remarkVOS.add(remarkVO);
        }
        return remarkVOS;
    }

    /**
     * 分页查找
     * @param remarkPageQueryDTO
     * @return
     */
    public PageResult pageQuery(RemarkPageQueryDTO remarkPageQueryDTO){
        PageHelper.startPage(remarkPageQueryDTO.getPage(),remarkPageQueryDTO.getPageSize());

        Page<Remark> page = remarkMapper.pageQuery(remarkPageQueryDTO);
        List<Remark> result = page.getResult();

        List<RemarkVO> remarkVOS = new ArrayList<RemarkVO>();
        for(Remark remark : result){
            RemarkVO remarkVO = new RemarkVO();
            //设定detail、商户名、用户名
            remarkVO.setDetail(remark.getDetail());
            remarkVO.setId(remark.getId());
            remarkVO.setDate(remark.getUpdateTime());
            remarkVO.setMarketerUsername(marketerMapper.getById(remark.getMarketerId()).getUsername());
            remarkVO.setUserUsername(userMapper.getUserById(remark.getUserId()).getName());
            remarkVOS.add(remarkVO);
        }
        return new PageResult(page.getTotal(),remarkVOS);
    }

    /**
     * 更新评价
     *
     * @param newremarkDTO
     */
    public void update(NewRemarkDTO newremarkDTO){
        Remark remark = remarkMapper.getByid(newremarkDTO.getId());

        if(remark == null){
            throw new RemarkNotFoundException(MessageConstant.REMARK_NOT_FOUND);
        }

        Remark remarknew = new Remark();
        remarknew.setDetail(newremarkDTO.getDetail());
        remarknew.setId(newremarkDTO.getId());
        remarknew.setUpdateTime(LocalDateTime.now());

        remarkMapper.update(remarknew);
    }

    /**
     * 新增评论
     * @param remarkDTO
     */
    public void save(RemarkDTO remarkDTO){
        Remark remark = Remark.builder()
                .detail(remarkDTO.getDetail())
                .marketerId(remarkDTO.getMarketerId())
                .userId(BaseContext.getCurrentId())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        remarkMapper.insert(remark);
    }
}

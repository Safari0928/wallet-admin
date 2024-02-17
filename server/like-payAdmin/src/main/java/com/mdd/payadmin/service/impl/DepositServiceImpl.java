package com.mdd.payadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.payDeposit.PayDepositDetail;
import com.mdd.common.mapper.payDeposit.PayDepositDetailMapper;
import com.mdd.payadmin.commons.MappingUtils;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.service.IDepositService;
import com.mdd.payadmin.validate.deposit.DepositSearchValidate;
import com.mdd.payadmin.vo.deposit.DepositDetailVo;
import com.mdd.payadmin.vo.deposit.DepositListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class DepositServiceImpl implements IDepositService {

    @Resource
    PayDepositDetailMapper payDepositDetailMapper;

    @Override
    public PageResult<DepositListVo> List(PageValidate pageValidate, DepositSearchValidate searchValidate) {

        Integer page = pageValidate.getPageNo();
        Integer size = pageValidate.getPageSize();

        QueryWrapper<PayDepositDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");

        payDepositDetailMapper.setSearch(queryWrapper, searchValidate, new String[]{
                "=:transactionNumber@transaction_number:str",
                "=:cardNumber@card_number:str",
                "=:payId@pay_id:str",
                "=:status@status:int"
        });

        IPage<PayDepositDetail> iPage = payDepositDetailMapper.selectPage(new Page<>(page, size), queryWrapper);

        List<DepositListVo> list = new ArrayList<>();
        for (PayDepositDetail deposit : iPage.getRecords()) {
            DepositListVo vo = new DepositListVo();
            BeanUtils.copyProperties(deposit, vo);
            String createTime = deposit.getCreateTime().replace(".0","");
            vo.setCreateTime(createTime);
            String completeTime = deposit.getCompleteTime().replace(".0","");
            vo.setCompleteTime(completeTime);
            String statusMessage = MappingUtils.mapStatus(deposit.getStatus());
            vo.setStatusMSG(statusMessage);
            String typeMessage = MappingUtils.mapTypeDetail(deposit.getTypeDetail());
            vo.setTypeMSG(typeMessage);
            list.add(vo);
        }
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list);
    }
    
    @Override
    public Long getCount(PageValidate pageValidate) {
        QueryWrapper<PayDepositDetail> queryWrapper = new QueryWrapper<>();
        return payDepositDetailMapper.selectCount(queryWrapper);
    }

    @Override
    public DepositDetailVo detail(String depositId) {
        PayDepositDetail detail = payDepositDetailMapper.selectOne(new QueryWrapper<PayDepositDetail>()
                .eq("deposit_id", depositId));
        Assert.notNull(detail, "Data does not exist!");

        DepositDetailVo vo = new DepositDetailVo();
        BeanUtils.copyProperties(detail, vo);
        String createTime = detail.getCreateTime().replace(".0","");
        vo.setCreateTime(createTime);
        String completeTime = detail.getCompleteTime().replace(".0", "");
        vo.setCompleteTime(completeTime);
        String statusMessage = MappingUtils.mapStatus(detail.getStatus());
        vo.setStatusMSG(statusMessage);
        String typeMessage = MappingUtils.mapTypeDetail(detail.getTypeDetail());
        vo.setTypeMSG(typeMessage);
        return vo;
    }
}

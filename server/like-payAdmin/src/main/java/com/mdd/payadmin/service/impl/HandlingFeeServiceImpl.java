package com.mdd.payadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.PayTransactions;
import com.mdd.common.mapper.PayTransactionsMapper;
import com.mdd.payadmin.commons.Constants;
import com.mdd.payadmin.commons.MappingUtils;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.service.IHandlingFeeService;
import com.mdd.payadmin.validate.handlingfee.HandlingFeeSearchValidate;
import com.mdd.payadmin.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HandlingFeeServiceImpl implements IHandlingFeeService {

    @Resource
    PayTransactionsMapper payTransactionsMapper;

    @Override
    public PageResult<HandlingFeeVo> getAll(PageValidate pageValidate, HandlingFeeSearchValidate searchValidate){
        Integer pageNo = pageValidate.getPageNo();
        Integer pageSize = pageValidate.getPageSize();
        QueryWrapper<PayTransactions> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        payTransactionsMapper.setSearch(queryWrapper,searchValidate,new String[]{
                "=:transactionNumber@transaction_number:str",
                "=:payId@pay_id:str",
                "=:currencyType@currency_type:str",
                "=:typeDetail@type_detail:str"
        });
        Page<PayTransactions> iPage = payTransactionsMapper.selectPage(new Page<>(pageNo, pageSize),
                queryWrapper);
        List<HandlingFeeVo> result = new LinkedList<>();
        for(PayTransactions detail : iPage.getRecords()){
            HandlingFeeVo vo = new HandlingFeeVo();
            BeanUtils.copyProperties(detail, vo);
            vo.setDetailId(detail.getDetailId());
            vo.setCurrency(MappingUtils.mapCurrency(detail.getCurrencyType()));
            vo.setTransactionType(MappingUtils.mapTypeDetail(detail.getTypeDetail()));
            String commission = String.valueOf(detail.getChannelCommission()+detail.getPayCommission());
            vo.setChannelHandlingFee(detail.getChannelCommission());
            vo.setAipayHandlingFee(detail.getPayCommission());
            vo.setCommission(commission);
            result.add(vo);
        }
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), result);
    }

    @Override
    public List<HandlingFeeVo> getList(){
        List<PayTransactions> allDetails = payTransactionsMapper.selectList(new QueryWrapper<PayTransactions>().orderByDesc(Constants.CREATETIME));

        List<HandlingFeeVo> result = allDetails.stream()
                .map(detail -> {
                    HandlingFeeVo vo = new HandlingFeeVo();
                    BeanUtils.copyProperties(detail, vo);
                    vo.setCurrency(MappingUtils.mapCurrency(detail.getCurrencyType()));
                    vo.setTransactionType(MappingUtils.mapTypeDetail(detail.getTypeDetail()));
                    String commission = String.valueOf(detail.getChannelCommission()+detail.getPayCommission());
                    vo.setChannelHandlingFee(detail.getChannelCommission());
                    vo.setAipayHandlingFee(detail.getPayCommission());
                    vo.setCommission(commission);
                    vo.setDetailId(detail.getDetailId());
                    return vo;
                })
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public long databaseDataSize(){
        return payTransactionsMapper.selectCount(null);
    }
}

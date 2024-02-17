package com.mdd.payadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.PayTransactions;
import com.mdd.common.entity.payTransfer.PayTransferDetails;
import com.mdd.common.entity.payUser.PayUser;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.PayTransactionsMapper;
import com.mdd.common.mapper.payUser.PayUserMapper;
import com.mdd.common.util.PayUtils;
import com.mdd.payadmin.commons.Constants;
import com.mdd.payadmin.commons.MappingUtils;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.enums.AdminEnum;
import com.mdd.payadmin.service.ITransactionsService;
import com.mdd.payadmin.validate.transfer.TransferSearchValidate;
import com.mdd.payadmin.vo.TransactionsDefaultVo;
import com.mdd.payadmin.vo.TransferDefaultVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Service
public class TransactionsServiceImpl implements ITransactionsService {
    @Resource
    private PayTransactionsMapper transactionsMapper;

    @Resource
    PayUserMapper payUserMapper;

    @Override
    public PageResult<TransactionsDefaultVo> list(PageValidate pageValidate, TransferSearchValidate validate) {
        Integer pageNo = pageValidate.getPageNo();
        Integer pageSize = pageValidate.getPageSize();
        QueryWrapper<PayTransactions> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc(Constants.CREATETIME);

        transactionsMapper.setSearch(queryWrapper,validate,new String[]{
                "=:transactionNumber@transaction_number:str",
                "=:payId@pay_id:str",
                "=:userId@user_id:str",
                "=:status@status:int",
                "=:typeDetail@type_detail:int",
                "=:currencyType@currency_type:int"
        });
        Page<PayTransactions> iPage = transactionsMapper.selectPage(new Page<>(pageNo, pageSize),queryWrapper);


        List<TransactionsDefaultVo> list = new LinkedList<>();
        for (PayTransactions transactions : iPage.getRecords()) {
            TransactionsDefaultVo vo = new TransactionsDefaultVo();
            BeanUtils.copyProperties(transactions, vo);
            vo.setCreateTime(transactions.getCreateTime().replace(".0", ""));
            if (transactions.getCompleteTime()!=null){
            vo.setCompleteTime(transactions.getCompleteTime().replace(".0", ""));}
            vo.setStatusMSG(MappingUtils.mapStatus(transactions.getStatus()));
            vo.setTypeMSG(MappingUtils.mapTypeDetail(transactions.getTypeDetail()));
            vo.setCurrency(MappingUtils.mapCurrency(transactions.getCurrencyType()));
            list.add(vo);
        }
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list);
    }
    @Override
    public Long getCount(PageValidate pageValidate) {
        QueryWrapper<PayTransactions> queryWrapper = new QueryWrapper<>();
        return transactionsMapper.selectCount(queryWrapper);
    }
}

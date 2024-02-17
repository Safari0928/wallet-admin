package com.mdd.payadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.payTransfer.PayTransferDetails;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.payTransfer.PayTransferDetailsMapper;
import com.mdd.payadmin.commons.Constants;
import com.mdd.payadmin.commons.MappingUtils;
import com.mdd.payadmin.enums.AdminEnum;
import com.mdd.payadmin.service.ITransferService;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.validate.transfer.TransferSearchValidate;
import com.mdd.payadmin.vo.TransferDefaultVo;
import com.mdd.payadmin.vo.TransferDetailVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Service
public class TransferServiceImpl implements ITransferService {
    @Resource
    PayTransferDetailsMapper transferMapper;

    @Override
    public PageResult<TransferDefaultVo> list(PageValidate pageValidate , TransferSearchValidate validate) {
        Integer pageNo = pageValidate.getPageNo();
        Integer pageSize = pageValidate.getPageSize();

        QueryWrapper<PayTransferDetails> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc(Constants.CREATETIME);

        transferMapper.setSearch(queryWrapper,validate,new String[]{
                "=:transactionNumber@transaction_number:str",
                "=:payId@pay_id:str",
                "=:status@status:int",
                "=:typeDetail@type_detail:int",
                "=:currencyType@currency_type:int"
        });

        Page<PayTransferDetails> iPage = transferMapper.selectPage(new Page<>(pageNo, pageSize),queryWrapper);

        List<TransferDefaultVo> list = new LinkedList<>();
        for (PayTransferDetails transferDetails : iPage.getRecords()) {
            TransferDefaultVo vo = new TransferDefaultVo();
            BeanUtils.copyProperties(transferDetails, vo);
            vo.setCreateTime(transferDetails.getCreateTime().replace(".0",""));
            vo.setCompleteTime(transferDetails.getCompleteTime().replace(".0", ""));
            vo.setStatusMSG(MappingUtils.mapStatus(transferDetails.getStatus()));
            vo.setTypeMSG(MappingUtils.mapTypeDetail(transferDetails.getTypeDetail()));
            vo.setCurrency(MappingUtils.mapCurrency(transferDetails.getCurrencyType()));
            list.add(vo);
        }
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list);
    }

    @Override
    public TransferDetailVo getDetail(String detailId) {
        PayTransferDetails details = transferMapper.selectOne(new QueryWrapper<PayTransferDetails>()
                .eq("transfer_id", detailId));
        if (details == null){
            throw new OperateException(Constants.NO_RECORD,AdminEnum.SUCCESS.getCode());
        }
        TransferDetailVo vo = new TransferDetailVo();
        BeanUtils.copyProperties(details,vo);
        vo.setCreateTime(details.getCreateTime().replace(".0",""));
        vo.setCompleteTime(details.getCompleteTime().replace(".0", ""));
        vo.setStatusMSG(MappingUtils.mapStatus(details.getStatus()));
        vo.setCommission(details.getPayCommission()+details.getChannelCommission());
        vo.setTypeMSG(MappingUtils.mapTypeDetail(details.getTypeDetail()));
        vo.setCurrency(MappingUtils.mapCurrency(details.getCurrencyType()));
        return vo;
    }

    @Override
    public Long getCount(PageValidate pageValidate) {
        QueryWrapper<PayTransferDetails> queryWrapper = new QueryWrapper<>();
        return transferMapper.selectCount(queryWrapper);
    }
}

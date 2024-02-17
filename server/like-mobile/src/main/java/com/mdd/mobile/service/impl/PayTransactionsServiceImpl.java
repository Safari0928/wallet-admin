package com.mdd.mobile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.PayTransactions;
import com.mdd.common.entity.payDeposit.PayDeposit;
import com.mdd.common.entity.payTransfer.PayTransferDetails;
import com.mdd.common.entity.payWithdraw.PayWithdraw;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.PayTransactionsMapper;
import com.mdd.common.mapper.payDeposit.PayDepositMapper;
import com.mdd.common.mapper.payTransfer.PayTransferDetailsMapper;
import com.mdd.common.mapper.payWithdraw.PayWithdrawMapper;
import com.mdd.common.util.RedisUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.mobile.enums.PayError;
import com.mdd.mobile.enums.TypeDetailEnums;
import com.mdd.mobile.service.IPayTransactionsService;
import com.mdd.mobile.service.IRedisPanelDataService;
import com.mdd.mobile.validate.common.Constants;
import com.mdd.mobile.validate.common.PageValidate;
import com.mdd.mobile.vo.PayTransactionsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Service
public class PayTransactionsServiceImpl implements IPayTransactionsService {

    @Autowired
    private PayTransferDetailsMapper payTransferMapper;

    @Autowired
    private PayDepositMapper payDepositMapper;

    @Autowired
    private PayWithdrawMapper payWithdrawMapper;

    @Autowired
    private PayTransactionsMapper payTransactionsMapper;

    @Autowired
    private IRedisPanelDataService redisPanelDataService;

    @Override
    public PageResult<PayTransactionsVo> getTransactionsList(PageValidate pageValidate,String userId) {
        Integer pageNo = pageValidate.getPageNo();
        Integer pageSize = pageValidate.getPageSize();

        QueryWrapper<PayTransactions> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("type_detail",Constants.AMOUNT,"detail","detail_id", "create_time");
        queryWrapper.eq(Constants.USERID, userId);
        queryWrapper.orderByDesc(Constants.CREATETIME);

        Page<PayTransactions> iPage = payTransactionsMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
        List<PayTransactionsVo> list = new LinkedList<>();
        for (PayTransactions details : iPage.getRecords()) {
            PayTransactionsVo vo = new PayTransactionsVo();
            String createTime = details.getCreateTime().replace(".0","");
            BeanUtils.copyProperties(details, vo);
            vo.setCreateTime(createTime);
        switch (details.getTypeDetail()){
        case 1:
                vo.setTypeMSG(TypeDetailEnums.TYPE_DEPOSIT.getMsg());
            break;
        case 2:
                vo.setTypeMSG(TypeDetailEnums.TYPE_WITHDRAWAL.getMsg());
            break;
        case 3:
                vo.setTypeMSG(TypeDetailEnums.TYPE_TRANSFER_DEPOSIT.getMsg());
            break;
        case 4:
            vo.setTypeMSG(TypeDetailEnums.TYPE_TRANSFER_WITHDRAWAL.getMsg());
            default:
                break;
        }
            list.add(vo);
        }

        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list);
    }

    @Override
    public boolean saveTransferTransaction(String detailId, BigDecimal revenue) {

        PayTransferDetails payTransfer = payTransferMapper.selectOne(new QueryWrapper<PayTransferDetails>()
                .eq("transfer_id", detailId));
        if(payTransfer==null){
            throw new OperateException("Wrong details ID",PayError.PARAM_ERROR.getCode());
        }
        PayTransactions payTransactions = new PayTransactions();
        payTransactions.setUuid(ToolUtils.makeUUID());
        payTransactions.setUserId(payTransfer.getUserId());
        payTransactions.setPayId(payTransfer.getPayId());
        payTransactions.setTransactionNumber(payTransfer.getTransactionNumber());
        payTransactions.setCurrencyType(payTransfer.getCurrencyType());
        payTransactions.setAmount(payTransfer.getAmount());
        payTransactions.setDetailId(detailId);
        payTransactions.setDetail(payTransfer.getToNickName());
        payTransactions.setPayCommission(payTransfer.getPayCommission());
        payTransactions.setChannelCommission(payTransfer.getChannelCommission());
        payTransactions.setRealAmount(payTransfer.getRealAmount());
        payTransactions.setTypeDetail(payTransfer.getTypeDetail());
        payTransactions.setStatus(payTransfer.getStatus());
        if (payTransfer.getTypeDetail()==TypeDetailEnums.TYPE_TRANSFER_DEPOSIT.getCode()){
            payTransactions.setRevenue(revenue);}
        payTransactions.setCreateTime(payTransfer.getCreateTime());
        payTransactions.setCompleteTime(payTransfer.getCompleteTime());

        int insert = payTransactionsMapper.insert(payTransactions);
        if (insert<1){
            throw new OperateException(PayError.ASSERT_MYBATIS_ERROR.getMsg(),PayError.ASSERT_MYBATIS_ERROR.getCode());
        }
        if (payTransfer.getTypeDetail()==3&&payTransfer.getStatus()==1){
            redisPanelDataService.addTransactionAmount(payTransfer.getAmount());
            redisPanelDataService.addTransactionRevenue(revenue);
            redisPanelDataService.addOrdersCount();
        }
        RedisUtils.del(Constants.TRANSACTIONS_USER+payTransactions.getUserId());
        return true;
    }

    @Override
    public boolean saveDepositTransaction(String detailId, BigDecimal revenue) {
        PayDeposit payDeposit = payDepositMapper.selectOne(new QueryWrapper<PayDeposit>()
                .eq(Constants.UUID, detailId));
        if(payDeposit==null){
            throw new OperateException("Wrong details ID",PayError.SYSTEM_ERROR.getCode());
        }
        PayTransactions payTransactions = new PayTransactions();
        payTransactions.setUuid(ToolUtils.makeUUID());
        payTransactions.setUserId(payDeposit.getUserId());
        payTransactions.setPayId(payDeposit.getPayId());
        payTransactions.setTransactionNumber(payDeposit.getTransactionNumber());
        payTransactions.setCurrencyType(1);
        payTransactions.setAmount(payDeposit.getAmount());
        payTransactions.setDetailId(detailId);
        String bankCardId = payDeposit.getBankCardId();
        String last4Digits = bankCardId.substring(bankCardId.length() - 4);
        payTransactions.setDetail(last4Digits);
        payTransactions.setPayCommission(payDeposit.getPayCommission());
        payTransactions.setChannelCommission(payDeposit.getChannelCommission());
        payTransactions.setRealAmount(payDeposit.getRealAmount());
        payTransactions.setTypeDetail(TypeDetailEnums.TYPE_DEPOSIT.getCode());
        payTransactions.setStatus(payDeposit.getStatus());
        payTransactions.setRevenue(revenue);
        payTransactions.setCreateTime(payDeposit.getCreateTime());
        payTransactions.setCompleteTime(payDeposit.getCreateTime());

        int insert = payTransactionsMapper.insert(payTransactions);
        if (insert<1){
            throw new OperateException(PayError.ASSERT_MYBATIS_ERROR.getMsg(),PayError.ASSERT_MYBATIS_ERROR.getCode());
        }
        RedisUtils.del(Constants.TRANSACTIONS_USER+payTransactions.getUserId());
        return true;
    }

    @Override
    public boolean saveWithdrawTransaction(String detailId, BigDecimal revenue) {
        PayWithdraw payWithdraw = payWithdrawMapper.selectOne(new QueryWrapper<PayWithdraw>()
                .eq(Constants.UUID, detailId));
        if(payWithdraw==null){
            throw new OperateException("Wrong details ID",PayError.SYSTEM_ERROR.getCode());
        }

        PayTransactions payTransactions = new PayTransactions();
        payTransactions.setUuid(ToolUtils.makeUUID());
        payTransactions.setUserId(payWithdraw.getUserId());
        payTransactions.setPayId(payWithdraw.getPayId());
        payTransactions.setTransactionNumber(payWithdraw.getTransactionNumber());
        payTransactions.setCurrencyType(1);
        payTransactions.setAmount(payWithdraw.getAmount());
        payTransactions.setDetailId(detailId);
        payTransactions.setDetail(payWithdraw.getIban());
        String ibanId = payWithdraw.getIban();
        String last4Digits = ibanId.substring(ibanId.length() - 4);
        payTransactions.setDetail(last4Digits);
        payTransactions.setPayCommission(payWithdraw.getPayCommission());
        payTransactions.setChannelCommission(payWithdraw.getChannelCommission());
        payTransactions.setRealAmount(payWithdraw.getRealAmount());
        payTransactions.setTypeDetail(TypeDetailEnums.TYPE_WITHDRAWAL.getCode());
        payTransactions.setStatus(payWithdraw.getStatus());
        payTransactions.setRevenue(revenue);
        payTransactions.setCreateTime(payWithdraw.getCreateTime());

        int insert = payTransactionsMapper.insert(payTransactions);
        if (insert<1){
            throw new OperateException(PayError.ASSERT_MYBATIS_ERROR.getMsg(),PayError.ASSERT_MYBATIS_ERROR.getCode());
        }
        RedisUtils.del(Constants.TRANSACTIONS_USER+payTransactions.getUserId());
        return true;
    }
}

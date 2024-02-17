package com.mdd.mobile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.payAcoounts.PayUserAccount;
import com.mdd.common.entity.payLimits.PayTransferConfigure;
import com.mdd.common.entity.payTransfer.PayTransfer;
import com.mdd.common.entity.payTransfer.PayTransferDetails;
import com.mdd.common.exception.OperateException;
import com.mdd.common.exception.PaymentException;
import com.mdd.common.mapper.payAccount.PayUserAccountMapper;
import com.mdd.common.mapper.payLimits.PayTransferLimitsMapper;
import com.mdd.common.mapper.payTransfer.PayTransferDetailsMapper;
import com.mdd.common.mapper.payTransfer.PayTransferMapper;
import com.mdd.common.util.PayUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.mobile.LikeMobileThreadLocal;
import com.mdd.mobile.enums.PayError;
import com.mdd.mobile.enums.StatusEnums;
import com.mdd.mobile.enums.TypeDetailEnums;
import com.mdd.mobile.service.IPayTransactionsService;
import com.mdd.mobile.service.IRedisPanelDataService;
import com.mdd.mobile.validate.common.Constants;
import com.mdd.mobile.validate.common.PageValidate;
import com.mdd.mobile.validate.transfer.TransferValidate;
import com.mdd.mobile.vo.PayTransferDetailsVo;
import org.jetbrains.annotations.NotNull;
import com.mdd.mobile.service.IPayTransferService;
import com.mdd.mobile.vo.PayTransferDetailVo;
import com.mdd.mobile.vo.PayTransferVo;;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;


@Service
public class PayTransferServiceImpl implements IPayTransferService {

    @Autowired
    private PayTransferMapper payTransferMapper;

    @Autowired
    private PayTransferDetailsMapper payTransferDetailsMapper;

    @Autowired
    private PayUserAccountMapper payUserAccountMapper;

    @Autowired
    private IPayTransactionsService payTransactionsService;

    @Autowired
    private PayTransferLimitsMapper limitsMapper;

    @Autowired
    private IRedisPanelDataService redisPanelDataService;

    @Override
    @Transactional(rollbackFor = OperateException.class)
    public PayTransferVo makeTransfer(TransferValidate validate) {
        String payId = LikeMobileThreadLocal.getPayId();
        Integer currencyType = validate.getCurrencyType();
        BigDecimal amount = validate.getAmount();
        String toPayId = validate.getToPayId();
        List<PayUserAccount> userAccounts = payUserAccountMapper.selectList(new QueryWrapper<PayUserAccount>()
                .in(Constants.CURRENCY_TYPE, currencyType, currencyType)
                .in(Constants.PayId, payId, toPayId));

        PayUserAccount sender = null;
        PayUserAccount receiver = null;

        for (PayUserAccount account : userAccounts) {
            if (account.getPayId().equals(payId)) {
                sender = account;
            } else if (account.getPayId().equals(toPayId)) {
                receiver = account;
            }
        }
        PayTransferConfigure payTransferConfigure = limitsMapper.selectOne(new QueryWrapper<PayTransferConfigure>()
                .eq("currency_id", currencyType));
        Double payCommission = payTransferConfigure.getPayCommission();
        Double channelCommission = payTransferConfigure.getChannelCommission();
        Double commission = payTransferConfigure.getCommission();
        BigDecimal revenue = amount.multiply(BigDecimal.valueOf(commission));
        BigDecimal finalAmount = amount.subtract(revenue);

        if (sender == null || receiver == null) {
            return null;
        }

        if (sender != null && receiver != null) {
            boolean deductResult = deductBalance(sender, amount);
            boolean addResult = addBalance(receiver, finalAmount);
            if (deductResult && addResult) {
                PayTransferVo Svo = insertData(sender, receiver, amount,
                finalAmount, StatusEnums.SUCCESS.getCode(), payCommission, channelCommission,revenue);
                Svo.setStatusMSG(StatusEnums.SUCCESS.getMsg());
                redisPanelDataService.addTransferAmount(amount);
                return Svo;
            }
        }
        PayTransferVo Fvo = insertData(sender, receiver, amount,
        finalAmount, StatusEnums.FAIL.getCode(), payCommission, channelCommission,revenue);
        Fvo.setStatusMSG(StatusEnums.FAIL.getMsg());
        return Fvo;
    }

    @Override
    @NotNull
    public PayTransfer createPayTransferEntry(PayUserAccount sender, PayUserAccount receiver, Integer type) {
        PayTransfer transfer = new PayTransfer();
        transfer.setUuid(ToolUtils.makeUUID());
        transfer.setUserId(sender.getUserId());
        transfer.setToUserId(receiver.getUserId());
        transfer.setNickName(sender.getNickName());
        transfer.setToNickName(receiver.getNickName());
        transfer.setPayId(sender.getPayId());
        transfer.setToPayId(receiver.getPayId());
        transfer.setTypeDetail(type);
        transfer.setStatus(StatusEnums.FAIL.getCode());
        transfer.setCurrencyType(sender.getCurrencyType());
        String transactionNumber = PayUtils.generateTransactionNumber("090", type);
        transfer.setTransactionNumber(transactionNumber);
        return transfer;
    }

    @Override
    public PayTransferDetails saveTransferDetails(String transferId) {
        PayTransfer transfer = payTransferMapper.selectOne(new QueryWrapper<PayTransfer>()
                .eq(Constants.UUID, transferId));
        PayTransferDetails details = new PayTransferDetails();
        details.setUuid(ToolUtils.makeUUID());
        details.setTransferId(transfer.getUuid());
        details.setCreateTime(PayUtils.createDateTime());
        details.setUserId(transfer.getUserId());
        details.setToUserId(transfer.getToUserId());
        details.setNickName(transfer.getNickName());
        details.setToNickName(transfer.getToNickName());
        details.setPayId(transfer.getPayId());
        details.setToPayId(transfer.getToPayId());
        details.setTransactionNumber(transfer.getTransactionNumber());
        details.setCurrencyType(transfer.getCurrencyType());
        details.setRealAmount(transfer.getAmount());
        details.setPayCommission(transfer.getPayCommission());
        details.setChannelCommission(transfer.getChannelCommission());
        details.setTypeDetail(transfer.getTypeDetail());
        details.setCompleteTime(PayUtils.createDateTime());

        return details;
    }

    @Override
    public PayTransferDetailVo getDetailByTransferId(String transferId) {
        PayTransferDetails payTransferDetail = payTransferDetailsMapper.selectOne(new QueryWrapper<PayTransferDetails>()
                .eq("transfer_id", transferId));
        PayTransferDetailVo vo = new PayTransferDetailVo();
        BeanUtils.copyProperties(payTransferDetail, vo);
        vo.setCreateTime(payTransferDetail.getCreateTime().replace(".0", ""));
        vo.setCompleteTime(payTransferDetail.getCompleteTime().replace(".0", ""));
        Double finalCommission = payTransferDetail.getPayCommission() + payTransferDetail.getChannelCommission();
        vo.setCommission(finalCommission);
        if (payTransferDetail.getStatus() < 0) {
            vo.setStatusMSG(StatusEnums.FAIL.getMsg());
        } else {
            vo.setStatusMSG(StatusEnums.SUCCESS.getMsg());
        }
        if (payTransferDetail.getTypeDetail() == TypeDetailEnums.TYPE_TRANSFER_DEPOSIT.getCode()) {
            vo.setTypeMSG(TypeDetailEnums.TYPE_TRANSFER_DEPOSIT.getMsg());
        }
        if (payTransferDetail.getTypeDetail() == TypeDetailEnums.TYPE_TRANSFER_WITHDRAWAL.getCode()) {
            vo.setTypeMSG(TypeDetailEnums.TYPE_TRANSFER_WITHDRAWAL.getMsg());
        }
        return vo;
    }

    @Override
    public PageResult<PayTransferDetailsVo> list(PageValidate pageValidate) {
        Integer pageNo = pageValidate.getPageNo();
        Integer pageSize = pageValidate.getPageSize();

        QueryWrapper<PayTransferDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("to_nick_name", "amount", "transfer_id", "type_detail", "create_time", "complete_time");
        queryWrapper.eq(Constants.USERID, LikeMobileThreadLocal.getUserId());
        queryWrapper.orderByDesc(Constants.CREATETIME);
        Page<PayTransferDetails> iPage = payTransferDetailsMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);

        List<PayTransferDetailsVo> list = new LinkedList<>();
        for (PayTransferDetails details : iPage.getRecords()) {
            PayTransferDetailsVo vo = new PayTransferDetailsVo();
            BeanUtils.copyProperties(details, vo);
            vo.setCreateTime(details.getCreateTime().replace(".0", ""));
            vo.setCompleteTime(details.getCompleteTime().replace(".0", ""));
            if (details.getTypeDetail() == TypeDetailEnums.TYPE_TRANSFER_DEPOSIT.getCode()) {
                vo.setTypeMSG(TypeDetailEnums.TYPE_TRANSFER_DEPOSIT.getMsg());
            }
            if (details.getTypeDetail() == TypeDetailEnums.TYPE_TRANSFER_WITHDRAWAL.getCode()) {
                vo.setTypeMSG(TypeDetailEnums.TYPE_TRANSFER_WITHDRAWAL.getMsg());
            }
            list.add(vo);
        }
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list);
    }

    private boolean deductBalance(PayUserAccount user, BigDecimal amount) {
        String uuid = user.getUuid();
        BigDecimal balance = user.getBalance();
        BigDecimal transferLimit = user.getTransferLimit();
        if (balance.compareTo(amount) > 0 && transferLimit.compareTo(amount) > 0) {
            BigDecimal updatedBalance = balance.subtract(amount);
            BigDecimal updatedLimit = transferLimit.subtract(amount);
            UpdateWrapper<PayUserAccount> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set(Constants.BALANCE, updatedBalance)
                    .set(Constants.TRANSFER_LIMIT, updatedLimit)
                    .eq(Constants.UUID, uuid);
            int update = payUserAccountMapper.update(null, updateWrapper);
            if (update < 1) {
                throw new OperateException(PayError.ASSERT_MYBATIS_ERROR.getMsg(), PayError.ASSERT_MYBATIS_ERROR.getCode());
            }
            return true;
        }
        return false;
    }

    private boolean addBalance(PayUserAccount user, BigDecimal amount) {

        String uuid = user.getUuid();
        BigDecimal balance = user.getBalance();
        BigDecimal transferLimit = user.getTransferLimit();

        if (transferLimit.compareTo(amount) > 0) {
            BigDecimal updatedBalance = balance.add(amount);
            BigDecimal updatedLimit = transferLimit.subtract(amount);
            UpdateWrapper<PayUserAccount> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set(Constants.BALANCE, updatedBalance)
                    .set(Constants.TRANSFER_LIMIT, updatedLimit)
                    .eq(Constants.UUID, uuid);
            int update = payUserAccountMapper.update(null, updateWrapper);
            if (update < 1) {
                throw new OperateException(PayError.ASSERT_MYBATIS_ERROR.getMsg(), PayError.ASSERT_MYBATIS_ERROR.getCode());
            }
            return true;
        }
        return false;
    }

    private PayTransferVo insertData(PayUserAccount sender, PayUserAccount receiver,
        BigDecimal amount, BigDecimal finalAmount, Integer status, Double payCommission, Double channelCommission,BigDecimal revenue) {
        PayTransfer payTransfer = createPayTransferEntry(receiver, sender, TypeDetailEnums.TYPE_TRANSFER_DEPOSIT.getCode());
        payTransfer.setPayCommission(payCommission);
        payTransfer.setChannelCommission(channelCommission);
        payTransfer.setAmount(finalAmount);
        payTransfer.setStatus(status);
        int insert = payTransferMapper.insert(payTransfer);
        if (insert < 1) {
            throw new PaymentException(PayError.ASSERT_MYBATIS_ERROR.getMsg(), PayError.ASSERT_MYBATIS_ERROR.getCode());
        }

        PayTransferDetails payTransferDetails = saveTransferDetails(payTransfer.getUuid());
        payTransferDetails.setAmount(amount);
        payTransferDetails.setStatus(status);
        int detailsResults = payTransferDetailsMapper.insert(payTransferDetails);
        if (detailsResults < 1) {
            throw new PaymentException(PayError.ASSERT_MYBATIS_ERROR.getMsg(), PayError.ASSERT_MYBATIS_ERROR.getCode());
        }

        String PTransferId = payTransferDetails.getTransferId();
        if (PTransferId != null) {
            payTransactionsService.saveTransferTransaction(PTransferId,revenue);
        } else {
            throw new PaymentException(PayError.PARAM_ERROR.getMsg());
        }

        PayTransfer recTransferEntry = createPayTransferEntry(sender, receiver, TypeDetailEnums.TYPE_TRANSFER_WITHDRAWAL.getCode());
        recTransferEntry.setPayCommission(payCommission);
        recTransferEntry.setChannelCommission(channelCommission);
        recTransferEntry.setAmount(finalAmount);
        recTransferEntry.setStatus(status);
        int transferResult = payTransferMapper.insert(recTransferEntry);
        if (transferResult < 1) {
            throw new OperateException(PayError.ASSERT_MYBATIS_ERROR.getMsg(), PayError.ASSERT_MYBATIS_ERROR.getCode());
        }

        PayTransferDetails receiverTransferDetails = saveTransferDetails(recTransferEntry.getUuid());
        receiverTransferDetails.setAmount(amount);
        receiverTransferDetails.setStatus(status);
        int DetailsResult = payTransferDetailsMapper.insert(receiverTransferDetails);
        if (DetailsResult < 1) {
            throw new OperateException(PayError.ASSERT_MYBATIS_ERROR.getMsg(), PayError.ASSERT_MYBATIS_ERROR.getCode());
        }

        String RTransferId = receiverTransferDetails.getTransferId();
        if (RTransferId != null) {
            payTransactionsService.saveTransferTransaction(RTransferId,revenue);
        } else {
            throw new OperateException(PayError.SYSTEM_ERROR.getMsg());
        }

        PayTransferVo vo = new PayTransferVo();
        vo.setTransactionNumber(payTransfer.getTransactionNumber());
        vo.setStatus(payTransfer.getStatus());
        return vo;
    }

}

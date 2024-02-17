package com.mdd.mobile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.PayTransactions;
import com.mdd.common.entity.payAcoounts.PayUserAccount;
import com.mdd.common.entity.payBankCard.PayBankCards;
import com.mdd.common.entity.payDeposit.PayDeposit;
import com.mdd.common.entity.payDeposit.PayDepositDetail;
import com.mdd.common.entity.payLimits.PayDepositConfigure;
import com.mdd.common.entity.server.Sys;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.PayTransactionsMapper;
import com.mdd.common.mapper.payAccount.PayUserAccountMapper;
import com.mdd.common.mapper.payBankCard.PayBankCardMapper;
import com.mdd.common.mapper.payDeposit.PayDepositDetailMapper;
import com.mdd.common.mapper.payDeposit.PayDepositMapper;
import com.mdd.common.mapper.payLimits.PayDepositLimitsMapper;
import com.mdd.common.mapper.payUser.PayUserMapper;
import com.mdd.common.util.*;
import com.mdd.mobile.LikeMobileThreadLocal;
import com.mdd.mobile.controller.CodeController;
import com.mdd.mobile.enums.PayError;
import com.mdd.mobile.enums.StatusEnums;
import com.mdd.mobile.enums.TypeDetailEnums;
import com.mdd.mobile.service.IDepositService;
import com.mdd.mobile.service.IPayTransactionsService;
import com.mdd.mobile.service.IRedisPanelDataService;
import com.mdd.mobile.validate.common.Constants;
import com.mdd.mobile.validate.common.PageValidate;
import com.mdd.mobile.validate.deposit.CreateDepositValidate;
import com.mdd.mobile.validate.deposit.SendDepositValidate;
import com.mdd.mobile.vo.deposit.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Service
public class DepositServiceImpl implements IDepositService {

    @Resource
    PayDepositMapper payDepositMapper;
    @Resource
    PayBankCardMapper payBankCardMapper;
    @Resource
    PayDepositDetailMapper payDepositDetailMapper;
    @Resource
    IPayTransactionsService transactionsService;
    @Resource
    PayTransactionsMapper payTransactionsMapper;
    @Resource
    CodeController codeController;
    @Resource
    PayUserAccountMapper payUserAccountMapper;
    @Resource
    private PayDepositLimitsMapper limitsMapper;
    @Resource
    IRedisPanelDataService iRedisPanelDataService;
    /**
     * 文章分类
     *
     * @return List<PayDepositVo>
     * @author Carullah
     */
    @Transactional
    @Override
    public PageResult<DepositVo> list(PageValidate pageValidate) {
        String userId=LikeMobileThreadLocal.getUserId();
        Integer pageNo = pageValidate.getPageNo();
        Integer pageSize = pageValidate.getPageSize();

        QueryWrapper<PayDeposit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");

        IPage<PayDeposit> iPage = payDepositMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);

        List<DepositVo> list = new LinkedList<>();
        for (PayDeposit deposit : iPage.getRecords()) {
            PayBankCards payBankCard = payBankCardMapper.selectOne(new QueryWrapper<PayBankCards>().
                    eq(Constants.UUID, deposit.getBankCardId())
                    .eq(Constants.USERID, deposit.getUserId())
                    .eq("is_delete", StatusEnums.NOT_DELETE.getCode()));

            Assert.notNull(payBankCard, "You don't have any card");

            DepositVo vo = new DepositVo();
            vo.setCardNumber(payBankCard.getCardNumber().substring(payBankCard.getCardNumber().length() - 4));
            vo.setBankName(payBankCard.getBankName());
            vo.setDepositId(deposit.getUuid());
            vo.setCreateTime(deposit.getCreateTime());
            BeanUtils.copyProperties(deposit, vo);
            list.add(vo);
        }

        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list);
    }

    @Transactional
    @Override
    public CompleteDepositVo sendDeposit(SendDepositValidate sendDepositValidate) {

        String userId= LikeMobileThreadLocal.getUserId();
        String payId=LikeMobileThreadLocal.getPayId();

        PayDeposit model = new PayDeposit();
        PayBankCards payBankCard = payBankCardMapper.selectOne(new QueryWrapper<PayBankCards>()
                .eq(Constants.UUID, sendDepositValidate.getBankCardId())
                .eq("is_delete", StatusEnums.NOT_DELETE.getCode())
        );
        Assert.notNull(payBankCard, "You don't have any card");

        PayDepositConfigure payDepositConfigure = limitsMapper.selectOne(new QueryWrapper<PayDepositConfigure>()
                .eq("is_active", 1));
        Assert.notNull(payDepositConfigure, "Pay Deposit Configure Not Found");

        BigDecimal comAmount = sendDepositValidate.getAmount().multiply(BigDecimal.valueOf(payDepositConfigure.getCommission()));
        BigDecimal finalAmount = sendDepositValidate.getAmount().subtract(comAmount);

        model.setUuid(ToolUtils.makeUUID());
        model.setTransactionNumber(PayUtils.generateTransactionNumber("090", TypeDetailEnums.TYPE_DEPOSIT.getCode()));
        BeanUtils.copyProperties(payBankCard, model);
        BeanUtils.copyProperties(sendDepositValidate, model);
        model.setStatus(StatusEnums.SUCCESS.getCode());
        model.setCreateTime(PayUtils.createDateTime());
        model.setTypeDetail(TypeDetailEnums.TYPE_DEPOSIT.getCode());
        model.setStatus(StatusEnums.FAIL.getCode());
        model.setPayId(payId);
        model.setUserId(userId);
        model.setRealAmount(finalAmount);
        model.setPayCommission(payDepositConfigure.getPayCommission());
        model.setChannelCommission(payDepositConfigure.getChannelCommission());
        model.setCompleteTime(PayUtils.createDateTime());

        int err = payDepositMapper.insert(model);
        if (err != 1) {
            throw new OperateException(PayError.ASSERT_MYBATIS_ERROR.getMsg(), PayError.ASSERT_MYBATIS_ERROR.getCode());
        }

        if (!createDepositDetail(model,null)) {
            throw new OperateException(PayError.ASSERT_MYBATIS_ERROR.getMsg(), PayError.ASSERT_MYBATIS_ERROR.getCode());
        }
        return completeDeposit(model.getUuid(), true);
    }

    @Transactional
    @Override
    public AddDepositVo createDeposit(CreateDepositValidate createDepositValidate) {

        String userId =  LikeMobileThreadLocal.getUserId();
        String payId =LikeMobileThreadLocal.getPayId();
        codeController.sendGenerateVerificationCode(Constants.REDIS_DEPOSIT + ":" + userId);

        PayDepositConfigure payDepositConfigure = limitsMapper.selectOne(new QueryWrapper<PayDepositConfigure>()
                .eq("is_active", 1));
        Assert.notNull(payDepositConfigure, "Pay Deposit Configure Not Found");

        BigDecimal comAmount = createDepositValidate.getAmount().multiply(BigDecimal.valueOf(payDepositConfigure.getCommission()));
        BigDecimal finalAmount = createDepositValidate.getAmount().subtract(comAmount);

        PayDeposit model = new PayDeposit();
        model.setUuid(ToolUtils.makeUUID());
        model.setUserId(userId);
        model.setTransactionNumber(PayUtils.generateTransactionNumber("090", TypeDetailEnums.TYPE_DEPOSIT.getCode()));
        BeanUtils.copyProperties(createDepositValidate, model);
        model.setStatus(StatusEnums.SUCCESS.getCode());
        model.setCreateTime(PayUtils.createDateTime());
        model.setPayId(payId);
        model.setRealAmount(finalAmount);
        model.setChannelCommission(payDepositConfigure.getChannelCommission());
        model.setPayCommission(payDepositConfigure.getPayCommission());
        model.setTypeDetail(TypeDetailEnums.TYPE_DEPOSIT.getCode());
        model.setStatus(StatusEnums.FAIL.getCode());
        model.setCompleteTime(PayUtils.createDateTime());

        if (createDepositValidate.getSave().equals(1)) {
            PayBankCards payBankCards = new PayBankCards();
            BeanUtils.copyProperties(createDepositValidate, payBankCards);
            payBankCards.setUuid(ToolUtils.makeUUID());
            payBankCards.setUserId(userId);
            payBankCards.setIsDelete(1);
            payBankCards.setCreateTime(PayUtils.createDateTime());
            int err1 = payBankCardMapper.insert(payBankCards);
            if (err1 != 1) {
                throw new OperateException(PayError.ASSERT_MYBATIS_ERROR.getMsg(), PayError.ASSERT_MYBATIS_ERROR.getCode());
            }
            model.setBankCardId(payBankCards.getUuid());
        }

        int err = payDepositMapper.insert(model);

        if (err != 1) {
            throw new OperateException(PayError.ASSERT_MYBATIS_ERROR.getMsg(), PayError.ASSERT_MYBATIS_ERROR.getCode());
        }

        if (!createDepositDetail(model ,createDepositValidate)) {
            throw new OperateException(PayError.ASSERT_MYBATIS_ERROR.getMsg(), PayError.ASSERT_MYBATIS_ERROR.getCode());
        }

        AddDepositVo vo = new AddDepositVo();
        BeanUtils.copyProperties(model, vo);
        BeanUtils.copyProperties(createDepositValidate, vo);

        double finalCom = payDepositConfigure.getChannelCommission() + payDepositConfigure.getPayCommission();
        vo.setCommission(Math.floor(finalCom * 100) / 100);
        vo.setAmount(createDepositValidate.getAmount());
        vo.setDepositId(model.getUuid());
        vo.setRealAmount(finalAmount);
        vo.setStatus(model.getStatus());
        vo.setFullName(PayUtils.replaceSubstringWithChar(
                createDepositValidate.getFullName(), 2,
                createDepositValidate.getFullName().length() - 2,
                '*')
        );
        vo.setCvv(PayUtils.replaceSubstringWithChar(createDepositValidate.getCvv(),
                        0,
                        createDepositValidate.getCvv().length(),
                        '*'
                )
        );
        vo.setExpiryDate(PayUtils.replaceSubstringWithChar(createDepositValidate.getExpiryDate(),
                        0,
                        createDepositValidate.getExpiryDate().length(),
                        '*'
                )
        );
        return vo;
    }

    @Override
    public CompleteDepositVo completeDeposit(String depositId, boolean verify) {

        String userId=LikeMobileThreadLocal.getUserId();
        CompleteDepositVo completeDepositVo = new CompleteDepositVo();
        PayDeposit payDeposit = payDepositMapper.selectOne(new QueryWrapper<PayDeposit>()
                .eq(Constants.UUID, depositId)
                );
        Assert.notNull(payDeposit, "deposit Not Found");

        PayDepositDetail payDepositDetail = payDepositDetailMapper.selectOne(new QueryWrapper<PayDepositDetail>()
                .eq("deposit_id", depositId)
                );
        Assert.notNull(payDepositDetail, "Pay Deposit Detail Not Found");

        PayUserAccount payUserAccount = payUserAccountMapper.selectOne(
                new QueryWrapper<PayUserAccount>()
                        .eq(Constants.USERID, userId)
                        .eq(Constants.CURRENCY_TYPE,1)
        );
        Assert.notNull(payUserAccount, "pay User Account Not Found");

        if (!verify) {
            completeDepositVo.setCode(PayError.UPDATE_AMOUNT_FILED.getCode());
            completeDepositVo.setMsg(PayError.UPDATE_AMOUNT_FILED.getMsg());
            completeDepositVo.setAmount(null);
            return completeDepositVo;
        }

        PayDepositConfigure payDepositConfigure = limitsMapper.selectOne(new QueryWrapper<PayDepositConfigure>()
                .eq("is_active", 1));

        Double commission = payDepositConfigure.getCommission();
        BigDecimal comAmount = payDeposit.getAmount().multiply(BigDecimal.valueOf(commission));

        if (payUserAccount.getDepositLimit().compareTo(payDeposit.getAmount()) >= 0) {
            BigDecimal newBalance = payUserAccount.getBalance().add(comAmount);
            payUserAccount.setBalance(newBalance);
            payUserAccount.setDepositLimit(payUserAccount.getDepositLimit().subtract(payDeposit.getAmount()));
        }else {
            completeDepositVo.setMsg(PayError.PAYMENT_ERROR.getMsg());
            completeDepositVo.setCode(PayError.PAYMENT_ERROR.getCode());
            return completeDepositVo;
        }

        int err4 = payUserAccountMapper.updateById(payUserAccount);
        if (err4==1){
            payDeposit.setStatus(StatusEnums.SUCCESS.getCode());
            payDepositDetail.setStatus(StatusEnums.SUCCESS.getCode());
        }else {
            throw new OperateException(PayError.ASSERT_MYBATIS_ERROR.getMsg(), PayError.ASSERT_MYBATIS_ERROR.getCode());
        }

        if (!transactionsService.saveDepositTransaction(payDeposit.getUuid(),comAmount)) {
            throw new OperateException(PayError.ASSERT_MYBATIS_ERROR.getMsg(), PayError.ASSERT_MYBATIS_ERROR.getCode());
        }

        int err2 = payDepositDetailMapper.updateById(payDepositDetail);
        int err3 = payDepositMapper.updateById(payDeposit);

        if ( err2 != 1 || err3 != 1 || err4 != 1) {
            throw new OperateException(PayError.ASSERT_MYBATIS_ERROR.getMsg(), PayError.ASSERT_MYBATIS_ERROR.getCode());
        }
        completeDepositVo.setCode(PayError.SUCCESS.getCode());
        completeDepositVo.setMsg(PayError.SUCCESS.getMsg());
        completeDepositVo.setAmount(payDeposit.getAmount());
        if (payDeposit.getStatus()==1){
            iRedisPanelDataService.addTransactionAmount(payDeposit.getAmount());
            iRedisPanelDataService.addTransactionRevenue(comAmount);
            iRedisPanelDataService.addOrdersCount();
            iRedisPanelDataService.addDepositAmount(payDeposit.getAmount());
        }
        return completeDepositVo;
    }

    @Override
    public DepositDetailVo detail(String id) {
        PayDepositDetail detail = payDepositDetailMapper.selectOne(new QueryWrapper<PayDepositDetail>().select("bank_name,card_number,transaction_number,amount,commission,status,create_time,complete_time").eq("deposit_id", id));

        Assert.notNull(detail, "Data does not exist!");

        DepositDetailVo vo = new DepositDetailVo();
        BeanUtils.copyProperties(detail, vo);
        vo.setTransactionType(TypeDetailEnums.TYPE_DEPOSIT.getCode());
        return vo;
    }

    public boolean createDepositDetail(PayDeposit model,CreateDepositValidate createDepositValidate) {

        String phoneNumber=LikeMobileThreadLocal.getPhoneNumber();
        if (model.getBankCardId()!=null){
            PayBankCards payBankCards = payBankCardMapper.selectOne(new QueryWrapper<PayBankCards>()
                    .eq(Constants.UUID, model.getBankCardId())
                    .eq("is_delete", StatusEnums.NOT_DELETE.getCode())
            );
            if (StringUtils.isNull(model) || StringUtils.isNull(payBankCards)) {
                Assert.isNull(model, "Create Deposit Detail is failed");
            } else {
                PayDepositDetail detail = new PayDepositDetail();
                detail.setUuid(ToolUtils.makeUUID());
                detail.setUserId(model.getUserId());
                detail.setPayId(model.getPayId());
                detail.setDepositId(model.getUuid());
                detail.setTransactionNumber(model.getTransactionNumber());
                detail.setAmount(model.getAmount());
                detail.setRealAmount(model.getRealAmount());
                detail.setStatus(model.getStatus());
                detail.setCompleteTime(model.getCompleteTime());
                detail.setDepositId(model.getUuid());
                detail.setPayCommission(model.getPayCommission());
                detail.setChannelCommission(model.getChannelCommission());
                double finalCom = model.getChannelCommission() + model.getPayCommission();
                detail.setCommission(Math.floor(finalCom * 100) / 100);
                detail.setCardNumber(payBankCards.getCardNumber());
                detail.setExpiryDate(payBankCards.getExpiryDate());
                detail.setCvv(payBankCards.getCvv());
                detail.setBankName(payBankCards.getBankName());
                detail.setFullName(payBankCards.getFullName());
                detail.setPhoneNumber(phoneNumber);
                detail.setCreateTime(PayUtils.createDateTime());
                detail.setTypeDetail(model.getTypeDetail());

                int err = payDepositDetailMapper.insert(detail);
                if (err != 1) {
                    return false;
                }

            }
        }else {
            PayDepositDetail detail = new PayDepositDetail();
            detail.setUuid(ToolUtils.makeUUID());
            detail.setUserId(model.getUserId());
            detail.setPayId(model.getPayId());
            detail.setDepositId(model.getUuid());
            detail.setTransactionNumber(model.getTransactionNumber());
            detail.setAmount(model.getAmount());
            detail.setRealAmount(model.getRealAmount());
            detail.setStatus(model.getStatus());
            detail.setCompleteTime(model.getCompleteTime());
            detail.setDepositId(model.getUuid());
            detail.setPayCommission(model.getPayCommission());
            detail.setChannelCommission(model.getChannelCommission());
            double finalCom = model.getChannelCommission() + model.getPayCommission();
            detail.setCommission(Math.floor(finalCom * 100) / 100);
            detail.setPhoneNumber(phoneNumber);
            detail.setCreateTime(PayUtils.createDateTime());
            detail.setTypeDetail(model.getTypeDetail());
            detail.setCardNumber(createDepositValidate.getCardNumber());
            detail.setExpiryDate(createDepositValidate.getExpiryDate());
            detail.setCvv(createDepositValidate.getCvv());
            detail.setBankName(createDepositValidate.getBankName());
            detail.setFullName(createDepositValidate.getFullName());
            int err = payDepositDetailMapper.insert(detail);
            if (err != 1) {
                return false;
            }
        }


        return true;
    }
}







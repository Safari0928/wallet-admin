package com.mdd.mobile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.PayTransactions;
import com.mdd.common.entity.payAcoounts.PayUserAccount;
import com.mdd.common.entity.payBankAccounts.PayBankAccount;
import com.mdd.common.entity.payCurrency.PayCurrency;
import com.mdd.common.entity.payLimits.PayWithdrawalConfigure;
import com.mdd.common.entity.payUser.PayUser;
import com.mdd.common.entity.payWithdraw.PayWithdraw;
import com.mdd.common.entity.payWithdraw.PayWithdrawDetail;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.PayTransactionsMapper;
import com.mdd.common.mapper.payAccount.PayUserAccountMapper;
import com.mdd.common.mapper.payBankAccounts.PayBankAccountsMapper;
import com.mdd.common.mapper.payCurrency.PayCurrencyMapper;
import com.mdd.common.mapper.payLimits.PayWithdrawalLimitsMapper;
import com.mdd.common.mapper.payUser.PayUserMapper;
import com.mdd.common.mapper.payWithdraw.PayWithdrawDetailMapper;
import com.mdd.common.mapper.payWithdraw.PayWithdrawMapper;
import com.mdd.common.util.PayUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.mobile.LikeMobileThreadLocal;
import com.mdd.mobile.enums.PayError;
import com.mdd.mobile.enums.StatusEnums;
import com.mdd.mobile.enums.TypeDetailEnums;
import com.mdd.mobile.service.IPayTransactionsService;
import com.mdd.mobile.service.IPayWithdrawService;
import com.mdd.mobile.service.IRedisPanelDataService;
import com.mdd.mobile.validate.common.Constants;
import com.mdd.mobile.validate.common.PageValidate;
import com.mdd.mobile.validate.withdraw.WithdrawCodeValidate;
import com.mdd.mobile.validate.withdraw.WithdrawValidate;
import com.mdd.mobile.vo.withdraw.VerifyWithdrawVo;
import com.mdd.mobile.vo.withdraw.WithdrawDetailVo;
import com.mdd.mobile.vo.withdraw.WithdrawDetailsVo;
import com.mdd.mobile.vo.withdraw.WithdrawVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class PayWithdrawServiceImpl implements IPayWithdrawService {
    
    @Resource
    PayWithdrawMapper payWithdrawMapper;

    @Resource
    PayWithdrawDetailMapper payWithdrawDetailMapper;

    @Resource
    PayBankAccountsMapper payBankAccountsMapper;

    @Resource
    PayUserAccountMapper payUserAccountMapper;

    @Resource
    PayUserMapper payUserMapper;

    @Resource
    PayWithdrawalLimitsMapper payWithdrawalLimitsMapper;

    @Resource
    PayCurrencyMapper payCurrencyMapper;

    @Resource
    IPayTransactionsService transactionsService;

    @Resource
    PayTransactionsMapper payTransactionsMapper;

    @Resource
    IRedisPanelDataService iRedisPanelDataService;

    private final ReentrantLock withdrawalLock = new ReentrantLock();

    @Transactional
    @Override
    public WithdrawVo addWithdraw(WithdrawValidate withdrawValidate){
        withdrawalLock.lock();
        try{
            String userId = (String) LikeMobileThreadLocal.get("userId");
            PayBankAccount payBankAccount = payBankAccountsMapper.selectOne(
                    new QueryWrapper<PayBankAccount>()
                            .eq(Constants.USERID,userId)
                            .eq("iban",withdrawValidate.getIban())
                            .eq("is_delete",StatusEnums.SUCCESS.getCode()));
            Assert.notNull(payBankAccount,"You do not have a bank account registered with this iban");

            PayUserAccount payUserAccount = payUserAccountMapper.selectOne(
                    new QueryWrapper<PayUserAccount>()
                            .eq(Constants.USERID,userId)
                            .eq(Constants.CURRENCY_TYPE,1)
            );
            Assert.notNull(payUserAccount,"You do not have an account registered with this user id");

            if(payUserAccount.getVerifyAccount()!=StatusEnums.SUCCESS.getCode()){
                int code = PayError.USER_ACCOUNT_NOT_VERIFIED.getCode();
                String msg = PayError.USER_ACCOUNT_NOT_VERIFIED.getMsg();
                throw new OperateException(msg,code);
            }
            PayWithdrawalConfigure payWithdrawalConfigure = payWithdrawalLimitsMapper.selectOne(
                    new QueryWrapper<PayWithdrawalConfigure>().eq("is_active",1).last(Constants.LIMIT1));
            Assert.notNull(payWithdrawalConfigure,"There is no such limit");

            PayCurrency payCurrency = payCurrencyMapper.selectOne(
                    new QueryWrapper<PayCurrency>().eq("currency_code",withdrawValidate.getCurrencyName())
            );
            Assert.isTrue((payCurrency.getIsActiveForWithdraw().equals(1)),"Withdrawals cannot be made in the selected currency");

            BigDecimal withdrawAmount = withdrawValidate.getAmount();
            Double commission = payWithdrawalConfigure.getCommission();
            BigDecimal commissionAmount = withdrawAmount.multiply(BigDecimal.valueOf(commission));
            BigDecimal realAmount = withdrawAmount.subtract(commissionAmount).setScale(1, RoundingMode.HALF_UP);
            BigDecimal userBalance = payUserAccount.getBalance();

            WithdrawVo withdrawVo = new WithdrawVo();
            withdrawVo.setIban(withdrawValidate.getIban());
            withdrawVo.setNickName(payUserAccount.getNickName());
            withdrawVo.setAmount(withdrawAmount);
            withdrawVo.setCommission(commission);
            withdrawVo.setRealAmount(realAmount);

            PayWithdraw payWithdraw = new PayWithdraw();
            payWithdraw.setUuid(ToolUtils.makeUUID());
            payWithdraw.setUserId(userId);
            payWithdraw.setUserId(payUserAccount.getUserId());
            payWithdraw.setTransactionNumber(PayUtils.generateTransactionNumber("090", TypeDetailEnums.TYPE_WITHDRAWAL.getCode()));
            payWithdraw.setBankName("Is Bankasi");
            payWithdraw.setAmount(withdrawAmount);
            payWithdraw.setPayId(payUserAccount.getPayId());
            payWithdraw.setTypeDetail(TypeDetailEnums.TYPE_WITHDRAWAL.getCode());
            payWithdraw.setCurrencyType(payCurrency.getCurrencyType());
            String createTime = PayUtils.createDateTime();
            payWithdraw.setCommission(commission);
            payWithdraw.setPayCommission(payWithdrawalConfigure.getPayCommission());
            payWithdraw.setChannelCommission(payWithdrawalConfigure.getChannelCommission());
            payWithdraw.setRealAmount(realAmount);
            payWithdraw.setIban(withdrawValidate.getIban());
            payWithdraw.setCreateTime(createTime);

            if(userBalance.compareTo(withdrawAmount) >= 0){
                payWithdraw.setStatus(StatusEnums.FAIL.getCode());
                withdrawVo.setStatus(StatusEnums.FAIL.getCode());

            }else{
                payWithdraw.setStatus(StatusEnums.FAIL.getCode());
                withdrawVo.setStatus(StatusEnums.FAIL.getCode());
                int code = PayError.PAYMENT_ERROR.getCode();
                String msg = PayError.PAYMENT_ERROR.getMsg();
                throw new OperateException(msg,code);
            }
            int add = payWithdrawMapper.insert(payWithdraw);
            if (add <= 0) {
                throw new OperateException("Inserting Withdraw failed!");
            } else {
                if (!transactionsService.saveWithdrawTransaction(payWithdraw.getUuid(),commissionAmount)) {
                    throw new OperateException("Withdraw Transaction failed!");
                }
            }
            withdrawVo.setUuid(payWithdraw.getUuid());
            addDetail(payWithdraw.getUuid());
            return withdrawVo;
        }finally {
            withdrawalLock.unlock();
        }
    }

    @Override
    public PayWithdrawDetail addDetail(String uuid){
        PayWithdraw payWithdraw = payWithdrawMapper.selectOne(new QueryWrapper<PayWithdraw>()
                .eq(Constants.UUID,uuid));
        PayUser payUser = payUserMapper.selectOne(new QueryWrapper<PayUser>()
                .eq(Constants.UUID,payWithdraw.getUserId()));
        PayWithdrawDetail payWithdrawDetail = new PayWithdrawDetail();
        payWithdrawDetail.setUuid(ToolUtils.makeUUID());
        payWithdrawDetail.setUserId(payWithdraw.getUserId());
        payWithdrawDetail.setPayId(payWithdraw.getPayId());
        payWithdrawDetail.setWithdrawId(payWithdraw.getUuid());
        payWithdrawDetail.setTransactionNumber(payWithdraw.getTransactionNumber());
        payWithdrawDetail.setIban(payWithdraw.getIban());
        payWithdrawDetail.setPhoneNumber(payUser.getPhoneNumber());
        payWithdrawDetail.setBankName(payWithdraw.getBankName());
        payWithdrawDetail.setAmount(payWithdraw.getAmount());
        payWithdrawDetail.setRealAmount(payWithdraw.getRealAmount());
        payWithdrawDetail.setCommission(payWithdraw.getCommission());
        payWithdrawDetail.setCreateTime(payWithdraw.getCreateTime());
        payWithdrawDetail.setStatus(payWithdraw.getStatus());
        payWithdrawDetailMapper.insert(payWithdrawDetail);
        return payWithdrawDetail;
    }

    @Override
    public VerifyWithdrawVo addVerifyCode(WithdrawCodeValidate withdrawCodeValidate){
        String userId = (String) LikeMobileThreadLocal.get("userId");
        PayWithdraw payWithdraw = payWithdrawMapper.selectOne(
                new QueryWrapper<PayWithdraw>()
                        .eq(Constants.UUID,withdrawCodeValidate.getUuid())
                        .eq(Constants.USERID,userId)
        );
        Assert.notNull(payWithdraw,"There is no such withdrawal process");

        PayUserAccount payUserAccount = payUserAccountMapper.selectOne(
                new QueryWrapper<PayUserAccount>()
                        .eq(Constants.USERID,userId)
                        .eq(Constants.CURRENCY_TYPE,1)
        );
        Assert.notNull(payUserAccount,"You do not have an account registered with this user id");

        PayTransactions payTransactions = payTransactionsMapper.selectOne(new QueryWrapper<PayTransactions>()
                .eq("detail_id", withdrawCodeValidate.getUuid())
                .eq(Constants.USERID, userId));

        PayWithdrawDetail payWithdrawDetail = payWithdrawDetailMapper.selectOne(
                new QueryWrapper<PayWithdrawDetail>().eq("withdraw_id",withdrawCodeValidate.getUuid()));

        PayCurrency payCurrency = payCurrencyMapper.selectOne(
                new QueryWrapper<PayCurrency>()
                        .eq("currency_type",payWithdraw.getCurrencyType()));

        PayBankAccount payBankAccount = payBankAccountsMapper.selectOne(
                new QueryWrapper<PayBankAccount>().eq("iban",payWithdraw.getIban())
        );
        PayWithdrawalConfigure payWithdrawalConfigure = payWithdrawalLimitsMapper.selectOne(
                new QueryWrapper<PayWithdrawalConfigure>().eq("is_active",1).last(Constants.LIMIT1));
        Assert.notNull(payWithdrawalConfigure,"There is no such limit");

        BigDecimal withdrawAmount = payWithdraw.getAmount();
        BigDecimal currentTotalAtBank = payBankAccount.getTotal();
        BigDecimal userBalance = payUserAccount.getBalance();
        BigDecimal userLimit = payUserAccount.getWithdrawalLimit();
        BigDecimal newBalance = userBalance.subtract(withdrawAmount);
        BigDecimal newLimit = userLimit.subtract(withdrawAmount);
        BigDecimal commission = BigDecimal.valueOf(payWithdraw.getCommission());
        BigDecimal commissionAmount = withdrawAmount.multiply(commission);
        BigDecimal amountToAdd = withdrawAmount.subtract(commissionAmount);
        currentTotalAtBank = currentTotalAtBank.add(amountToAdd);

        if(userBalance.compareTo(withdrawAmount) >= 0 && userLimit.compareTo(withdrawAmount)>=0){
            if(withdrawAmount.intValue()>payWithdrawalConfigure.getAmountAudited().intValue()){
                payUserAccount.setBalance(newBalance);
                payUserAccount.setWithdrawalLimit(newLimit);
                payTransactions.setStatus(StatusEnums.PENDING.getCode());
                payWithdraw.setStatus(StatusEnums.PENDING.getCode());
                payWithdrawDetail.setStatus(StatusEnums.PENDING.getCode());
                payUserAccountMapper.updateById(payUserAccount);
                payTransactionsMapper.updateById(payTransactions);
                payWithdrawDetailMapper.updateById(payWithdrawDetail);
            }else{
                String completeTime = PayUtils.createDateTime();
                payUserAccount.setBalance(newBalance);
                payUserAccount.setWithdrawalLimit(newLimit);
                payTransactions.setStatus(StatusEnums.SUCCESS.getCode());
                payWithdraw.setStatus(StatusEnums.SUCCESS.getCode());
                payWithdraw.setCompleteTime(completeTime);
                payTransactions.setCompleteTime(completeTime);
                payWithdrawDetail.setStatus(StatusEnums.SUCCESS.getCode());
                iRedisPanelDataService.addWithdrawAmount(payWithdraw.getAmount());
                iRedisPanelDataService.addTransactionAmount(payWithdraw.getAmount());
                iRedisPanelDataService.addTransactionRevenue(commissionAmount);
                iRedisPanelDataService.addOrdersCount();
                payWithdrawDetail.setCompleteTime(PayUtils.createDateTime());
                payBankAccount.setTotal(currentTotalAtBank);

                payUserAccountMapper.updateById(payUserAccount);
                payTransactionsMapper.updateById(payTransactions);
                payWithdrawDetailMapper.updateById(payWithdrawDetail);
                payBankAccountsMapper.updateById(payBankAccount);
            }

        }else{
            String completeTime = PayUtils.createDateTime();
            payWithdraw.setStatus(StatusEnums.FAIL.getCode());
            payWithdraw.setCompleteTime(completeTime);
            int code = PayError.PAYMENT_ERROR.getCode();
            String msg = PayError.PAYMENT_ERROR.getMsg();
            throw new OperateException(msg,code);
        }

        int add = payWithdrawMapper.updateById(payWithdraw);
        if(add<=0){
            int code = PayError.ASSERT_MYBATIS_ERROR.getCode();
            String msg = PayError.ASSERT_MYBATIS_ERROR.getMsg();
            throw new OperateException(msg,code);
        }

        VerifyWithdrawVo verifyWithdrawVo = new VerifyWithdrawVo();
        verifyWithdrawVo.setStatus(StatusEnums.SUCCESS.getMsg());
        verifyWithdrawVo.setAmount(payWithdraw.getAmount());
        verifyWithdrawVo.setCurrencySymbol(payCurrency.getSymbol());
        return verifyWithdrawVo;

    }

    @Override
    public PageResult<WithdrawDetailsVo> getAll(PageValidate pageValidate, String userId){
        Integer pageNo   = pageValidate.getPageNo();
        Integer pageSize = pageValidate.getPageSize();

        QueryWrapper<PayWithdraw>queryWrapper = new QueryWrapper<>();
        queryWrapper.select("bank_name","iban",Constants.AMOUNT,Constants.CREATETIME);
        queryWrapper.eq(Constants.USERID,userId);
        queryWrapper.orderByDesc(Constants.CREATETIME);

        Page<PayWithdraw> iPage =payWithdrawMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
        List<WithdrawDetailsVo> list = new LinkedList<>();
        for (PayWithdraw details : iPage.getRecords()){
            WithdrawDetailsVo vo = new WithdrawDetailsVo();
            BeanUtils.copyProperties(details,vo);
            String iban = details.getIban();
            if(iban!=null){
                String lastFourDigits = iban.substring(iban.length()-4);
                vo.setIban(lastFourDigits);
            }
            list.add(vo);
        }
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list);
    }

    @Override
    public WithdrawDetailVo getDetail(String uuid){
        PayWithdraw payWithdraw = payWithdrawMapper.selectOne(new QueryWrapper<PayWithdraw>()
                .eq(Constants.UUID,uuid));
        Assert.notNull(payWithdraw,"There are no transactions registered to this id");

        WithdrawDetailVo vo = new WithdrawDetailVo();
        BeanUtils.copyProperties(payWithdraw,vo);
        if(payWithdraw.getStatus()==StatusEnums.SUCCESS.getCode()){
            vo.setStatus(StatusEnums.SUCCESS.getMsg());
        }else if (payWithdraw.getStatus()==StatusEnums.PENDING.getCode()){
            vo.setStatus(StatusEnums.PENDING.getMsg());
        }else if(payWithdraw.getStatus()==StatusEnums.FAIL.getCode()){
            vo.setStatus(StatusEnums.FAIL.getMsg());
        }
        vo.setTransactionType(TypeDetailEnums.TYPE_WITHDRAWAL.getMsg());
        return vo;
    }

}

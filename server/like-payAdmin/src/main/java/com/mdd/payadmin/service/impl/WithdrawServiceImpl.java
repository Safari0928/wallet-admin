package com.mdd.payadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.PayTransactions;
import com.mdd.common.entity.payAcoounts.PayUserAccount;
import com.mdd.common.entity.payBankAccounts.PayBankAccount;
import com.mdd.common.entity.payLimits.PayWithdrawalConfigure;
import com.mdd.common.entity.payUser.PayUser;
import com.mdd.common.entity.payWithdraw.PayWithdraw;
import com.mdd.common.entity.payWithdraw.PayWithdrawDetail;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.PayTransactionsMapper;
import com.mdd.common.mapper.payAccount.PayUserAccountMapper;
import com.mdd.common.mapper.payBankAccounts.PayBankAccountsMapper;
import com.mdd.common.mapper.payLimits.PayWithdrawalLimitsMapper;
import com.mdd.common.mapper.payUser.PayUserMapper;
import com.mdd.common.mapper.payWithdraw.PayWithdrawDetailMapper;
import com.mdd.common.mapper.payWithdraw.PayWithdrawMapper;
import com.mdd.common.util.PayUtils;
import com.mdd.common.util.RedisUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.payadmin.commons.AdminUtils;
import com.mdd.payadmin.commons.Constants;
import com.mdd.payadmin.commons.MappingUtils;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.enums.AdminEnum;
import com.mdd.payadmin.enums.StatusEnums;
import com.mdd.payadmin.enums.TypeDetailEnums;
import com.mdd.payadmin.service.IWithdrawService;
import com.mdd.payadmin.validate.withdraw.ApproveValidate;
import com.mdd.payadmin.validate.withdraw.CancelValidate;
import com.mdd.payadmin.validate.withdraw.WithdrawSearchValidate;
import com.mdd.payadmin.vo.WithdrawDetailVo;
import com.mdd.payadmin.vo.WithdrawVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WithdrawServiceImpl implements IWithdrawService {

    @Resource
    PayWithdrawDetailMapper payWithdrawDetailMapper;

    @Resource
    PayWithdrawMapper payWithdrawMapper;

    @Resource
    PayUserMapper payUserMapper;

    @Resource
    PayWithdrawalLimitsMapper payWithdrawalLimitsMapper;

    @Resource
    PayBankAccountsMapper payBankAccountsMapper;

    @Resource
    PayUserAccountMapper payUserAccountMapper;

    @Resource
    PayTransactionsMapper payTransactionsMapper;

    final String key ="PanelData: ";

    @Override
    public PageResult<WithdrawVo> getDatas(PageValidate pageValidate, WithdrawSearchValidate searchValidate){
        Integer pageNo = pageValidate.getPageNo();
        Integer pageSize = pageValidate.getPageSize();

        QueryWrapper<PayWithdraw> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("status = 2");
        queryWrapper.orderByAsc("CASE WHEN status = 2 THEN create_time END");
        queryWrapper.orderByDesc("CASE WHEN status != 2 THEN complete_time END");

        payWithdrawMapper.setSearch(queryWrapper,searchValidate, new String[]{
                "=:transactionNumber@transaction_number:str",
                "=:payId@pay_id:str",
                "=:iban@iban:str",
                "=:status@status:str"
        });

        Page<PayWithdraw> iPage = payWithdrawMapper.selectPage(new Page<>(pageNo, pageSize),
                queryWrapper);

        List<WithdrawVo> result = new LinkedList<>();
        for (PayWithdraw detail : iPage.getRecords()) {
            WithdrawVo vo = new WithdrawVo();
            BeanUtils.copyProperties(detail, vo);
            vo.setTransactionNumber(detail.getTransactionNumber());
            vo.setCreateTime(detail.getCreateTime().replace(".0",""));
            if(detail.getCompleteTime()!=null){
                vo.setCompleteTime(detail.getCompleteTime().replace(".0",""));
            }
            vo.setStatusMessage(MappingUtils.mapStatus(detail.getStatus()));
            result.add(vo);
        }
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), result);
    }

    @Override
    public List<WithdrawVo> getAll(){
        QueryWrapper<PayWithdraw> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("status = 2");
        queryWrapper.orderByAsc("CASE WHEN status = 2 THEN create_time END");
        queryWrapper.orderByDesc("CASE WHEN status != 2 THEN complete_time END");

        List<PayWithdraw> allRecords = payWithdrawMapper.selectList(queryWrapper);
        List<WithdrawVo> result = new ArrayList<>();
        for (PayWithdraw detail : allRecords) {
            WithdrawVo vo = new WithdrawVo();
            BeanUtils.copyProperties(detail, vo);
            vo.setTransactionNumber(detail.getTransactionNumber());
            vo.setStatusMessage(MappingUtils.mapStatus(detail.getStatus()));
            vo.setCreateTime(detail.getCreateTime().replace(".0",""));
            if(detail.getCompleteTime()!=null){
                vo.setCompleteTime(detail.getCompleteTime().replace(".0",""));
            }
            result.add(vo);
        }
        return result;
    }

    @Override
    public WithdrawDetailVo getDetailById(String detailId){
        PayWithdrawDetail payWithdrawDetail = payWithdrawDetailMapper.selectOne(new QueryWrapper<PayWithdrawDetail>()
                .eq("withdraw_id", detailId));
        Assert.notNull(payWithdrawDetail,Constants.NO_RECORD, AdminEnum.FAILED.getCode());

        PayWithdraw payWithdraw = payWithdrawMapper.selectOne(new QueryWrapper<PayWithdraw>()
                .eq(Constants.UUID, detailId));
        Assert.notNull(payWithdraw,Constants.NO_RECORD, AdminEnum.FAILED.getCode());

        PayUser payUser = payUserMapper.selectOne(new QueryWrapper<PayUser>()
                .eq(Constants.UUID,payWithdrawDetail.getUserId()));
        Assert.notNull(payUser,Constants.NO_RECORD, AdminEnum.FAILED.getCode());

        PayWithdrawalConfigure payWithdrawalConfigure = payWithdrawalLimitsMapper.selectOne(
                new QueryWrapper<PayWithdrawalConfigure>().eq("is_active",1).last(Constants.LIMIT1));
        Assert.notNull(payWithdrawalConfigure,"There is no such limit");

        Double channelCommission = payWithdrawalConfigure.getChannelCommission();
        Double payCommission = payWithdrawalConfigure.getPayCommission();
        Double commission = channelCommission + payCommission;
        WithdrawDetailVo withdrawDetailVo = new WithdrawDetailVo();
        BeanUtils.copyProperties(payWithdrawDetail,withdrawDetailVo);
        withdrawDetailVo.setUuid(payWithdraw.getUuid());
        withdrawDetailVo.setAipayTransactionNumber(payWithdrawDetail.getTransactionNumber());
        withdrawDetailVo.setBankTransactionNumber("127937891782371723712939127312");
        withdrawDetailVo.setStatusMessage(MappingUtils.mapStatus(payWithdrawDetail.getStatus()));
        withdrawDetailVo.setCommission(commission);
        withdrawDetailVo.setChannelCommission(channelCommission);
        withdrawDetailVo.setPayCommission(payCommission);
        withdrawDetailVo.setTypeDetail(payWithdraw.getTypeDetail());
        withdrawDetailVo.setTypeMSG(MappingUtils.mapTypeDetail(payWithdraw.getTypeDetail()));
        withdrawDetailVo.setDescription(payWithdrawDetail.getDescription());
        withdrawDetailVo.setPhoneNumber(payUser.getPhoneNumber());
        withdrawDetailVo.setCreateTime(payWithdraw.getCreateTime().replace(".0",""));
        if(payWithdraw.getCompleteTime()!=null){
            withdrawDetailVo.setCompleteTime(payWithdraw.getCompleteTime().replace(".0",""));
        }
        return withdrawDetailVo;

    }

    @Override
    public boolean makeApprove(ApproveValidate approveValidate) {
        PayWithdrawDetail payWithdrawDetail = payWithdrawDetailMapper.selectOne(new QueryWrapper<PayWithdrawDetail>()
                .eq("withdraw_id", approveValidate.getDetailId()));
        Assert.notNull(payWithdrawDetail, Constants.NO_RECORD, AdminEnum.FAILED.getCode());

        PayWithdraw payWithdraw = payWithdrawMapper.selectOne(new QueryWrapper<PayWithdraw>()
                .eq(Constants.UUID, approveValidate.getDetailId()));
        Assert.notNull(payWithdraw, Constants.NO_RECORD, AdminEnum.FAILED.getCode());

        PayUserAccount payUserAccount = payUserAccountMapper.selectOne(new QueryWrapper<PayUserAccount>()
                .eq(Constants.USERID, payWithdrawDetail.getUserId())
                .eq(Constants.CURRENCY_TYPE, 1));
        Assert.notNull(payUserAccount, Constants.NO_RECORD, AdminEnum.FAILED.getCode());

        PayTransactions payTransactions = payTransactionsMapper.selectOne(new QueryWrapper<PayTransactions>()
                .eq("detail_id", approveValidate.getDetailId()));

        PayBankAccount payBankAccount = payBankAccountsMapper.selectOne(new QueryWrapper<PayBankAccount>()
                .eq("iban", payWithdraw.getIban()));
        Assert.notNull(payBankAccount, Constants.NO_RECORD, AdminEnum.FAILED.getCode());

        PayWithdrawalConfigure payWithdrawalConfigure = payWithdrawalLimitsMapper.selectOne(
                new QueryWrapper<PayWithdrawalConfigure>().eq("is_active", 1).last(Constants.LIMIT1));
        Assert.notNull(payWithdrawalConfigure, "There is no such limit");

        if (payWithdrawDetail.getStatus() == StatusEnums.PENDING.getCode()) {
                BigDecimal withdrawAmount = payWithdraw.getAmount();
                BigDecimal currentTotalAtBank = payBankAccount.getTotal();
                BigDecimal commission = BigDecimal.valueOf(payWithdraw.getCommission());
                BigDecimal commissionAmount = withdrawAmount.multiply(commission);
                BigDecimal amountToAdd = withdrawAmount.subtract(commissionAmount);
                currentTotalAtBank = currentTotalAtBank.add(amountToAdd);
                String completeTime = PayUtils.createDateTime();
                payBankAccount.setTotal(currentTotalAtBank);
                payWithdrawDetail.setStatus(StatusEnums.SUCCESS.getCode());
                payWithdrawDetail.setCompleteTime(completeTime);
                AdminUtils.updateRedis(approveValidate.getDetailId(),StatusEnums.SUCCESS.getCode(),completeTime);
                AdminUtils.updateWithdrawRedis(approveValidate.getDetailId(),StatusEnums.SUCCESS.getCode(),completeTime );
                payWithdraw.setStatus(StatusEnums.SUCCESS.getCode());
                payWithdraw.setCompleteTime(completeTime);
                payTransactions.setStatus(StatusEnums.SUCCESS.getCode());
                payTransactions.setCompleteTime(completeTime);
                addWithdrawAmount(withdrawAmount);
                addTransactionAmount(payWithdraw.getAmount());
                addTransactionRevenue(commissionAmount);
                addOrdersCount();
                payWithdrawDetailMapper.updateById(payWithdrawDetail);
                payWithdrawMapper.updateById(payWithdraw);
                payBankAccountsMapper.updateById(payBankAccount);
                payTransactionsMapper.updateById(payTransactions);
                return true;
        }else {
            return false;
        }
    }

    public void addOrdersCount() {
        String orderKey =key+TypeDetailEnums.TYPE_ORDER_COUNT.getMsg()+": "+TypeDetailEnums.TYPE_ORDER_COUNT.getCode()+TypeDetailEnums.TYPE_ORDER_COUNT.getMsg();
        if (!RedisUtils.exists(orderKey)){
            RedisUtils.set(orderKey,0);
        }else {
            int oldAmount = (int) RedisUtils.get(orderKey);
            RedisUtils.set(orderKey,oldAmount+1);
        }
    }

    public void addTransactionRevenue(BigDecimal amount) {
        String TransactionRevenueKey =key+TypeDetailEnums.TYPE_REVENUE.getMsg()+": "+TypeDetailEnums.TYPE_REVENUE.getCode()+TypeDetailEnums.TYPE_REVENUE.getMsg();
        if (!RedisUtils.exists(TransactionRevenueKey)){
            RedisUtils.set(TransactionRevenueKey,amount);
        }else {
            BigDecimal oldAmount = (BigDecimal) RedisUtils.get(TransactionRevenueKey);
            RedisUtils.set(TransactionRevenueKey,oldAmount.add(amount));
        }
    }

    public void addTransactionAmount(BigDecimal amount) {
        String TransactionKey =key+TypeDetailEnums.TYPE_TRANSACTION.getMsg()+": "+TypeDetailEnums.TYPE_TRANSACTION.getCode()+TypeDetailEnums.TYPE_TRANSACTION.getMsg();
        if (!RedisUtils.exists(TransactionKey)){
            RedisUtils.set(TransactionKey,amount);
        }else {
            BigDecimal oldAmount = (BigDecimal) RedisUtils.get(TransactionKey);
            RedisUtils.set(TransactionKey,oldAmount.add(amount));
        }
    }

    public void addWithdrawAmount(BigDecimal amount) {

        String transferKey =key+ TypeDetailEnums.TYPE_WITHDRAWAL.getMsg()+": "+TypeDetailEnums.TYPE_WITHDRAWAL.getCode()+TypeDetailEnums.TYPE_WITHDRAWAL.getMsg();
        if (!RedisUtils.exists(transferKey)){
            RedisUtils.set(transferKey,amount);
        }
        else {
            BigDecimal oldAmount = (BigDecimal) RedisUtils.get(transferKey);
            RedisUtils.set(transferKey,oldAmount.add(amount));
        }
    }

    @Override
    public boolean makeCancel(CancelValidate cancelValidate){
        PayWithdrawDetail payWithdrawDetail = payWithdrawDetailMapper.selectOne(new QueryWrapper<PayWithdrawDetail>()
                .eq("withdraw_id", cancelValidate.getDetailId()));
        Assert.notNull(payWithdrawDetail, Constants.NO_RECORD, AdminEnum.FAILED.getCode());

        PayWithdraw payWithdraw = payWithdrawMapper.selectOne(new QueryWrapper<PayWithdraw>()
                .eq(Constants.UUID, cancelValidate.getDetailId()));
        Assert.notNull(payWithdraw, Constants.NO_RECORD, AdminEnum.FAILED.getCode());

        PayUserAccount payUserAccount = payUserAccountMapper.selectOne(new QueryWrapper<PayUserAccount>()
                .eq(Constants.USERID, payWithdrawDetail.getUserId())
                .eq(Constants.CURRENCY_TYPE, 1));
        Assert.notNull(payUserAccount, Constants.NO_RECORD, AdminEnum.FAILED.getCode());

        PayTransactions payTransactions = payTransactionsMapper.selectOne(new QueryWrapper<PayTransactions>()
                .eq("detail_id", cancelValidate.getDetailId()));

        PayWithdrawalConfigure payWithdrawalConfigure = payWithdrawalLimitsMapper.selectOne(
                new QueryWrapper<PayWithdrawalConfigure>().eq("is_active", 1).last(Constants.LIMIT1));
        Assert.notNull(payWithdrawalConfigure, "There is no such limit");

        if (payWithdrawDetail.getStatus() == StatusEnums.PENDING.getCode()) {
            BigDecimal withdrawAmount = payWithdraw.getAmount();
            BigDecimal currentTotalAtUserAccount = payUserAccount.getBalance();
            BigDecimal commission = BigDecimal.valueOf(payWithdraw.getCommission());
            BigDecimal commissionAmount = withdrawAmount.multiply(commission);
            BigDecimal amountToAdd = withdrawAmount.subtract(commissionAmount);
            currentTotalAtUserAccount = currentTotalAtUserAccount.add(amountToAdd);

            String completeTime = PayUtils.createDateTime();
            payUserAccount.setBalance(currentTotalAtUserAccount);
            payUserAccountMapper.updateById(payUserAccount);

            payWithdrawDetail.setStatus(StatusEnums.FAIL.getCode());
            payWithdrawDetail.setCompleteTime(completeTime);
            payWithdrawDetail.setDescription(cancelValidate.getDescription());
            payWithdraw.setStatus(StatusEnums.FAIL.getCode());
            payWithdraw.setCompleteTime(completeTime);
            payTransactions.setStatus(StatusEnums.FAIL.getCode());
            payTransactions.setCompleteTime(completeTime);
            AdminUtils.updateRedis(cancelValidate.getDetailId(),StatusEnums.FAIL.getCode(),completeTime);
            AdminUtils.updateWithdrawRedis(cancelValidate.getDetailId(),StatusEnums.FAIL.getCode(),completeTime);
            payWithdrawDetailMapper.updateById(payWithdrawDetail);
            payWithdrawMapper.updateById(payWithdraw);
            payTransactionsMapper.updateById(payTransactions);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public long databaseDataSize(){
        return payWithdrawMapper.selectCount(null);
    }

}

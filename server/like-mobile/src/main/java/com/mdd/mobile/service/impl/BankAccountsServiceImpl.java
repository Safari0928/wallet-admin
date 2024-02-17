package com.mdd.mobile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.payAcoounts.PayUserAccount;
import com.mdd.common.entity.payBankAccounts.PayBankAccount;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.payAccount.PayUserAccountMapper;
import com.mdd.common.mapper.payBankAccounts.PayBankAccountsMapper;
import com.mdd.common.util.PayUtils;
import com.mdd.common.util.RedisUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.mobile.LikeMobileThreadLocal;
import com.mdd.mobile.enums.CardTypeEnum;
import com.mdd.mobile.enums.PayError;
import com.mdd.mobile.enums.StatusEnums;
import com.mdd.mobile.service.IBankAccountsService;
import com.mdd.mobile.validate.bankAccount.BankAccountValidate;
import com.mdd.mobile.validate.bankAccount.IbanValidate;
import com.mdd.mobile.validate.common.Constants;
import com.mdd.mobile.validate.common.PageValidate;
import com.mdd.mobile.vo.bankAccount.AllBankAccountsVo;
import com.mdd.mobile.vo.bankAccount.BankAccountsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class BankAccountsServiceImpl implements IBankAccountsService {
    @Resource
    PayBankAccountsMapper payBankAccountsMapper;

    @Resource
    PayUserAccountMapper payUserAccountMapper;

    private final ReentrantLock withdrawalLock = new ReentrantLock();

    @Override
    public PageResult<BankAccountsVo> getBankAccountInfo(PageValidate pageValidate,String userId) {
        Integer pageNo = pageValidate.getPageNo();
        Integer pageSize = pageValidate.getPageSize();
        QueryWrapper<PayBankAccount> bankAccountQueryWrapper  = new QueryWrapper<>();
        bankAccountQueryWrapper.eq(Constants.USERID,userId);
        Assert.notNull(bankAccountQueryWrapper ,"You haven't any your own bank account by IBAN");

        IPage<PayBankAccount> iPage = payBankAccountsMapper.selectPage((new Page<>(pageNo, pageSize)),bankAccountQueryWrapper);
        List<BankAccountsVo> list = new LinkedList<>();
        for (PayBankAccount payBankAccount : iPage.getRecords()){
            QueryWrapper<PayUserAccount> userAccountQueryWrapper = new QueryWrapper<>();
            userAccountQueryWrapper.eq(Constants.USERID, userId);
            userAccountQueryWrapper.eq(Constants.CURRENCY_TYPE,1);
            PayUserAccount payUserAccount = payUserAccountMapper.selectOne(userAccountQueryWrapper);
            if (payUserAccount != null && payBankAccount.getIsDelete()!=StatusEnums.FAIL.getCode()) {
                BankAccountsVo bankAccountsVo = new BankAccountsVo();
                bankAccountsVo.setBankCardName(payBankAccount.getBankCardName());
                bankAccountsVo.setCardType(mapCardType(payBankAccount.getCardType()));
                bankAccountsVo.setIban(payBankAccount.getIban());
                bankAccountsVo.setBankName("İs Bankasi");

                BeanUtils.copyProperties(payBankAccount, bankAccountsVo);
                list.add(bankAccountsVo);
            }
            Assert.notNull(payUserAccount,"You do not have a registered account.");
        }
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list);
    }

    public static String mapCardType(int cardType) {
        if (cardType >= 1 && cardType <= CardTypeEnum.values().length) {
            return CardTypeEnum.values()[cardType - 1].getMsg();
        } else {
            return "Unknown Type";
        }
    }

    @Override
    public AllBankAccountsVo addBankAccount(BankAccountValidate bankAccountValidate){
        withdrawalLock.lock();
        try{
            String userId = (String) LikeMobileThreadLocal.get("userId");
            PayBankAccount payBankAccount = payBankAccountsMapper.selectOne(
                    new QueryWrapper<PayBankAccount>()
                            .eq("iban",bankAccountValidate.getIban()));
            Assert.isNull(payBankAccount,"A bank account linked to this bank already exists");
            PayUserAccount payUserAccount = payUserAccountMapper.selectOne(
                    new QueryWrapper<PayUserAccount>()
                            .eq(Constants.USERID,userId)
                            .eq(Constants.CURRENCY_TYPE,1)
            );
            Assert.notNull(payUserAccount,"You need to create an account and verify your account.");

            if(payUserAccount.getVerifyAccount()==-1 || payUserAccount.getVerifyAccount()==2){
                int code = PayError.USER_ACCOUNT_NOT_VERIFIED.getCode();
                String msg = PayError.USER_ACCOUNT_NOT_VERIFIED.getMsg();
                throw new OperateException(msg,code);
            }else if(payUserAccount.getVerifyAccount()==1){
                PayBankAccount model = new PayBankAccount();
                model.setUuid(ToolUtils.makeUUID());
                model.setUserId(userId);
                model.setIban(bankAccountValidate.getIban());
                model.setBankCardName(bankAccountValidate.getBankCardName());
                model.setCardType(bankAccountValidate.getCardType());
                model.setCreateTime(PayUtils.createDateTime());
                model.setCurrencyId("1");
                model.setAccountId(payUserAccount.getUuid());
                int add = payBankAccountsMapper.insert(model);
                if(add<=0){
                    int code = PayError.ASSERT_MYBATIS_ERROR.getCode();
                    String msg = PayError.ASSERT_MYBATIS_ERROR.getMsg();
                    throw new OperateException(msg,code);
                }
                AllBankAccountsVo allBankAccountsVo = new AllBankAccountsVo();
                allBankAccountsVo.setBankCardName(bankAccountValidate.getBankCardName());
                allBankAccountsVo.setBankName("İş Bankası");
                allBankAccountsVo.setIban(bankAccountValidate.getIban());

                String key = Constants.BANK_ACCOUNT +userId;
                RedisUtils.del(key);
                return allBankAccountsVo;
            }
        }finally {
            withdrawalLock.unlock();
        }
        return null;
    }

    @Override
    public boolean deleteBankAccount(IbanValidate ibanValidate){
        String userId = (String) LikeMobileThreadLocal.get("userId");
        PayBankAccount payBankAccount = payBankAccountsMapper.selectOne(
                new QueryWrapper<PayBankAccount>()
                        .eq("iban",ibanValidate.getIban())
                        .eq(Constants.USERID,userId)
        );
        Assert.notNull(payBankAccount,"There is no account registered to this iban.");

        if(payBankAccount.getIsDelete()>0){
            payBankAccount.setDeleteTime(PayUtils.createDateTime());
            payBankAccount.setIsDelete(StatusEnums.FAIL.getCode());
            payBankAccountsMapper.updateById(payBankAccount);
            return true;
        }else{
            int code = PayError.BANK_ACCOUNT_ALREADY_DELETE.getCode();
            String msg = PayError.BANK_ACCOUNT_ALREADY_DELETE.getMsg();
            throw new OperateException(msg,code);
        }
    }
}
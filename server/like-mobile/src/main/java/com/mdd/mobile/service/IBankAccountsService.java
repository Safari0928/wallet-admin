package com.mdd.mobile.service;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.payBankAccounts.PayBankAccount;
import com.mdd.mobile.validate.bankAccount.BankAccountValidate;
import com.mdd.mobile.validate.bankAccount.IbanValidate;
import com.mdd.mobile.validate.common.PageValidate;
import com.mdd.mobile.vo.bankAccount.AllBankAccountsVo;
import com.mdd.mobile.vo.bankAccount.BankAccountsVo;

public interface IBankAccountsService {

    PageResult<BankAccountsVo> getBankAccountInfo(PageValidate pageValidate, String userId);

    AllBankAccountsVo addBankAccount(BankAccountValidate bankAccountValidate);

    boolean deleteBankAccount(IbanValidate ibanValidate);

}

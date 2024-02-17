package com.mdd.mobile.service;

import com.mdd.common.core.PageResult;
import com.mdd.mobile.validate.bankCard.BankCardValidate;
import com.mdd.mobile.validate.common.PageValidate;
import com.mdd.mobile.vo.BankCardVo;
import com.mdd.mobile.vo.deposit.ErrVo;

public interface IBankCardService {

    ErrVo addBankCard(BankCardValidate bankCardValidate);

    ErrVo cancelBankCard(String userId, String bankCardId);

    PageResult<BankCardVo> list(PageValidate pageValidate, String userId);
}

package com.mdd.mobile.service;

import com.mdd.common.core.PageResult;
import com.mdd.common.entity.payWithdraw.PayWithdrawDetail;
import com.mdd.mobile.validate.common.PageValidate;
import com.mdd.mobile.validate.withdraw.WithdrawCodeValidate;
import com.mdd.mobile.validate.withdraw.WithdrawValidate;
import com.mdd.mobile.vo.withdraw.VerifyWithdrawVo;
import com.mdd.mobile.vo.withdraw.WithdrawDetailVo;
import com.mdd.mobile.vo.withdraw.WithdrawDetailsVo;
import com.mdd.mobile.vo.withdraw.WithdrawVo;

public interface IPayWithdrawService {

    WithdrawVo addWithdraw(WithdrawValidate withdrawValidate);

    VerifyWithdrawVo addVerifyCode(WithdrawCodeValidate withdrawCodeValidate);

    PayWithdrawDetail addDetail(String uuid);

    PageResult<WithdrawDetailsVo> getAll(PageValidate pageValidate,String userId);

    WithdrawDetailVo getDetail(String uuid);

}

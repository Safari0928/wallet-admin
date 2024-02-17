package com.mdd.payadmin.service;

import com.mdd.common.core.PageResult;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.validate.withdraw.ApproveValidate;
import com.mdd.payadmin.validate.withdraw.CancelValidate;
import com.mdd.payadmin.validate.withdraw.WithdrawSearchValidate;
import com.mdd.payadmin.vo.WithdrawDetailVo;
import com.mdd.payadmin.vo.WithdrawVo;

import java.util.List;

public interface IWithdrawService {

    PageResult<WithdrawVo> getDatas(PageValidate pageValidate, WithdrawSearchValidate searchValidate);

    List<WithdrawVo> getAll();

    WithdrawDetailVo getDetailById(String detailId);

    boolean makeApprove(ApproveValidate approveValidate);

    long databaseDataSize();

    boolean makeCancel(CancelValidate cancelValidate);

}

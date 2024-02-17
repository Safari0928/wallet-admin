package com.mdd.payadmin.service;

import com.mdd.payadmin.validate.withdraw.UpdateWithdrawConfigureValidate;
import com.mdd.payadmin.vo.WithdrawConfigureVo;

public interface IWithdrawConfigureService {

    WithdrawConfigureVo list();

    boolean update(UpdateWithdrawConfigureValidate validate);
}

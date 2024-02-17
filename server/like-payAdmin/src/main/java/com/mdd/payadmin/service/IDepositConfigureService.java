package com.mdd.payadmin.service;

import com.mdd.payadmin.validate.configure.UpdateDepositConfigureValidate;
import com.mdd.payadmin.vo.configure.DepositConfigureListVo;

public interface IDepositConfigureService {
    DepositConfigureListVo list();
    boolean update(UpdateDepositConfigureValidate configureValidate);
}

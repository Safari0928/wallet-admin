package com.mdd.payadmin.service;

import com.mdd.payadmin.validate.SystemLoginValidate;
import com.mdd.payadmin.vo.SystemLoginVo;

public interface ISystemLoginService {
    SystemLoginVo login(SystemLoginValidate loginValidate);

    void logout(String token);
}

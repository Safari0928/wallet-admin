package com.mdd.mobile.service;

import com.mdd.mobile.validate.login.PayRegisterValidate;
import com.mdd.mobile.vo.payLogin.PayLoginVo;

public interface IPayLoginService {

    /**
     * save user,account
     * @param payRegisterValidate
     * @return Boolean
     */
    Boolean register(PayRegisterValidate payRegisterValidate);

    /**
     * login to the application with the phone
     * @param mobile accountNumber
     * @param  password
     * @return PayloginVo
     */
    PayLoginVo mobileLogin(String mobile, String password);

    void logout(String token);

    Boolean forgetPassword(String password);
}

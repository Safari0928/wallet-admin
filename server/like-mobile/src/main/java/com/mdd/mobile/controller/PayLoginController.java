package com.mdd.mobile.controller;

import com.mdd.common.aop.NotLogin;
import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.util.RedisUtils;
import com.mdd.mobile.enums.PayError;
import com.mdd.mobile.validate.common.Constants;
import com.mdd.mobile.validate.login.ForgetPasswordValidate;
import com.mdd.mobile.validate.login.PayLoginPhoneValidate;
import com.mdd.mobile.validate.login.PayRegisterValidate;
import com.mdd.mobile.vo.payLogin.PayLoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.mdd.mobile.service.IPayLoginService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/login")
@Api(tags = " System Login Controller")
public class PayLoginController {

    @Resource
    IPayLoginService IPayLoginService;

    @NotLogin
    @PostMapping("/register")
    @ApiOperation(value="Register")
    public AjaxResult<Object> register(@Validated @RequestBody PayRegisterValidate payRegisterValidate) {

        Boolean keyPhone = (Boolean) RedisUtils.get(Constants.IS_PHONE_CHECK+payRegisterValidate.getPhone());
        Boolean keyEmail = (Boolean) RedisUtils.get(Constants.IS_PHONE_CHECK+payRegisterValidate.getEmail());

        if(!RedisUtils.exists(Constants.IS_PHONE_CHECK+payRegisterValidate.getPhone())
                || !RedisUtils.exists(Constants.IS_PHONE_CHECK+payRegisterValidate.getEmail())
        ){
            return AjaxResult.failed("Please enter the confirmation code received on your phone or email");
        }else if(keyPhone && keyEmail ){
            Boolean register=IPayLoginService.register(payRegisterValidate);
            if(register)
                return AjaxResult.success("You have successfully registered");
            else
                return AjaxResult.failed("Account could not be created.");
        }else{
            return AjaxResult.failed("Wrong confirmation code");
        }
    }

    @NotLogin
    @PostMapping("/mobileLogin")
    @ApiOperation(value="Mobile Login")
    public AjaxResult<PayLoginVo> mobileLogin(@Validated @RequestBody PayLoginPhoneValidate loginPhoneValidate) {
        String mobile = loginPhoneValidate.getPhone();
        String password = loginPhoneValidate.getPassword();
        PayLoginVo loginVo = IPayLoginService.mobileLogin(mobile, password);

        if (loginVo==null) {
            return AjaxResult.failed(PayError.LOGIN_ACCOUNT_ERROR.getCode(), PayError.LOGIN_ACCOUNT_ERROR.getMsg(),null);
        }
        return AjaxResult.success(PayError.SUCCESS.getCode(),PayError.SUCCESS.getMsg(),loginVo);
    }

    @NotLogin
    @PostMapping("/forgetpassword")
    @ApiOperation(value="Mobile Login")
    public AjaxResult<Object> forgetPassword(@Validated @RequestBody ForgetPasswordValidate forget) {
        String password = forget.getNewPassword();
        if(!RedisUtils.exists(Constants.IS_PHONE_CHECK+forget.getPhone())){
            return AjaxResult.failed("Please enter the confirmation code received on your phone !");
        }
        Boolean forgetPassword = IPayLoginService.forgetPassword(password);
        if (!forgetPassword) {
            return AjaxResult.failed(PayError.FAILED.getCode(), PayError.FAILED.getMsg());
        }
        return AjaxResult.success(PayError.SUCCESS.getCode(),PayError.SUCCESS.getMsg());
    }

    @NotPower
    @NotLogin
    @PostMapping("/logout")
    @ApiOperation(value="sign out")
    public AjaxResult<Object> logout(HttpServletRequest request) {
        IPayLoginService.logout(request.getHeader("token"));
        return AjaxResult.success();
    }
}
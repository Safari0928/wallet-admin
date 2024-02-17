package com.mdd.mobile.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.util.PayUtils;
import com.mdd.common.util.RedisUtils;
import com.mdd.mobile.enums.PayError;
import com.mdd.mobile.service.ICodeService;
import com.mdd.mobile.validate.login.CodeCheckValidate;
import com.mdd.mobile.validate.common.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@RequestMapping("/code")
@Api(tags = "Code Controller")
public class CodeController {

    @Resource
    ICodeService iCodeService;

    @PostMapping("/email")
    @ApiOperation(value = "Send Vertification Code ", notes = "Sends code to mail")
    public AjaxResult<Object> sendMailVerificationCode(@RequestParam String email) {
        Boolean isSendCode=iCodeService.sendMailVerificationCode(email);
        if(isSendCode) {
            int code = PayError.SUCCESS.getCode();
            String msg = PayError.SUCCESS.getMsg();
            return AjaxResult.success(code, msg);
        }
        else{
            int code = PayError.FAILED.getCode();
            String msg = PayError.FAILED.getMsg();
            return AjaxResult.success(code, msg);
        }
    }

    @PostMapping("/phone")
    @ApiOperation(value = "Send Vertification Code ", notes = "Sends code to phone")
    public AjaxResult<Object> sendPhoneVerificationCode(@RequestParam String phone) {

        Boolean isSendCode=iCodeService.sendPhoneVerificationCode(phone);
        if(isSendCode) {
            int code = PayError.SUCCESS.getCode();
            String msg = PayError.SUCCESS.getMsg();
            return AjaxResult.success(code, msg);
        }else {
            int code = PayError.CODE_COULDNT_SEND.getCode();
            String msg = PayError.CODE_COULDNT_SEND.getMsg();
            return AjaxResult.success(code, msg);
        }
    }

    @PostMapping("/check")
    @ApiOperation(value="Code Check", notes="checks the verification code sent to the phone ")
    public AjaxResult<Object> codeCheck(@Validated @RequestBody CodeCheckValidate codeCheckValidate) {
        Boolean check=iCodeService.codeCheck(codeCheckValidate.getValue(),codeCheckValidate.getCode());

        if(check){
            RedisUtils.set(Constants.IS_PHONE_CHECK+codeCheckValidate.getValue(),check,60);
            int code = PayError.SUCCESS.getCode();
            String msg = PayError.SUCCESS.getMsg();
            return AjaxResult.success(code, msg);
        }
        else{
            RedisUtils.set(Constants.IS_PHONE_CHECK+codeCheckValidate.getValue(),check,60);
            int code = PayError.CODE_NOT_MATCHED.getCode();
            String msg = PayError.CODE_NOT_MATCHED.getMsg();
            return AjaxResult.success(code, msg);
        }
    }

    @ApiOperation(value = "Send Verification Code",notes = "Sends code")
    public boolean sendGenerateVerificationCode(String key) {
        int code = PayUtils.generateVerificationCode();
        return iCodeService.saveVerificationCode(key, String.valueOf(code));
    }

    @PostMapping("/send/forgetpassword")
    @ApiOperation(value = "Forget Password Code ", notes = "Sends code to mail")
    public AjaxResult<Object> forgetPasswordCode(@RequestParam String phone) {
        Boolean isSendCode=iCodeService.forgetPasswordCode(phone);
        if(isSendCode) {
            int code = PayError.SUCCESS.getCode();
            String msg = PayError.SUCCESS.getMsg();
            return AjaxResult.success(code, msg);
        }
        else{
            int code = PayError.FAILED.getCode();
            String msg = PayError.FAILED.getMsg();
            return AjaxResult.success(code, msg);
        }
    }

}

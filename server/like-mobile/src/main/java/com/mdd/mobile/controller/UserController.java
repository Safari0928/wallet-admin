package com.mdd.mobile.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.exception.OperateException;
import com.mdd.common.util.RedisUtils;
import com.mdd.mobile.LikeMobileThreadLocal;
import com.mdd.mobile.enums.PayError;
import com.mdd.mobile.service.IUserService;
import com.mdd.mobile.validate.common.Constants;
import com.mdd.mobile.validate.user.MailValidate;
import com.mdd.mobile.validate.user.ChangePhoneValidate;
import com.mdd.mobile.validate.user.NickNameValidate;
import com.mdd.mobile.validate.user.PasswordValidate;
import com.mdd.mobile.vo.user.ChangeMailVo;
import com.mdd.mobile.vo.user.UserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@RequestMapping("/api/user")
@Api(tags = "User")
public class UserController {

    @Resource
    IUserService iUserService;

    @GetMapping("/info")
    @ApiOperation(value = "User info")
    public AjaxResult<UserInfoVo> detail() {

        String userId = (String) StpUtil.getLoginId();
        UserInfoVo vo = iUserService.info(userId);
        if (vo == null) return AjaxResult.failed(PayError.FAILED.getCode(), PayError.FAILED.getMsg(), null);

        return AjaxResult.success(PayError.SUCCESS.getCode(), PayError.SUCCESS.getMsg(), vo);
    }

    @PostMapping("/changenickname")
    @ApiOperation(value = "Change User Nick Name")
    public AjaxResult<Object> changeNickName(@Validated @RequestBody NickNameValidate nickNameValidate){
        String userId = (String) LikeMobileThreadLocal.get("userId");
        if(iUserService.changeNickName(userId,nickNameValidate)){
            int code = PayError.SUCCESS.getCode();
            String msg = PayError.SUCCESS.getMsg();
            return AjaxResult.success(code, msg, nickNameValidate);
        }else{
            int code = PayError.FAILED.getCode();
            String msg = PayError.FAILED.getMsg();
            return AjaxResult.failed(code, msg);
        }
    }

    @PutMapping("/changeavatar")
    @ApiOperation(value = "Change User Avatar")
    public AjaxResult<Object> changeAvatar( @RequestParam String avatar){
        String userId = LikeMobileThreadLocal.getUserId();
        if(iUserService.changeAvatar(userId,avatar)){
            return AjaxResult.success( PayError.SUCCESS.getCode(), PayError.SUCCESS.getMsg(), null);
        }else{
            return AjaxResult.failed(PayError.FAILED.getCode(), PayError.FAILED.getMsg(), null);
        }
    }

    @PutMapping("/changephone")
    @ApiOperation(value = "Change User Avatar")
    public AjaxResult<Object> changePhone(@Validated @RequestBody ChangePhoneValidate changePhoneValidate){
        String userId = LikeMobileThreadLocal.getUserId();
        String phone =changePhoneValidate.getPhone();
        String newPhone =changePhoneValidate.getNewPhone();

        if(!iUserService.changePhone(userId,newPhone,phone)){
            return AjaxResult.failed(PayError.FAILED.getCode(), PayError.FAILED.getMsg(), null);
        }else{
            return AjaxResult.success( PayError.SUCCESS.getCode(), PayError.SUCCESS.getMsg(), newPhone);
        }
    }

    @PostMapping("/changepassword")
    @ApiOperation(value="Change User Password")
    public AjaxResult<Object> changePassword(@Validated @RequestBody PasswordValidate passwordValidate){
        String oldPwd = passwordValidate.getOldPassword();
        String newPwd = passwordValidate.getNewPassword();
        String newPwdAgain = passwordValidate.getNewPasswordAgain();
        String userId = (String) LikeMobileThreadLocal.get("userId");

        if(iUserService.changePassword(userId,oldPwd,newPwd,newPwdAgain)){
            int code = PayError.SUCCESS.getCode();
            String msg = PayError.SUCCESS.getMsg();
            return AjaxResult.success(code, msg);
        }else{
            int code = PayError.FAILED.getCode();
            String msg = PayError.FAILED.getMsg();
            return AjaxResult.failed(code, msg);
        }
    }

    @PostMapping("/changeemail")
    @ApiOperation(value = "Change E-Mail")
    public AjaxResult<ChangeMailVo> changeMail(@Validated @RequestBody MailValidate mailValidate){
        String userId = (String) LikeMobileThreadLocal.get("userId");
        String key =Constants.LOGIN_CODES+mailValidate.getOldMail();
        String value = (String) RedisUtils.get(key);
        if(!RedisUtils.exists(key)){
            int code= PayError.CODE_NOT_FOUND.getCode();
            String message = PayError.CODE_NOT_FOUND.getMsg();
            throw new OperateException(message,code);
        }else if(value.equals(mailValidate.getCode())){
            ChangeMailVo changeMailVo = iUserService.changeMail(userId,mailValidate);
            return AjaxResult.success(changeMailVo);
        }else{
            int code= PayError.CODE_NOT_MATCHED.getCode();
            String message = PayError.CODE_NOT_MATCHED.getMsg();
            throw new OperateException(message,code);
        }
    }

}

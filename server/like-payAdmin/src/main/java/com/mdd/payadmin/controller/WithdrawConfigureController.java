package com.mdd.payadmin.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.exception.OperateException;
import com.mdd.payadmin.LikePayAdminThreadLocal;
import com.mdd.payadmin.enums.AdminEnum;
import com.mdd.payadmin.enums.StatusEnums;
import com.mdd.payadmin.service.IWithdrawConfigureService;
import com.mdd.payadmin.validate.withdraw.UpdateWithdrawConfigureValidate;
import com.mdd.payadmin.vo.WithdrawConfigureVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/withdrawconfigure")
@Api(value = "Withdraw Configure Controller")
public class WithdrawConfigureController {

    @Resource
    IWithdrawConfigureService iWithdrawConfigureService;

    @GetMapping("/default")
    @ApiOperation(value = "Default Admin Withdrawal Configure Page")
    public AjaxResult<WithdrawConfigureVo> list(){
        WithdrawConfigureVo withdrawConfigureVo = iWithdrawConfigureService.list();
        if (withdrawConfigureVo == null) return AjaxResult.failed(AdminEnum.FAILED.getCode(), AdminEnum.FAILED.getMsg(), null);

        return AjaxResult.success(AdminEnum.SUCCESS.getCode(), AdminEnum.SUCCESS.getMsg(), withdrawConfigureVo);
    }

    @PostMapping("/update")
    @ApiOperation(value = "Change Withdrawal configure")
    public AjaxResult<Object> update(@Validated @RequestBody UpdateWithdrawConfigureValidate validate){
        String role = LikePayAdminThreadLocal.getRole();
        if (!"0".equals(role)){
            throw new OperateException(ErrorEnum.NO_PERMISSION.getMsg(), ErrorEnum.NO_PERMISSION.getCode());
        }
        if(iWithdrawConfigureService.update(validate)){
            return AjaxResult.success(StatusEnums.SUCCESS.getCode(),StatusEnums.SUCCESS.getMsg());
        }
        return AjaxResult.failed(StatusEnums.FAIL.getCode(),StatusEnums.FAIL.getMsg());
    }

}

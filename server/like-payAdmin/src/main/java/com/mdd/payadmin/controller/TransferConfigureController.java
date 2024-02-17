package com.mdd.payadmin.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.exception.OperateException;
import com.mdd.payadmin.LikePayAdminThreadLocal;
import com.mdd.payadmin.enums.StatusEnums;
import com.mdd.payadmin.service.ITransferConfigureService;
import com.mdd.payadmin.validate.transfer.*;
import com.mdd.payadmin.vo.TransferConfigureVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/transferconfigure")
public class TransferConfigureController {
    @Resource
    ITransferConfigureService transferConfigure;

    @GetMapping("/default")
    @ApiOperation(value = "default transfer configure page")
    public AjaxResult<TransferConfigureVo> page(@RequestParam(value = "currency", required = false, defaultValue = "1") Integer currency) {
        TransferConfigureVo vo = transferConfigure.defaultPage(currency);
        return AjaxResult.success(vo);
    }

    @PostMapping("/update")
    @ApiOperation(value = "Change our Transfer Configure")
    public AjaxResult update(@Validated @RequestBody TransferConfigureValidate validate){
        String role = LikePayAdminThreadLocal.getRole();
        if (!"0".equals(role)){
            throw new OperateException(ErrorEnum.NO_PERMISSION.getMsg(), ErrorEnum.NO_PERMISSION.getCode());
        }
        if(transferConfigure.update(validate)){
            return AjaxResult.success();
        }
        return AjaxResult.failed(StatusEnums.FAIL.getMsg());
    }
}

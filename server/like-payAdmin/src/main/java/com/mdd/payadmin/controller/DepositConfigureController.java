package com.mdd.payadmin.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.exception.OperateException;
import com.mdd.payadmin.LikePayAdminThreadLocal;
import com.mdd.payadmin.enums.AdminEnum;
import com.mdd.payadmin.service.IDepositConfigureService;
import com.mdd.payadmin.validate.configure.UpdateDepositConfigureValidate;
import com.mdd.payadmin.vo.configure.DepositConfigureListVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@RequestMapping("/api/depositconfigure")
public class DepositConfigureController {

    @Resource
    IDepositConfigureService configureService;

    @GetMapping("/default")
    @ApiOperation(value = "Deposit Configure Controller")
    public AjaxResult<DepositConfigureListVo> detail() {
        DepositConfigureListVo vo = configureService.list();

        if (vo == null) return AjaxResult.failed(AdminEnum.FAILED.getCode(), AdminEnum.FAILED.getMsg(), null);

        return AjaxResult.success(AdminEnum.SUCCESS.getCode(), AdminEnum.SUCCESS.getMsg(), vo);
    }

    @PostMapping("/update")
    public AjaxResult<Object> update(@Validated @RequestBody UpdateDepositConfigureValidate configureValidate) {
        String role = LikePayAdminThreadLocal.getRole();
        if (!"0".equals(role)){
            throw new OperateException(ErrorEnum.NO_PERMISSION.getMsg(), ErrorEnum.NO_PERMISSION.getCode());
        }
        if (!configureService.update(configureValidate)) {
            return AjaxResult.failed(AdminEnum.UPDATE_ERROR.getCode(), AdminEnum.UPDATE_ERROR.getMsg(), null);
        }
        return AjaxResult.success(AdminEnum.SUCCESS.getCode(), AdminEnum.SUCCESS.getMsg(), null);
    }

}

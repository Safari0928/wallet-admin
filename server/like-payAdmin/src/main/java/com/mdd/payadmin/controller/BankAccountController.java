package com.mdd.payadmin.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.enums.AdminEnum;
import com.mdd.payadmin.service.IBankAccountService;
import com.mdd.payadmin.vo.BankAccountVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/bankaccount")
public class BankAccountController {

    @Resource
    IBankAccountService iBankAccountService;

    @GetMapping("/list")
    @ApiOperation(value ="Get All Bank Account")
    public AjaxResult<PageResult<BankAccountVo>> list(@Validated PageValidate pageValidate, @RequestParam String userId){
        PageResult<BankAccountVo> list = iBankAccountService.list(pageValidate,userId);
        if (list == null) {
            return AjaxResult.failed(AdminEnum.FAILED.getCode(), AdminEnum.FAILED.getMsg(), null);
        }
        return AjaxResult.success(AdminEnum.SUCCESS.getCode(), AdminEnum.SUCCESS.getMsg(), list);
    }
}

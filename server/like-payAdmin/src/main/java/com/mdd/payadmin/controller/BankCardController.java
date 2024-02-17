package com.mdd.payadmin.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.enums.AdminEnum;
import com.mdd.payadmin.service.IBankCardService;
import com.mdd.payadmin.vo.bankCard.BankCardVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/bankcard")
public class BankCardController {

    @Resource
    IBankCardService iBankCardService;
    @GetMapping("/list")
    @ApiOperation(value = "get All Bank Cards")
    public AjaxResult<PageResult<BankCardVo>> list(@Validated PageValidate pageValidate ,@RequestParam String userId) {

        PageResult<BankCardVo> list = iBankCardService.list(pageValidate,userId);

        if (list == null) {
            return AjaxResult.failed(AdminEnum.FAILED.getCode(), AdminEnum.FAILED.getMsg(), null);
        }
        return AjaxResult.success(AdminEnum.SUCCESS.getCode(), AdminEnum.SUCCESS.getMsg(), list);
    }
}

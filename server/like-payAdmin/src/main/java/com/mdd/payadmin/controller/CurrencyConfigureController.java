package com.mdd.payadmin.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.exception.OperateException;
import com.mdd.payadmin.LikePayAdminThreadLocal;
import com.mdd.payadmin.enums.AdminEnum;
import com.mdd.payadmin.service.ICurrencyConfigureService;
import com.mdd.payadmin.validate.currency.CurrencyValidate;
import com.mdd.payadmin.vo.CurrencyConfigureVo;
import com.mdd.payadmin.vo.CurrencyEditVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/currencyconfigure")
@Api(value = "Currency Configure Controller")
public class CurrencyConfigureController {

    @Resource
    ICurrencyConfigureService iCurrencyConfigureService;

    @GetMapping("/list")
    @ApiOperation(value = "default currency configure page")
    public AjaxResult<List<CurrencyConfigureVo>> list(){
        List<CurrencyConfigureVo> currencyConfigureVo = iCurrencyConfigureService.list();
        if (currencyConfigureVo == null) return AjaxResult.failed(AdminEnum.FAILED.getCode(), AdminEnum.FAILED.getMsg(), null);

        return AjaxResult.success(AdminEnum.SUCCESS.getCode(), AdminEnum.SUCCESS.getMsg(), currencyConfigureVo);
    }

    @GetMapping("/detail")
    @ApiOperation(value = "default currency configure page")
    public AjaxResult<CurrencyEditVo> detail(@RequestParam String currencyID){
        CurrencyEditVo currencyEditVo = iCurrencyConfigureService.detail(currencyID);
        if (currencyEditVo == null) return AjaxResult.failed(AdminEnum.FAILED.getCode(), AdminEnum.FAILED.getMsg(), null);

        return AjaxResult.success(AdminEnum.SUCCESS.getCode(), AdminEnum.SUCCESS.getMsg(), currencyEditVo);
    }

    @PostMapping("/update")
    @ApiOperation(value = "Update Currency Type")
    public AjaxResult<Object> updateCurrency(@Validated @RequestBody CurrencyValidate currencyValidate){
        String role = LikePayAdminThreadLocal.getRole();
        if (!"0".equals(role)){
            throw new OperateException(ErrorEnum.NO_PERMISSION.getMsg(), ErrorEnum.NO_PERMISSION.getCode());
        }
        boolean result = iCurrencyConfigureService.editCurrency(currencyValidate);
        if (!result) return AjaxResult.failed(AdminEnum.FAILED.getCode(), AdminEnum.FAILED.getMsg(), null);

        return AjaxResult.success(AdminEnum.SUCCESS.getCode(), AdminEnum.SUCCESS.getMsg());
    }
}

package com.mdd.mobile.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.mobile.LikeMobileThreadLocal;
import com.mdd.mobile.service.IPayIndexService;
import com.mdd.mobile.validate.account.VerifyAccountValidate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/index")
@Api(tags = "Index Controller")
public class PayIndexController {

    @Resource
    IPayIndexService payIndexService;

    @GetMapping("/account")
    @ApiOperation(value="User Account")
    public AjaxResult<Map<String, Object>> userAccount() {
        String userId = LikeMobileThreadLocal.getUserId();
        Map<String, Object> account =payIndexService.userAccount(userId);
        return AjaxResult.success(account);
    }

    @PostMapping("/verify/account")
    @ApiOperation(value="Verify Account",notes="Verifies the user account and account verification is pending")
    public AjaxResult<Object> verifyAccount(@Validated @RequestBody  VerifyAccountValidate verifyAccountValidate){
        Boolean verifyAccount = payIndexService.verifyAccount(verifyAccountValidate);
        if(verifyAccount)
            return AjaxResult.success("success");
        else
            return AjaxResult.failed("user could not be verified");
    }

}




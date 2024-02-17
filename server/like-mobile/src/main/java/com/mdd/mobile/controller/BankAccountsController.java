package com.mdd.mobile.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.util.RedisUtils;
import com.mdd.mobile.LikeMobileThreadLocal;
import com.mdd.mobile.enums.PayError;
import com.mdd.mobile.validate.bankAccount.BankAccountValidate;
import com.mdd.mobile.validate.bankAccount.IbanValidate;
import com.mdd.mobile.validate.common.Constants;
import com.mdd.mobile.validate.common.PageValidate;
import com.mdd.mobile.vo.bankAccount.AllBankAccountsVo;
import com.mdd.mobile.vo.bankAccount.BankAccountsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.mdd.mobile.service.IBankAccountsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/bankaccounts")
@Api(tags = {"Bank Accounts Controller"},value = "Bank Accounts Controller's Api documentation")
public class BankAccountsController {

    @Resource
    private IBankAccountsService iBankAccountsService;

    @Resource
    CodeController codeController;

    @GetMapping("/list")
    @ApiOperation(value = "Get Selected Bank Accounts", notes = "Allows bank accounts to be listed in the table")
    public AjaxResult<PageResult<BankAccountsVo>> getBankAccountsVo(@Validated PageValidate pageValidate) {
        String userId = (String) LikeMobileThreadLocal.get("userId");
        PageResult<BankAccountsVo> bankAccountInfo = iBankAccountsService.getBankAccountInfo(pageValidate,userId);
        if(bankAccountInfo == null){
            return AjaxResult.failed(PayError.FAILED.getCode(), PayError.FAILED.getMsg(),null);
        }
        return AjaxResult.success(PayError.SUCCESS.getCode(), PayError.SUCCESS.getMsg(),bankAccountInfo);
    }

    @PostMapping("/add")
    @ApiOperation(value = "Add Bank Accounts")
    public AjaxResult<AllBankAccountsVo> add(@Validated @RequestBody BankAccountValidate bankAccountValidate){
        String userId = (String) LikeMobileThreadLocal.get("userId");
        AllBankAccountsVo allBankAccountsVo = new AllBankAccountsVo();
        String key = Constants.BANK_ACCOUNT +userId + bankAccountValidate.getIban();
        String redisCode = (String) RedisUtils.get(key);
        String validateCode = bankAccountValidate.getCode();
        if(validateCode.equals(redisCode)){
            allBankAccountsVo = iBankAccountsService.addBankAccount(bankAccountValidate);
            int code = PayError.SUCCESS.getCode();
            String msg = PayError.SUCCESS.getMsg();
            return AjaxResult.success(code,msg,allBankAccountsVo);
        }else{
            int code = PayError.CODE_NOT_FOUND.getCode();
            String msg = PayError.CODE_NOT_FOUND.getMsg();
            return AjaxResult.failed(code,msg,allBankAccountsVo);
        }

    }

    @PostMapping("/sendcode")
    @ApiOperation(value = "Send Verify Code To Redis")
    public boolean sendCode(@RequestParam String iban){
        String userId = (String) LikeMobileThreadLocal.get("userId");
        String key = Constants.BANK_ACCOUNT +userId +iban;
        boolean send = codeController.sendGenerateVerificationCode(key);
        return send;
    }

    @PostMapping("/delete")
    @ApiOperation(value = "Delete Bank Account")
    public AjaxResult<Object> delete(@Validated @RequestBody IbanValidate ibanValidate){
        if(iBankAccountsService.deleteBankAccount(ibanValidate)){
            int code = PayError.SUCCESS.getCode();
            String msg = PayError.SUCCESS.getMsg();
            return AjaxResult.success(code, msg, null);
        }else{
            int code = PayError.FAILED.getCode();
            String msg = PayError.FAILED.getMsg();
            return AjaxResult.failed(code, msg, null);
        }

    }
}

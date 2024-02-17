package com.mdd.mobile.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.payAcoounts.PayUserAccount;
import com.mdd.common.mapper.payAccount.PayUserAccountMapper;
import com.mdd.common.util.RedisUtils;
import com.mdd.mobile.LikeMobileThreadLocal;
import com.mdd.mobile.enums.PayError;
import com.mdd.mobile.service.IPayWithdrawService;
import com.mdd.mobile.validate.common.Constants;
import com.mdd.mobile.validate.common.PageValidate;
import com.mdd.mobile.validate.withdraw.FullWithdrawValidate;
import com.mdd.mobile.validate.withdraw.WithdrawCodeValidate;
import com.mdd.mobile.validate.withdraw.WithdrawValidate;
import com.mdd.mobile.vo.withdraw.VerifyWithdrawVo;
import com.mdd.mobile.vo.withdraw.WithdrawDetailVo;
import com.mdd.mobile.vo.withdraw.WithdrawDetailsVo;
import com.mdd.mobile.vo.withdraw.WithdrawVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/withdraw")
@Api(tags = {"Withdraw Controller"}, value="Withdraw Controller's Api Documentation")
public class PayWithdrawController {

    @Resource
    IPayWithdrawService iPayWithdrawService;

    @Resource
    CodeController codeController;

    @Resource
    PayUserAccountMapper payUserAccountMapper;

    Integer counter = 1;

    @PostMapping("/add")
    @ApiOperation(value = "Withdrawal process")
    public AjaxResult<WithdrawVo> add(@Validated @RequestBody WithdrawValidate withdrawValidate){

        String userId = (String) LikeMobileThreadLocal.get("userId");
        String uniqueKey = userId + ":" + withdrawValidate.getIban() + ":" + withdrawValidate.getAmount().toString() + ":" + withdrawValidate.getCurrencyName();
        String redisKey = Constants.WITHDRAW + uniqueKey;

        if(RedisUtils.exists(redisKey) && counter>3){
            counter = 0;
            int code = PayError.PROCESS_NOT_FINISHED.getCode();
            String msg = PayError.PROCESS_NOT_FINISHED.getMsg();
            return AjaxResult.failed(code, msg, null);
        }

        RedisUtils.set(redisKey,1,60);
        counter+=1;
        String key =Constants.WITHDRAW_VERIFY_CODE+userId;
        BigDecimal amount = withdrawValidate.getAmount();

        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            int code = PayError.PARAMS_VALID_ERROR.getCode();
            String msg = PayError.PARAMS_VALID_ERROR.getMsg();
            return AjaxResult.failed(code, msg, null);
        }

        boolean sent = codeController.sendGenerateVerificationCode(key);
        if(!sent){
            int code = PayError.ASSERT_REDIS_ERROR.getCode();
            String msg = PayError.ASSERT_REDIS_ERROR.getMsg();
            return AjaxResult.failed(code, msg, null);
        }

        WithdrawVo withdrawVo = iPayWithdrawService.addWithdraw(withdrawValidate);
        return AjaxResult.success(PayError.CONFIRMATION_CODE_REQUIRED.getMsg(), withdrawVo);
    }

    @PostMapping("/sendcode")
    @ApiOperation(value = "Send Code")
    public AjaxResult<VerifyWithdrawVo> sendCode(@Validated @RequestBody WithdrawCodeValidate withdrawCodeValidate){
        String userId = (String) LikeMobileThreadLocal.get("userId");
        String key = Constants.WITHDRAW_VERIFY_CODE+userId;
        String redisCode = (String) RedisUtils.get(key);
        String validateCode = withdrawCodeValidate.getCode();

        VerifyWithdrawVo verifyWithdrawVo = new VerifyWithdrawVo();
        if(redisCode==null){
            int code = PayError.CODE_NOT_FOUND.getCode();
            String msg = PayError.CODE_NOT_FOUND.getMsg();
            return AjaxResult.failed(code, msg, verifyWithdrawVo);
        }
        else if(redisCode.equals(validateCode)) {
            verifyWithdrawVo = iPayWithdrawService.addVerifyCode(withdrawCodeValidate);
            return AjaxResult.success(verifyWithdrawVo);
        }else {
            int code = PayError.CAPTCHA_ERROR.getCode();
            String msg = PayError.CAPTCHA_ERROR.getMsg();
            return AjaxResult.failed(code, msg, verifyWithdrawVo);
        }
    }

    @PostMapping("/fullwithdrawal")
    @ApiOperation(value = "Full Withdrawal process")
    public AjaxResult<WithdrawVo> addFullWithdrawal(@Validated @RequestBody FullWithdrawValidate fullWithdrawValidate){
        String userId = (String) LikeMobileThreadLocal.get("userId");
        PayUserAccount payUserAccount = payUserAccountMapper.selectOne(
                new QueryWrapper<PayUserAccount>()
                        .eq(Constants.USERID,userId)
                        .eq(Constants.CURRENCY_TYPE,1)
        );
        WithdrawValidate withdrawValidate = new WithdrawValidate();
        BigDecimal userBalance = payUserAccount.getBalance();

        withdrawValidate.setAmount(userBalance);
        withdrawValidate.setIban(fullWithdrawValidate.getIban());
        withdrawValidate.setCurrencyName(fullWithdrawValidate.getCurrencyName());

        String uniqueKey = userId + ":" + withdrawValidate.getIban() + ":" + withdrawValidate.getAmount().toString() + ":" + withdrawValidate.getCurrencyName();
        String redisKey = Constants.WITHDRAW + uniqueKey;

        if(RedisUtils.exists(redisKey) && counter>3){
            counter = 0;
            int code = PayError.PROCESS_NOT_FINISHED.getCode();
            String msg = PayError.PROCESS_NOT_FINISHED.getMsg();
            return AjaxResult.failed(code, msg, null);
        }

        RedisUtils.set(redisKey,1,60);
        counter+=1;
        String key =Constants.WITHDRAW_VERIFY_CODE+userId;
        BigDecimal amount = withdrawValidate.getAmount();

        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            int code = PayError.PARAMS_VALID_ERROR.getCode();
            String msg = PayError.PARAMS_VALID_ERROR.getMsg();
            return AjaxResult.failed(code, msg, null);
        }

        boolean sent = codeController.sendGenerateVerificationCode(key);
        if(!sent){
            int code = PayError.ASSERT_REDIS_ERROR.getCode();
            String msg = PayError.ASSERT_REDIS_ERROR.getMsg();
            return AjaxResult.failed(code, msg, null);
        }

        WithdrawVo withdrawVo = iPayWithdrawService.addWithdraw(withdrawValidate);
        return AjaxResult.success(PayError.CONFIRMATION_CODE_REQUIRED.getMsg(), withdrawVo);

    }

    @GetMapping("/list")
    @ApiOperation(value = "Get All Withdrawals Of A User")
    public AjaxResult<PageResult<WithdrawDetailsVo>> getAllDetails(@Validated PageValidate pageValidate){
        String userId = (String) LikeMobileThreadLocal.get("userId");
        PageResult<WithdrawDetailsVo>detailsList = iPayWithdrawService.getAll(pageValidate,userId);
        return AjaxResult.success(detailsList);

    }

    @GetMapping("/detail")
    @ApiOperation(value = "Get Detail Withdrawal Of A User")
    public AjaxResult<WithdrawDetailVo> detailByUuid(@RequestParam String uuid){
        WithdrawDetailVo vo = iPayWithdrawService.getDetail(uuid);
        return AjaxResult.success(vo);
    }

}

package com.mdd.mobile.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.util.RedisUtils;
import com.mdd.mobile.LikeMobileThreadLocal;
import com.mdd.mobile.enums.PayError;
import com.mdd.mobile.service.IBankCardService;
import com.mdd.mobile.validate.bankCard.BankCardValidate;
import com.mdd.mobile.validate.bankCard.CancelBankCardValidate;
import com.mdd.mobile.validate.common.PageValidate;
import com.mdd.mobile.vo.BankCardVo;
import com.mdd.mobile.vo.deposit.ErrVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@RequestMapping("/api/bankcard")
@Api(tags = "Bank card ")
public class BankCardController {
    @Resource
    IBankCardService iBankCardService;
    @Resource
    CodeController codeController;

    @GetMapping("/list")
    @ApiOperation(value = "get All Bank Cards")
    public AjaxResult<PageResult<BankCardVo>> list(@Validated PageValidate pageValidate, @RequestParam String userId) {

        PageResult<BankCardVo> bankCardList = iBankCardService.list(pageValidate, userId);

        if (bankCardList == null) {
            return AjaxResult.failed(PayError.FAILED.getCode(), PayError.FAILED.getMsg(), null);
        }
        return AjaxResult.success(PayError.SUCCESS.getCode(), PayError.SUCCESS.getMsg(), bankCardList);
    }

    @PostMapping("/add")
    @ApiOperation(value = "Add bank card")
    public AjaxResult<Object> addBankCard(@Validated @RequestBody BankCardValidate bankCardValidate) {
        String userId=LikeMobileThreadLocal.getUserId();
        String key = (String) RedisUtils.get("phone: "+userId);
        if (!key.equals(bankCardValidate.getCode())) {
            return AjaxResult.failed(PayError.FAILED.getCode(), PayError.FAILED.getMsg(), null);
        }
        ErrVo errVo = iBankCardService.addBankCard(bankCardValidate);

        if (errVo.getCode() != 200) {
            return AjaxResult.failed(errVo.getCode(), errVo.getMsg());
        }
        return AjaxResult.success(errVo.getCode(), errVo.getMsg(), errVo.getData());
    }

    @DeleteMapping("/cancel")
    @ApiOperation(value = "Cancel bank card")
    public AjaxResult<Object> cancelCard(@Validated @RequestParam String bankCardId) {
        String userId = LikeMobileThreadLocal.getUserId();
        ErrVo errVo = iBankCardService.cancelBankCard(userId, bankCardId);

        if (!errVo.getCode().equals(200)) {
            return AjaxResult.failed(errVo.getCode(), errVo.getMsg());
        }
        return AjaxResult.success(errVo.getCode(), errVo.getMsg(), errVo.getData());
    }


    @GetMapping("/getcode")
    @ApiOperation(value = "get code card")
    public AjaxResult<Object> addBankCard() {
    String userId = LikeMobileThreadLocal.getUserId();
        codeController.sendGenerateVerificationCode("phone: "+userId);

        return AjaxResult.success(PayError.SUCCESS.getCode(), PayError.SUCCESS.getMsg());
    }


}

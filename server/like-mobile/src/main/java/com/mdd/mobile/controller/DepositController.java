package com.mdd.mobile.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.util.RedisUtils;
import com.mdd.mobile.LikeMobileThreadLocal;
import com.mdd.mobile.enums.PayError;
import com.mdd.mobile.validate.common.Constants;
import com.mdd.mobile.validate.common.PageValidate;
import com.mdd.mobile.validate.deposit.DepositCompleteValidate;
import com.mdd.mobile.validate.deposit.CreateDepositValidate;
import com.mdd.mobile.validate.deposit.SendDepositValidate;
import com.mdd.mobile.vo.deposit.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.mdd.mobile.service.IDepositService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/deposit")
@Api(tags = "Deposit")
public class DepositController {

    @Resource
    IDepositService iDepositService;

    @GetMapping("/list")
    @ApiOperation(value = "Get All Deposit")
    public AjaxResult<PageResult<DepositVo>> list(@Validated PageValidate pageValidate) {

        PageResult<DepositVo> depositList = iDepositService.list(pageValidate);
        if (depositList == null) {
            return AjaxResult.failed(PayError.FAILED.getCode(), PayError.FAILED.getMsg(), null);
        }
        return AjaxResult.success(PayError.SUCCESS.getCode(), PayError.SUCCESS.getMsg(), depositList);

    }

    @PostMapping("/send")
    @ApiOperation(value = "Send deposit")
    public AjaxResult<Object> sendDeposit(@Validated @RequestBody SendDepositValidate sendDepositValidate) {

        /*if (sendDepositValidate.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return AjaxResult.failed(PayError.PARAMS_VALID_ERROR.getCode(),
                    PayError.PARAMS_VALID_ERROR.getMsg(),
                    null);
        }
        String uniqueKey = sendDepositValidate.getBankCardId() + sendDepositValidate.getAmount();
        String redisKey = Constants.REDIS_DEPOSIT + uniqueKey;

        if (RedisUtils.exists(redisKey)) {
            return AjaxResult.failed(PayError.PROCESS_NOT_FINISHED.getCode(), PayError.PROCESS_NOT_FINISHED.getMsg(), null);
        }*/
        //RedisUtils.set(redisKey, 1, 60);
        CompleteDepositVo completed = iDepositService.sendDeposit(sendDepositValidate);
        if (completed.getCode() != 200) {
            return AjaxResult.failed(completed.getCode(), completed.getMsg());
        }
        return AjaxResult.success(completed.getCode(), completed.getMsg(), completed.getAmount());
    }

    @PostMapping("/create")
    @ApiOperation(value = "Create deposit")
    public AjaxResult<AddDepositVo> createDeposit(@Validated @RequestBody CreateDepositValidate createDepositValidate) {

        if (createDepositValidate.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return AjaxResult.failed(PayError.PARAMS_VALID_ERROR.getCode(),
                    PayError.PARAMS_VALID_ERROR.getMsg(),
                    null);
        }

        AddDepositVo vo = iDepositService.createDeposit(createDepositValidate);
        if (vo == null) return AjaxResult.failed(PayError.FAILED.getCode(), PayError.FAILED.getMsg(), null);

        return AjaxResult.success(PayError.SUCCESS.getCode(), PayError.SUCCESS.getMsg(), vo);
    }

    @PostMapping("/complete")
    @ApiOperation(value = "complete deposit")
    public AjaxResult<Object> completeDeposit(@Validated @RequestBody DepositCompleteValidate depositCompleteValidate) {

        String depositId = depositCompleteValidate.getDepositId();
        String userId = LikeMobileThreadLocal.getUserId();
        String code = depositCompleteValidate.getCode();

        boolean verify = false;
        String key = (String) RedisUtils.get(Constants.REDIS_DEPOSIT + ":" + userId);
        if (key.equals(code)) verify = true;

        CompleteDepositVo completed = iDepositService.completeDeposit(depositId, verify);
        if (completed.getCode() != 200) {
            return AjaxResult.failed(completed.getCode(), completed.getMsg());
        }
        return AjaxResult.success(completed.getCode(), completed.getMsg(), completed.getAmount());
    }

    @GetMapping("/detail")
    @ApiOperation(value = "Deposit Detail")
    public AjaxResult<DepositDetailVo> detail(@RequestParam String detailId) {
        DepositDetailVo vo = iDepositService.detail(detailId);

        if (vo == null) return AjaxResult.failed(PayError.FAILED.getCode(), PayError.FAILED.getMsg(), null);

        return AjaxResult.success(PayError.SUCCESS.getCode(), PayError.SUCCESS.getMsg(), vo);
    }
}

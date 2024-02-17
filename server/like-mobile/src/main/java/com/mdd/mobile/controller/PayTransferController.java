package com.mdd.mobile.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.util.RedisUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.mobile.enums.PayError;
import com.mdd.mobile.service.IPayTransferService;
import com.mdd.mobile.validate.common.Constants;
import com.mdd.mobile.validate.common.PageValidate;
import com.mdd.mobile.validate.transfer.TransferValidate;
import com.mdd.mobile.vo.PayTransferDetailVo;
import com.mdd.mobile.vo.PayTransferDetailsVo;
import com.mdd.mobile.vo.PayTransferVo;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

@RestController
@RequestMapping("/api/transfer")
@Api(tags = "Transfer Controller")
public class PayTransferController {
    @Resource
    IPayTransferService iPayTransferService;

    @PostMapping("/send")
    public AjaxResult<PayTransferVo> sendMoney(@Validated @RequestBody TransferValidate transferValidate) {
        BigDecimal amount = transferValidate.getAmount();
        String toPayId = transferValidate.getToPayId();
        String uniqueId = Constants.TRANSFER_UNIQUE +System.currentTimeMillis() + ToolUtils.randomString(6);

        if (RedisUtils.exists(uniqueId)) {
            PayTransferVo vo = (PayTransferVo) RedisUtils.get(uniqueId);
            if (vo != null) {
                if (vo.getStatus() < 0) {
                    int code = PayError.PAYMENT_ERROR.getCode();
                    String msg = Constants.COMPLETE + PayError.PAYMENT_ERROR.getMsg();
                    return AjaxResult.failed(code, msg, vo);
                }
                return AjaxResult.success(Constants.COMPLETE + PayError.SUCCESS.getMsg(), vo);
            }
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0 || !toPayId.matches("\\d{10}")) {
            int code = PayError.PARAMS_VALID_ERROR.getCode();
            String msg = PayError.PARAMS_VALID_ERROR.getMsg();
            return AjaxResult.failed(code, msg, null);
        }
        PayTransferVo vo = iPayTransferService.makeTransfer(transferValidate);
        RedisUtils.set(uniqueId, vo,100);
        if (vo == null) {
            int code = PayError.USER_NOT_FOUND.getCode();
            String msg = PayError.USER_NOT_FOUND.getMsg();
            return AjaxResult.failed(code, msg, null);
        }
        if (vo.getStatus() < 0) {
            int code = PayError.PAYMENT_ERROR.getCode();
            String msg = PayError.PAYMENT_ERROR.getMsg();
            return AjaxResult.failed(code, msg, vo);
        }
        return AjaxResult.success(PayError.SUCCESS.getMsg(), vo);
    }

    @GetMapping("/detail")
    public AjaxResult<PayTransferDetailVo> detailById(@RequestParam @NotNull String transferId) {
        PayTransferDetailVo vo = iPayTransferService.getDetailByTransferId(transferId);
        if (vo == null) {
            return AjaxResult.failed(PayError.FAILED.getCode(), PayError.FAILED.getMsg(), null);
        }
        return AjaxResult.success(PayError.SUCCESS.getMsg(), vo);
    }

    @GetMapping("/getList")
    public AjaxResult<PageResult<PayTransferDetailsVo>> getListDetails(@Validated PageValidate pageValidate) {
        PageResult<PayTransferDetailsVo> detailsList = iPayTransferService.list(pageValidate);
        if (detailsList == null) {
            return AjaxResult.failed(PayError.FAILED.getCode(), PayError.FAILED.getMsg(), null);
        }
        return AjaxResult.success(PayError.SUCCESS.getMsg(), detailsList);
    }
}


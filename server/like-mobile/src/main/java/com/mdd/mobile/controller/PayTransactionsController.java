package com.mdd.mobile.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.util.RedisUtils;
import com.mdd.mobile.LikeMobileThreadLocal;
import com.mdd.mobile.enums.PayError;
import com.mdd.mobile.service.IPayTransactionsService;
import com.mdd.mobile.validate.common.Constants;
import com.mdd.mobile.validate.common.PageValidate;
import com.mdd.mobile.vo.PayTransactionsVo;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Api(tags = "Transactions Controller")
public class PayTransactionsController {

    @Resource
    IPayTransactionsService payTransactionsService;

    @GetMapping("/getList")
    public AjaxResult<PageResult<PayTransactionsVo>> getListDetails(@Validated PageValidate pageValidate) {
        String userId = LikeMobileThreadLocal.getUserId();
        if (RedisUtils.exists(Constants.TRANSACTIONS_USER+userId)){
            PageResult<PayTransactionsVo> vo = (PageResult<PayTransactionsVo>) RedisUtils.get(Constants.TRANSACTIONS_USER+userId);
            Integer pageSize = pageValidate.getPageSize();
            if (pageSize.equals(vo.getPageSize())){
                return AjaxResult.success(vo);
            }
        }
        PageResult<PayTransactionsVo> detailsList = payTransactionsService.getTransactionsList(pageValidate, userId);
        if (detailsList == null) {
            int code = PayError.USER_NOT_FOUND.getCode();
            String msg = PayError.USER_NOT_FOUND.getMsg();
            return AjaxResult.failed(code, msg, null);
        }
        RedisUtils.set(Constants.TRANSACTIONS_USER+userId,detailsList,604800);
        return AjaxResult.success(detailsList);
    }
}

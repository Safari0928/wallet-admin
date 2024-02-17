package com.mdd.payadmin.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.util.PayUtils;
import com.mdd.common.util.RedisUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.payadmin.commons.Constants;
import com.mdd.payadmin.commons.MappingUtils;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.enums.AdminEnum;
import com.mdd.payadmin.enums.StatusEnums;
import com.mdd.payadmin.service.ITransactionsService;
import com.mdd.payadmin.validate.transfer.TransferSearchValidate;
import com.mdd.payadmin.validate.withdraw.ApproveValidate;
import com.mdd.payadmin.vo.TransactionsDefaultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/transactions")
@Api(tags = "Admin Transactions Controller")
public class TransactionsController {

    @Resource
    private ITransactionsService transactionsService;

    @GetMapping("/list")
    @ApiOperation(value="Default Transaction list")
    public AjaxResult<PageResult<TransactionsDefaultVo>> list(@Validated PageValidate pageValidate, @Validated TransferSearchValidate validate) {
        Integer pageNo = pageValidate.getPageNo();
        Integer pageSize = pageValidate.getPageSize();
        String catchKey= Constants.TRANSACTION_PREFIX+pageNo+pageSize;

        if (StringUtils.isNull(validate.getTransactionNumber())
                && StringUtils.isNull(validate.getPayId())&&StringUtils.isNull(validate.getTypeDetail())
                &&StringUtils.isNull(validate.getCurrencyType())&&StringUtils.isNull(validate.getStatus()) &&StringUtils.isNull(validate.getUserId())) {
            if (RedisUtils.exists(catchKey)) {
                PageResult<TransactionsDefaultVo> list = (PageResult<TransactionsDefaultVo>) RedisUtils.get(catchKey);
                Long count = transactionsService.getCount(pageValidate);
                if (!count.equals(list.getCount())) {
                    String commonPrefix = Constants.TRANSACTION_PREFIX;
                    Set<String> keysToDelete = RedisUtils.keys(commonPrefix + "*");
                    if (keysToDelete != null && !keysToDelete.isEmpty()) {
                        RedisUtils.delP(keysToDelete.toArray(new String[0]));
                    }
                } else if (pageNo.equals(list.getPageNo()) && pageSize.equals(list.getPageSize())) {
                    return AjaxResult.success(list);
                }
            }
        }
        PageResult<TransactionsDefaultVo> list = transactionsService.list(pageValidate,validate);
        if (list.getLists().isEmpty()){
            return AjaxResult.failed(AdminEnum.FAILED.getCode(),Constants.NO_RECORD,list);
        }
        RedisUtils.set(catchKey,list,604800);
        return AjaxResult.success(list);
    }
}

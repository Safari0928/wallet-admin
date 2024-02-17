package com.mdd.payadmin.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.util.RedisUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.payadmin.commons.Constants;
import com.mdd.payadmin.enums.AdminEnum;
import com.mdd.payadmin.service.ITransferService;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.validate.transfer.TransferSearchValidate;
import com.mdd.payadmin.vo.TransferDefaultVo;
import com.mdd.payadmin.vo.TransferDetailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Set;

@RestController
@RequestMapping("/api/transfer")
@Api(tags = "Admin Transfer Controller")
public class TransferController {
    @Resource
    ITransferService transferService;

    @GetMapping("/list")
    @ApiOperation(value = "Default Transfer list")
    public AjaxResult<PageResult<TransferDefaultVo>> list(@Validated PageValidate pageValidate, @Validated TransferSearchValidate validate) {
        Integer pageNo = pageValidate.getPageNo();
        Integer pageSize = pageValidate.getPageSize();
        String catchKey = Constants.TRANSFER_PREFIX + pageNo + pageSize;

        if (StringUtils.isNull(validate.getTransactionNumber())
                && StringUtils.isNull(validate.getPayId())&&StringUtils.isNull(validate.getTypeDetail())
                &&StringUtils.isNull(validate.getCurrencyType())&&StringUtils.isNull(validate.getStatus())) {
            if (RedisUtils.exists(catchKey)) {
                PageResult<TransferDefaultVo> list = (PageResult<TransferDefaultVo>) RedisUtils.get(catchKey);
                Long count = transferService.getCount(pageValidate);
                if (!count.equals(list.getCount())) {
                    String commonPrefix = Constants.TRANSFER_PREFIX;
                    Set<String> keysToDelete = RedisUtils.keys(commonPrefix + "*");
                    if (keysToDelete != null && !keysToDelete.isEmpty()) {
                        RedisUtils.delP(keysToDelete.toArray(new String[0]));
                    }
                } else if (pageNo.equals(list.getPageNo()) && pageSize.equals(list.getPageSize())) {
                    return AjaxResult.success(list);
                }
            }
        }
        PageResult<TransferDefaultVo> list = transferService.list(pageValidate, validate);
        if (list.getLists().isEmpty()){
            return AjaxResult.failed(AdminEnum.FAILED.getCode(),Constants.NO_RECORD,list);
        }

        RedisUtils.set(catchKey, list, 604800);
        return AjaxResult.success(list);
    }

    @GetMapping("/detail")
    @ApiOperation(value = "get transfer detail")
    public AjaxResult<TransferDetailVo> detail(@RequestParam String detailId) {
        TransferDetailVo vo = transferService.getDetail(detailId);
        return AjaxResult.success(vo);
    }
}

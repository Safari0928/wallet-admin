package com.mdd.payadmin.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.util.RedisUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.payadmin.commons.Constants;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.enums.AdminEnum;
import com.mdd.payadmin.service.IWithdrawService;
import com.mdd.payadmin.validate.withdraw.ApproveValidate;
import com.mdd.payadmin.validate.withdraw.CancelValidate;
import com.mdd.payadmin.validate.withdraw.WithdrawSearchValidate;
import com.mdd.payadmin.vo.WithdrawDetailVo;
import com.mdd.payadmin.vo.WithdrawVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/withdraw")
@Api(tags = "Admin Withdraw Controller")
public class WithdrawController {

    @Resource
    IWithdrawService iWithdrawService;

    @GetMapping("/list")
    @ApiOperation(value = "All Withdraw Transactions")
    public AjaxResult<PageResult<WithdrawVo>> getAll(@Validated PageValidate pageValidate, @Validated WithdrawSearchValidate searchValidate){

        Integer pageNo = pageValidate.getPageNo();
        Integer pageSize = pageValidate.getPageSize();
        long databaseSize = iWithdrawService.databaseDataSize();
        String key = Constants.WITHDRAW_PREFIX;
        if(StringUtils.isNull(searchValidate.getTransactionNumber())
                &&StringUtils.isNull(searchValidate.getIban())
                &&StringUtils.isNull(searchValidate.getStatus())
                &&StringUtils.isNull(searchValidate.getPayId())){
            if(RedisUtils.exists(key)){
                List<WithdrawVo> allData = (List<WithdrawVo>) RedisUtils.get(key);
                if (databaseSize!=(allData.size())) {
                    RedisUtils.del(key);
                    List<WithdrawVo> allList= iWithdrawService.getAll();
                    RedisUtils.set(key,allList,604800);
                }else{
                    int startIndex = (pageNo - 1) * pageSize;
                    int endIndex = Math.min(startIndex + pageSize, allData.size());
                    if (startIndex >= allData.size()) {
                        return AjaxResult.failed(AdminEnum.FAILED.getCode(), Constants.NO_RECORD, null);
                    }
                    List<WithdrawVo> paginatedData = allData.subList(startIndex, endIndex);
                    long totalRecords = allData.size();
                    PageResult<WithdrawVo> pageResult = new PageResult<>();
                    pageResult.setCount(totalRecords);
                    pageResult.setPageNo(pageNo);
                    pageResult.setPageSize(pageSize);
                    pageResult.setLists(paginatedData);
                    return AjaxResult.success(pageResult);
                }
            }
        }
        PageResult<WithdrawVo> list = iWithdrawService.getDatas(pageValidate,searchValidate);
        if (list.getLists().isEmpty()){
            return AjaxResult.failed(AdminEnum.FAILED.getCode(),Constants.NO_RECORD,list);
        }
        List<WithdrawVo> allList= iWithdrawService.getAll();
        RedisUtils.set(key,allList,604800);
        return AjaxResult.success(list);
    }

    @GetMapping("/detail")
    @ApiOperation(value="Get Detail By Detail Id")
    public AjaxResult<WithdrawDetailVo> getDetail(@RequestParam String detailId){
        WithdrawDetailVo withdrawDetailVo = iWithdrawService.getDetailById(detailId);
        if(withdrawDetailVo == null){
            return AjaxResult.failed(AdminEnum.FAILED.getCode(),AdminEnum.FAILED.getMsg(),null);
        }
        return AjaxResult.success(withdrawDetailVo);
    }

    @PostMapping("/approve")
    @ApiOperation(value = "Get Verify Detail And Approve By Detail Id")
    public AjaxResult makeApprove( @Validated @RequestBody ApproveValidate approveValidate){
        boolean result = iWithdrawService.makeApprove(approveValidate);
        if(!result){
            return AjaxResult.failed(AdminEnum.FAILED.getCode(),AdminEnum.FAILED.getMsg());
        }
        return AjaxResult.success(AdminEnum.SUCCESS.getCode(),AdminEnum.SUCCESS.getMsg());
    }

    @PostMapping("/cancel")
    @ApiOperation(value = "Get Verify Detail And Don't Approve By Detail Id")
    public AjaxResult makeCancel(@Validated @RequestBody CancelValidate cancelValidate){
        boolean result = iWithdrawService.makeCancel(cancelValidate);
        if(!result){
            return AjaxResult.failed(AdminEnum.FAILED.getCode(),AdminEnum.FAILED.getMsg());
        }
        return AjaxResult.success(AdminEnum.SUCCESS.getCode(),AdminEnum.SUCCESS.getMsg());
    }
}
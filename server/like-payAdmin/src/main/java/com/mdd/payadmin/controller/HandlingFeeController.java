package com.mdd.payadmin.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.util.RedisUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.payadmin.commons.Constants;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.enums.AdminEnum;
import com.mdd.payadmin.service.IHandlingFeeService;
import com.mdd.payadmin.validate.handlingfee.HandlingFeeSearchValidate;
import com.mdd.payadmin.validate.withdraw.WithdrawSearchValidate;
import com.mdd.payadmin.vo.HandlingFeeVo;
import com.mdd.payadmin.vo.HandlingFeeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/handlingfee")
@Api(tags="Handling Fee Controller")
public class HandlingFeeController {

    @Resource
    IHandlingFeeService iHandlingFeeService;

    @GetMapping("/list")
    @ApiOperation(value = "All Handling Fee")
    public AjaxResult<PageResult<HandlingFeeVo>> list(@Validated PageValidate pageValidate, @Validated HandlingFeeSearchValidate searchValidate){
        Integer pageNo = pageValidate.getPageNo();
        Integer pageSize = pageValidate.getPageSize();
        long databaseSize = iHandlingFeeService.databaseDataSize();

        if(StringUtils.isNull(searchValidate.getTransactionNumber())
                &&StringUtils.isNull(searchValidate.getPayId())
                &&StringUtils.isNull(searchValidate.getCurrencyType())
                &&StringUtils.isNull(searchValidate.getTypeDetail())){

            String key = Constants.HANDLING_FEE_PREFIX;
            if(RedisUtils.exists(key)){
                List<Object> datas = RedisUtils.lGet(key,0,-1);
                if(databaseSize==datas.size()){
                    int startIndex = (pageNo - 1) * pageSize;
                    int endIndex = Math.min(startIndex + pageSize, datas.size());
                    List<Object> pageData = datas.subList(startIndex, endIndex);
                    List<HandlingFeeVo> handlingFeeData = pageData.stream()
                            .filter(obj -> obj instanceof HandlingFeeVo)
                            .map(obj -> (HandlingFeeVo) obj)
                            .collect(Collectors.toList());

                    PageResult<HandlingFeeVo> pageResult = new PageResult<>();
                    pageResult.setPageNo(pageNo);
                    pageResult.setPageSize(pageSize);
                    pageResult.setLists(handlingFeeData);
                    pageResult.setCount((long) datas.size());
                    return AjaxResult.success(pageResult);
                }else {
                    return getDatasAndAddRedis(pageValidate,searchValidate);
                }
            }
            else {
                return getDatasAndAddRedis(pageValidate,searchValidate);
            }
        }else{
            PageResult<HandlingFeeVo> list = iHandlingFeeService.getAll(pageValidate,searchValidate);
            return AjaxResult.success(list);
        }
    }

    public AjaxResult<PageResult<HandlingFeeVo>> getDatasAndAddRedis(PageValidate pageValidate, HandlingFeeSearchValidate searchValidate){
        String key = Constants.HANDLING_FEE_PREFIX;
        PageResult<HandlingFeeVo> databaseDatas = iHandlingFeeService.getAll(pageValidate, searchValidate);
        List<HandlingFeeVo> ilist= iHandlingFeeService.getList();
        if(ilist!=null && !ilist.isEmpty()){
            List<HandlingFeeVo> handlingFeeDatas = ilist;
            List<Object> redisData = new ArrayList<>();
            redisData.addAll(handlingFeeDatas);
            RedisUtils.lSet(key, redisData);
            RedisUtils.expire(key, 604800L);
        }
        return AjaxResult.success(databaseDatas);
    }
}

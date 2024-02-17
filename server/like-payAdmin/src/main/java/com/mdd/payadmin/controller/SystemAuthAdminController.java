package com.mdd.payadmin.controller;

import com.mdd.common.aop.NotPower;
import com.mdd.common.core.AjaxResult;
import com.mdd.payadmin.LikePayAdminThreadLocal;
import com.mdd.payadmin.enums.StatusEnums;
import com.mdd.payadmin.service.ISystemAuthAdminService;
import com.mdd.payadmin.validate.ProfileValidate;
import com.mdd.payadmin.vo.AdminProfileVo;
import com.mdd.payadmin.vo.SystemAuthAdminSelvesVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/system/admin")
@Api(tags = "系统用户管理")
public class SystemAuthAdminController {

    @Resource
    ISystemAuthAdminService systemAuthAdminService;

    @NotPower
    @GetMapping("/self")
    @ApiOperation(value="管理员信息")
    public AjaxResult<SystemAuthAdminSelvesVo> self() {
        String adminId = LikePayAdminThreadLocal.getAdminId();
        SystemAuthAdminSelvesVo vo = systemAuthAdminService.self(adminId);
        return AjaxResult.success(vo);
    }
    @GetMapping("/profile")
    @ApiOperation(value="管理员信息")
    public AjaxResult<AdminProfileVo> profile(){
        String adminId = LikePayAdminThreadLocal.getAdminId();
        AdminProfileVo vo=systemAuthAdminService.profile(adminId);
        return AjaxResult.success(vo);
    }

    @PostMapping("/change")
    @ApiOperation(value = "Change profile")
    public AjaxResult update(@Validated @RequestBody ProfileValidate validate){

        if(systemAuthAdminService.update(validate)){
            return AjaxResult.success();
        }
        return AjaxResult.failed(StatusEnums.FAIL.getMsg());
    }
}

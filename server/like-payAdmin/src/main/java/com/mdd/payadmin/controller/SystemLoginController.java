package com.mdd.payadmin.controller;

import com.mdd.common.aop.NotLogin;
import com.mdd.common.core.AjaxResult;
import com.mdd.payadmin.service.ISystemLoginService;
import com.mdd.payadmin.validate.SystemLoginValidate;
import com.mdd.payadmin.vo.SystemLoginVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/system")
public class SystemLoginController {
    @Resource
    ISystemLoginService systemLoginService;

    @NotLogin
    @PostMapping("/login")
    public AjaxResult<SystemLoginVo> login(@Validated @RequestBody SystemLoginValidate loginsValidate){
        SystemLoginVo vo = systemLoginService.login(loginsValidate);
        return AjaxResult.success(vo);
    }
    
    @PostMapping("/logout")
    @ApiOperation(value="log out")
    public AjaxResult<Object> logout(HttpServletRequest request) {
        systemLoginService.logout(request.getHeader("token"));
        return AjaxResult.success();
    }
}

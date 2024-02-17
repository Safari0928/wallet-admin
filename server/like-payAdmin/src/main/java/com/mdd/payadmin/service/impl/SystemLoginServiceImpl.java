package com.mdd.payadmin.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.PayAdmin;
import com.mdd.common.exception.LoginException;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.PayAdminMapper;
import com.mdd.common.util.*;
import com.mdd.payadmin.enums.AdminEnum;
import com.mdd.payadmin.service.ISystemLoginService;
import com.mdd.payadmin.validate.SystemLoginValidate;
import com.mdd.payadmin.vo.SystemLoginVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SystemLoginServiceImpl implements ISystemLoginService {

    @Resource
    PayAdminMapper payAdminMapper;

    @Override
    public SystemLoginVo login(SystemLoginValidate loginValidate) {
        String username = loginValidate.getUsername();
        String password = loginValidate.getPassword();

        PayAdmin admin = payAdminMapper.selectOne(new QueryWrapper<PayAdmin>()
                .eq("username", username));
        if (StringUtils.isNull(admin) || admin.getIsDelete().equals(1)){
            throw  new LoginException(AdminEnum.LOGIN_WRONG.getCode(),AdminEnum.LOGIN_WRONG.getMsg());
        }
        if (admin.getIsDisable().equals(1)){
            throw new LoginException(AdminEnum.ID_DISABLE.getCode(),AdminEnum.ID_DISABLE.getMsg());
        }
        String newPWd = password + admin.getSalt();
        String md5Pwd = ToolUtils.makeMd5(newPWd);
        if (!md5Pwd.equals(admin.getPassword())) {
            throw new LoginException(AdminEnum.WRONG_PASSWORD.getCode(),AdminEnum.WRONG_PASSWORD.getMsg());
        }
        try {
            StpUtil.login(admin.getUuid());
            admin.setLastLoginIp(IpUtils.getIpAddress());
            admin.setLastLoginTime(PayUtils.createDateTime());
            payAdminMapper.updateById(admin);

            SystemLoginVo vo = new SystemLoginVo();
            vo.setUuid(admin.getUuid());
            vo.setToken(StpUtil.getTokenValue());
            return vo;

        }catch (Exception e){
            throw new OperateException(e.getMessage());
        }
    }

    @Override
    public void logout(String token) {
        StpUtil.logout();
    }
}

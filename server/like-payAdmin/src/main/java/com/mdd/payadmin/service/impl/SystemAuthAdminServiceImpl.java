package com.mdd.payadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.PayAdmin;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.exception.LoginException;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.PayAdminMapper;
import com.mdd.common.util.*;
import com.mdd.payadmin.LikePayAdminThreadLocal;
import com.mdd.payadmin.commons.Constants;
import com.mdd.payadmin.enums.AdminEnum;
import com.mdd.payadmin.service.ISystemAuthAdminService;
import com.mdd.payadmin.validate.ProfileValidate;
import com.mdd.payadmin.vo.AdminProfileVo;
import com.mdd.payadmin.vo.SystemAuthAdminInformVo;
import com.mdd.payadmin.vo.SystemAuthAdminSelvesVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Service
public class SystemAuthAdminServiceImpl implements ISystemAuthAdminService {
    @Resource
    PayAdminMapper adminMapper;

    @Override
    public SystemAuthAdminSelvesVo self(String adminId) {

        PayAdmin admin = adminMapper.selectOne(new QueryWrapper<PayAdmin>()
                .select(PayAdmin.class, info ->
                        !info.getColumn().equals("salt") &&
                                !info.getColumn().equals("password") &&
                                !info.getColumn().equals("is_delete") &&
                                !info.getColumn().equals("delete_time"))
                .eq("is_delete", 0)
                .eq(Constants.UUID, adminId)
                .last("limit 1"));

        SystemAuthAdminInformVo systemAuthAdminInformVo = new SystemAuthAdminInformVo();
        BeanUtils.copyProperties(admin, systemAuthAdminInformVo);
        systemAuthAdminInformVo.setUpdateTime(admin.getUpdateTime().replace(".0", ""));
        systemAuthAdminInformVo.setCreateTime(admin.getCreateTime().replace(".0", ""));
        systemAuthAdminInformVo.setLastLoginTime(admin.getLastLoginTime().replace(".0", ""));

        List<String> auths = new LinkedList<>();
        // 所有权限
        auths.add("*");

        SystemAuthAdminSelvesVo vo = new SystemAuthAdminSelvesVo();
        vo.setUser(systemAuthAdminInformVo);
        vo.setPermissions(auths);
        return vo;
    }

    @Override
    public AdminProfileVo profile(String adminId) {
        PayAdmin admin = adminMapper.selectOne(new QueryWrapper<PayAdmin>()
                .select("uuid", "nickname", "username", "role_ids")
                .eq("is_delete", 0)
                .eq(Constants.UUID, adminId)
                .last("limit 1"));
        AdminProfileVo vo = new AdminProfileVo();
        BeanUtils.copyProperties(admin, vo);
        return vo;
    }

    @Override
    public boolean update(ProfileValidate validate) {
        String adminId = LikePayAdminThreadLocal.getAdminId();
        PayAdmin admin = adminMapper.selectOne(new QueryWrapper<PayAdmin>()
                .eq(Constants.UUID, adminId));
        if (StringUtils.isNotNull(validate.getNickname())) {
            admin.setNickname(validate.getNickname());
            admin.setUpdateTime(PayUtils.createDateTime());
        }
        if (StringUtils.isNotNull(validate.getUsername())) {
            admin.setUsername(validate.getUsername());
            admin.setUpdateTime(PayUtils.createDateTime());
        }
        if (StringUtils.isNotNull(validate.getOldPassword()) && StringUtils.isNotNull(validate.getNewPassword())) {
            String oldPwd = validate.getOldPassword() + admin.getSalt();
            String md5oldPwd = ToolUtils.makeMd5(oldPwd);
            if (!md5oldPwd.equals(admin.getPassword())) {
                throw new LoginException(AdminEnum.WRONG_PASSWORD.getCode(), AdminEnum.WRONG_PASSWORD.getMsg());
            }
            String newPwd = validate.getNewPassword() + admin.getSalt();
            String md5newPwd = ToolUtils.makeMd5(newPwd);
            admin.setPassword(md5newPwd);
        }
        int result = adminMapper.updateById(admin);
        if (result < 1) {
            throw new OperateException(ErrorEnum.ASSERT_MYBATIS_ERROR.getMsg(), ErrorEnum.ASSERT_MYBATIS_ERROR.getCode());
        }
        return true;
    }
}

package com.mdd.payadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.mdd.common.entity.PayAdmin;
import com.mdd.common.entity.payLimits.PayDepositConfigure;
import com.mdd.common.exception.LoginException;
import com.mdd.common.mapper.PayAdminMapper;
import com.mdd.common.mapper.payLimits.PayDepositLimitsMapper;
import com.mdd.common.util.PayUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.payadmin.LikePayAdminThreadLocal;
import com.mdd.payadmin.commons.Constants;
import com.mdd.payadmin.enums.AdminEnum;
import com.mdd.payadmin.service.IDepositConfigureService;
import com.mdd.payadmin.validate.configure.UpdateDepositConfigureValidate;
import com.mdd.payadmin.vo.configure.DepositConfigureListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DepositConfigureServiceImpl implements IDepositConfigureService {

    @Resource
    PayDepositLimitsMapper payDepositLimitsMapper;

    @Resource
    PayAdminMapper payAdminMapper;

    @Override
    public DepositConfigureListVo list() {

        PayDepositConfigure depositConfigure = payDepositLimitsMapper.selectOne(new QueryWrapper<PayDepositConfigure>()
                .eq("is_active", 1)
        );
        Assert.notNull(depositConfigure, "Deposit Configure Data does not exist!");
        DepositConfigureListVo vo = new DepositConfigureListVo();
        BeanUtils.copyProperties(depositConfigure, vo);

        return vo;
    }

    @Override
    public boolean update(UpdateDepositConfigureValidate configureValidate) {
        checkPassword(configureValidate.getPassword());

        PayDepositConfigure depositConfigure = payDepositLimitsMapper.selectOne(new QueryWrapper<PayDepositConfigure>()
                .eq(Constants.UUID, configureValidate.getUuid()));
        Assert.notNull(depositConfigure, "Deposit  Configure does not exist!");

        if (StringUtils.isNotNull(configureValidate.getPayCommission())) {
            depositConfigure.setPayCommission(configureValidate.getPayCommission());
            depositConfigure.setCommission(depositConfigure.getChannelCommission()+ configureValidate.getPayCommission());
            depositConfigure.setUpdateTime(PayUtils.createDateTime());
        }

        if (StringUtils.isNotNull(configureValidate.getVerifiedUserFee())) {
            depositConfigure.setVerifiedUserFee(configureValidate.getVerifiedUserFee());
            depositConfigure.setUpdateTime(PayUtils.createDateTime());
        }
        if (StringUtils.isNotNull(configureValidate.getUnverifiedUserFee())) {
            depositConfigure.setUnverifiedUserFee(configureValidate.getUnverifiedUserFee());
            depositConfigure.setUpdateTime(PayUtils.createDateTime());
        }

        int result = payDepositLimitsMapper.updateById(depositConfigure);
        if (result != 1) {
            return false;
        }
        return true;
    }

    private void checkPassword(String password){
        String username = LikePayAdminThreadLocal.getUsername();
        PayAdmin admin = payAdminMapper.selectOne(new QueryWrapper<PayAdmin>()
                .eq(Constants.USERNAME, username));
        String newPWd = password + admin.getSalt();
        String md5Pwd = ToolUtils.makeMd5(newPWd);
        if (!md5Pwd.equals(admin.getPassword())) {
            throw new LoginException(AdminEnum.WRONG_PASSWORD.getCode(),AdminEnum.WRONG_PASSWORD.getMsg());
        }
    }

}


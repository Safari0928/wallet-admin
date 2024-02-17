package com.mdd.payadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mdd.common.entity.PayAdmin;
import com.mdd.common.entity.payLimits.PayTransferConfigure;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.exception.LoginException;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.PayAdminMapper;
import com.mdd.common.mapper.payLimits.PayTransferLimitsMapper;
import com.mdd.common.util.PayUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.payadmin.LikePayAdminThreadLocal;
import com.mdd.payadmin.commons.Constants;
import com.mdd.payadmin.commons.MappingUtils;
import com.mdd.payadmin.enums.AdminEnum;
import com.mdd.payadmin.service.ITransferConfigureService;
import com.mdd.payadmin.validate.transfer.*;
import com.mdd.payadmin.vo.TransferConfigureVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

@Service
public class TransferConfigureServiceImpl implements ITransferConfigureService {

    @Resource
    PayTransferLimitsMapper transferLimitsMapper;

    @Resource
    PayAdminMapper payAdminMapper;
    @Override
    public TransferConfigureVo defaultPage(Integer currency) {

        PayTransferConfigure payTransferConfigure = transferLimitsMapper.selectOne(new QueryWrapper<PayTransferConfigure>()
                .eq("currency_id", currency));
        Assert.notNull(payTransferConfigure, Constants.NO_RECORD);
        TransferConfigureVo vo = new TransferConfigureVo();
        BeanUtils.copyProperties(payTransferConfigure,vo);
        vo.setCurrency(MappingUtils.mapCurrency(payTransferConfigure.getCurrencyId()));
        return vo;
    }

    @Override
    public Boolean update(TransferConfigureValidate validate) {
        checkPassword(validate.getPassword());
        PayTransferConfigure configure = transferLimitsMapper.selectOne(new QueryWrapper<PayTransferConfigure>()
                .eq(Constants.UUID, validate.getEditUuid()));
        if (StringUtils.isNotNull(validate.getPayCommission())){
            configure.setPayCommission(validate.getPayCommission());
            configure.setCommission(configure.getChannelCommission()+ validate.getPayCommission());
            configure.setUpdateTime(PayUtils.createDateTime());
        }
        if (StringUtils.isNotNull(validate.getUnverifiedUserFee())){
            configure.setUnverifiedUserFee(validate.getUnverifiedUserFee());
            configure.setUpdateTime(PayUtils.createDateTime());
        }
        if (StringUtils.isNotNull(validate.getVerifiedUserFee())){
            configure.setVerifiedUserFee(validate.getVerifiedUserFee());
            configure.setUpdateTime(PayUtils.createDateTime());
        }
        if (StringUtils.isNotNull(validate.getCurrentStatus())){
            configure.setIsActive(validate.getCurrentStatus());
            configure.setUpdateTime(PayUtils.createDateTime());
        }
        int result = transferLimitsMapper.updateById(configure);
        if (result<1){
            throw new OperateException(ErrorEnum.ASSERT_MYBATIS_ERROR.getMsg(), ErrorEnum.ASSERT_MYBATIS_ERROR.getCode());
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

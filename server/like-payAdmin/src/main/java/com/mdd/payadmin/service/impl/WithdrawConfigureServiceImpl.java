package com.mdd.payadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.mdd.common.entity.PayAdmin;
import com.mdd.common.entity.payLimits.PayWithdrawalConfigure;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.exception.LoginException;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.PayAdminMapper;
import com.mdd.common.mapper.payLimits.PayWithdrawalLimitsMapper;
import com.mdd.common.util.PayUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.payadmin.LikePayAdminThreadLocal;
import com.mdd.payadmin.commons.Constants;
import com.mdd.payadmin.commons.MappingUtils;
import com.mdd.payadmin.enums.AdminEnum;
import com.mdd.payadmin.service.IWithdrawConfigureService;
import com.mdd.payadmin.vo.WithdrawConfigureVo;
import org.springframework.beans.BeanUtils;
import com.mdd.payadmin.validate.withdraw.UpdateWithdrawConfigureValidate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class WithdrawConfigureServiceImpl implements IWithdrawConfigureService {

    @Resource
    PayWithdrawalLimitsMapper payWithdrawalLimitsMapper;

    @Resource
    PayAdminMapper payAdminMapper;


    private PayAdmin admin(){
        String adminId = LikePayAdminThreadLocal.getAdminId();
        PayAdmin admin = payAdminMapper.selectOne(new QueryWrapper<PayAdmin>()
                .eq(Constants.UUID, adminId));
        if (StringUtils.isNull(admin) || admin.getIsDelete().equals(1)) {
            throw new LoginException(AdminEnum.LOGIN_WRONG.getCode(), AdminEnum.LOGIN_WRONG.getMsg());
        }
        if (admin.getIsDisable().equals(1)) {
            throw new LoginException(AdminEnum.ID_DISABLE.getCode(), AdminEnum.ID_DISABLE.getMsg());
        }
        return admin;
    }

    private void checkPassword(String password){
        PayAdmin admin = admin();
        String newPWd = password + admin.getSalt();
        String md5Pwd = ToolUtils.makeMd5(newPWd);
        if (!md5Pwd.equals(admin.getPassword())) {
            throw new LoginException(AdminEnum.LOGIN_WRONG.getCode(), AdminEnum.LOGIN_WRONG.getMsg());
        }
    }

    @Override
    public boolean update(UpdateWithdrawConfigureValidate validate){
        checkPassword(validate.getPassword());
        PayWithdrawalConfigure payWithdrawalConfigure = payWithdrawalLimitsMapper.selectOne(new QueryWrapper<PayWithdrawalConfigure>()
               .eq(Constants.UUID, validate.getUuid()));
        Assert.notNull(payWithdrawalConfigure,AdminEnum.REQUEST_404_ERROR.getMsg());
        if (StringUtils.isNotNull(validate.getPayCommission())){
            payWithdrawalConfigure.setPayCommission(validate.getPayCommission());
            payWithdrawalConfigure.setCommission(payWithdrawalConfigure.getChannelCommission()+ validate.getPayCommission());
            payWithdrawalConfigure.setUpdateTime(PayUtils.createDateTime());
        }
        if (StringUtils.isNotNull(validate.getUnverifiedUserFee())){
            payWithdrawalConfigure.setUnverifiedUserFee(validate.getUnverifiedUserFee());
            payWithdrawalConfigure.setUpdateTime(PayUtils.createDateTime());
        }
        if (StringUtils.isNotNull(validate.getVerifiedUserFee())){
            payWithdrawalConfigure.setVerifiedUserFee(validate.getVerifiedUserFee());
            payWithdrawalConfigure.setUpdateTime(PayUtils.createDateTime());
        }
        if(StringUtils.isNotNull(validate.getAmountAudited())){
            payWithdrawalConfigure.setAmountAudited(validate.getAmountAudited());
            payWithdrawalConfigure.setUpdateTime(PayUtils.createDateTime());
        }
        int result = payWithdrawalLimitsMapper.updateById(payWithdrawalConfigure);
        if(result<1){
            throw new OperateException(ErrorEnum.ASSERT_MYBATIS_ERROR.getMsg(), ErrorEnum.ASSERT_MYBATIS_ERROR.getCode());
        }
        return true;
    }

    @Override
    public WithdrawConfigureVo list(){
        PayWithdrawalConfigure payWithdrawalConfigure = payWithdrawalLimitsMapper.selectOne(new QueryWrapper<PayWithdrawalConfigure>()
                .eq("is_active", 1));
        Assert.notNull(payWithdrawalConfigure,Constants.NO_RECORD);

        WithdrawConfigureVo withdrawConfigureVo = new WithdrawConfigureVo();
        BeanUtils.copyProperties(payWithdrawalConfigure,withdrawConfigureVo);
        withdrawConfigureVo.setCurrencyId(1);
        withdrawConfigureVo.setCurrency(MappingUtils.mapCurrency(1));
        withdrawConfigureVo.setAmountAudited(payWithdrawalConfigure.getAmountAudited());
        return withdrawConfigureVo;
    }
}

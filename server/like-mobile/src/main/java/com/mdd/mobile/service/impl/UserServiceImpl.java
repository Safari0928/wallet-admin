package com.mdd.mobile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.mdd.common.entity.payAcoounts.PayUserAccount;
import com.mdd.common.entity.payBankCard.PayBankCards;
import com.mdd.common.entity.payDeposit.PayDeposit;
import com.mdd.common.entity.payLimits.PayDepositConfigure;
import com.mdd.common.entity.payLimits.PayTransferConfigure;
import com.mdd.common.entity.payLimits.PayWithdrawalConfigure;
import com.mdd.common.entity.payUser.PayUser;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.payAccount.PayUserAccountMapper;
import com.mdd.common.mapper.payLimits.PayDepositLimitsMapper;
import com.mdd.common.mapper.payLimits.PayTransferLimitsMapper;
import com.mdd.common.mapper.payLimits.PayWithdrawalLimitsMapper;
import com.mdd.common.mapper.payUser.PayUserMapper;
import com.mdd.common.util.ToolUtils;
import com.mdd.mobile.enums.PayError;
import com.mdd.mobile.enums.StatusEnums;
import com.mdd.mobile.enums.TypeDetailEnums;
import com.mdd.mobile.service.IUserService;
import com.mdd.mobile.validate.common.Constants;
import com.mdd.mobile.validate.user.MailValidate;
import com.mdd.mobile.validate.user.NickNameValidate;
import com.mdd.mobile.vo.user.ChangeMailVo;
import com.mdd.mobile.vo.user.UserInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Resource
    PayUserMapper payUserMapper;
    @Resource
    PayDepositLimitsMapper payDepositLimitsMapper;

    @Resource
    PayWithdrawalLimitsMapper payWithdrawalLimitsMapper;

    @Resource
    PayTransferLimitsMapper payTransferLimitsMapper;
    @Resource
    PayUserAccountMapper payUserAccountMapper;

    @Override
    public UserInfoVo info(String userId) {
        int currencyId=1;
        PayUser user = payUserMapper.selectOne(new QueryWrapper<PayUser>()
                .eq(Constants.UUID, userId)
                .eq("status", StatusEnums.NOT_DELETE.getCode())
        );
        Assert.notNull(user, PayError.USER_NOT_FOUND.getMsg());

        PayDepositConfigure depositLimits = payDepositLimitsMapper.selectOne(
                new QueryWrapper<PayDepositConfigure>()
                        .eq("is_active", StatusEnums.NOT_DELETE.getCode())
        );

        Assert.notNull(depositLimits, "Limit is not found for deposit, please try agan");

        PayWithdrawalConfigure withdrawalLimits = payWithdrawalLimitsMapper.selectOne(
                new QueryWrapper<PayWithdrawalConfigure>()
                        .eq("is_active", StatusEnums.NOT_DELETE.getCode())
        );

        Assert.notNull(withdrawalLimits, "Limit is not found please for withdraw, try agan");

        PayTransferConfigure transferConfigure = payTransferLimitsMapper.selectOne(
                new QueryWrapper<PayTransferConfigure>()
                        .eq("is_active", StatusEnums.NOT_DELETE.getCode())
                        .eq("currency_id",currencyId)
        );
        Assert.notNull(transferConfigure, "Limit is not found please for Transfer, try agan");

        PayUserAccount payUserAccount = payUserAccountMapper.selectOne(
                new QueryWrapper<PayUserAccount>()
                        .eq(Constants.USERID, userId)
                        .eq("currency_type",currencyId)
        );

        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(user, userInfoVo); //TODO Add verifyAccount message and account type
        if (user.getVerifyAccount().equals(1)){

            userInfoVo.setDepositLimit(depositLimits.getVerifiedUserFee());
            userInfoVo.setUsedDepositLimit(payUserAccount.getDepositLimit());
            userInfoVo.setWithdrawalLimit(withdrawalLimits.getVerifiedUserFee());
            userInfoVo.setUsedWithdrawalLimit(payUserAccount.getWithdrawalLimit());
            userInfoVo.setTransferLimit(transferConfigure.getVerifiedUserFee());
            userInfoVo.setUsedTransferLimit(payUserAccount.getTransferLimit());
        }else {
            userInfoVo.setDepositLimit(depositLimits.getUnverifiedUserFee());
            userInfoVo.setUsedDepositLimit(payUserAccount.getDepositLimit());
            userInfoVo.setWithdrawalLimit(withdrawalLimits.getUnverifiedUserFee());
            userInfoVo.setUsedWithdrawalLimit(payUserAccount.getWithdrawalLimit());
            userInfoVo.setTransferLimit(transferConfigure.getUnverifiedUserFee());
            userInfoVo.setUsedTransferLimit(payUserAccount.getTransferLimit());
        }
        return userInfoVo;
    }

    @Override
    public boolean changeNickName(String userId, NickNameValidate nickNameValidate){
        PayUser payUser = payUserMapper.selectOne(new QueryWrapper<PayUser>().eq(Constants.UUID,userId));
        Assert.notNull(payUser, PayError.USER_NOT_FOUND.getMsg());

        payUser.setNickName(nickNameValidate.getNickName());
        int update = payUserMapper.updateById(payUser);
        if(update>0){
            return true;
        }else{
            int code = PayError.FAILED.getCode();
            String msg = PayError.FAILED.getMsg();
            throw new OperateException(msg,code);
        }
    }

    @Override
    public boolean changeAvatar(String userId, String avatar) {

        PayUser user = payUserMapper.selectOne(new QueryWrapper<PayUser>()
                        .select("id,avatar,uuid")
                .eq(Constants.UUID,userId));

        Assert.notNull(user, PayError.USER_NOT_FOUND.getMsg());
        user.setAvatar(avatar);
        int update = payUserMapper.updateById(user);

        if(update>0){
            return true;
        }else{
            int code = PayError.FAILED.getCode();
            String msg = PayError.FAILED.getMsg();
            throw new OperateException(msg,code);
        }
    }

    @Override
    public boolean changePassword(String userId, String oldPassword, String newPassword, String newPasswordAgain){
        PayUser payUser = payUserMapper.selectOne(new QueryWrapper<PayUser>()
                .eq(Constants.UUID,userId));
        Assert.notNull(payUser, "Account does not exist!");

        String oldPwd =  ToolUtils.makeMd5(oldPassword+payUser.getSalt());
        if(!oldPwd.equals(payUser.getPassword())){
            int code = PayError.PASSWORD_ERROR.getCode();
            String msg = PayError.PASSWORD_ERROR.getMsg();
            throw new OperateException(msg,code);
        }else if(!newPassword.equals(newPasswordAgain)){
            int code = PayError.PASSWORD_NOT_MATCHED.getCode();
            String msg = PayError.PASSWORD_NOT_MATCHED.getMsg();
            throw new OperateException(msg,code);
        }else{
            String newPwd = ToolUtils.makeMd5(newPassword+payUser.getSalt());
            payUser.setPassword(newPwd);
            int change = payUserMapper.updateById(payUser);
            if(change>0){
                return true;
            }else{
                int code = PayError.FAILED.getCode();
                String msg = PayError.FAILED.getMsg();
                throw new OperateException(msg,code);
            }
        }
    }

    @Override
    public ChangeMailVo changeMail (String userId,MailValidate mailValidate){
        PayUser payUser = payUserMapper.selectOne(new QueryWrapper<PayUser>()
                .eq(Constants.UUID,userId));
        Assert.notNull(payUser, "Account does not exist!");

        if(!payUser.getEmail().equals(mailValidate.getOldMail())){
            int code= PayError.MAIL_NOT_MATCHED.getCode();
            String msg = PayError.MAIL_NOT_MATCHED.getMsg();
            throw new OperateException(msg,code);
        }
        ChangeMailVo changeMailVo = new ChangeMailVo();
        BeanUtils.copyProperties(payUser,changeMailVo);
        payUser.setEmail(mailValidate.getNewMail());
        int result = payUserMapper.updateById(payUser);
        if(result>0){
            changeMailVo.setNewMail(mailValidate.getNewMail());
            changeMailVo.setStatus(StatusEnums.SUCCESS.getMsg());
            return changeMailVo;
        }else{
            int code= PayError.ASSERT_MYBATIS_ERROR.getCode();
            String msg = PayError.ASSERT_MYBATIS_ERROR.getMsg();
            throw new OperateException(msg,code);
        }
    }

    @Override
    public boolean changePhone(String userId, String newPhone, String phone) {
        PayUser user = payUserMapper.selectOne(new QueryWrapper<PayUser>()
                .select("id,phone_number,uuid")
                .eq(Constants.UUID,userId)
                .eq("phone_number",phone));

        Assert.notNull(user, PayError.USER_NOT_FOUND.getMsg());

        user.setPhoneNumber(newPhone);
        int update = payUserMapper.updateById(user);

        if(update!=1) return false;

        return true;
    }

}

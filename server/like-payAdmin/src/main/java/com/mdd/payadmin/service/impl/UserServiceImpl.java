package com.mdd.payadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.payAcoounts.PayUserAccount;
import com.mdd.common.entity.payLimits.PayDepositConfigure;
import com.mdd.common.entity.payLimits.PayTransferConfigure;
import com.mdd.common.entity.payLimits.PayWithdrawalConfigure;
import com.mdd.common.entity.payUser.PayUser;
import com.mdd.common.enums.CurrencyEnum;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.payAccount.PayUserAccountMapper;
import com.mdd.common.mapper.payLimits.PayDepositLimitsMapper;
import com.mdd.common.mapper.payLimits.PayTransferLimitsMapper;
import com.mdd.common.mapper.payLimits.PayWithdrawalLimitsMapper;
import com.mdd.common.mapper.payUser.PayUserMapper;
import com.mdd.payadmin.commons.Constants;
import com.mdd.payadmin.commons.MappingUtils;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.enums.StatusEnums;
import com.mdd.payadmin.enums.UserVerifyEnum;
import com.mdd.payadmin.service.IUserService;
import com.mdd.payadmin.validate.user.UserListValidate;
import com.mdd.payadmin.validate.user.UserStatusValidate;
import com.mdd.payadmin.vo.user.UserDetailVo;
import com.mdd.payadmin.vo.user.UserListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Resource
    PayUserMapper payUserMapper;
    @Resource
    PayUserAccountMapper payUserAccountMapper;
    @Resource
    PayWithdrawalLimitsMapper payWithdrawalLimitsMapper;
    @Resource
    PayDepositLimitsMapper payDepositLimitsMapper;
    @Resource
    PayTransferLimitsMapper payTransferLimitsMapper;

    @Override
    public PageResult<UserListVo> userList(PageValidate pageValidate, UserListValidate userListValidate) {

        Integer page = pageValidate.getPageNo();
        Integer size = pageValidate.getPageSize();
        QueryWrapper<PayUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("verify_account = 2");
        queryWrapper.orderByAsc("CASE WHEN verify_account = 2 THEN create_time END");
        queryWrapper.orderByDesc("CASE WHEN verify_account != 2 THEN update_time END");

        payUserMapper.setSearch(queryWrapper, userListValidate, new String[]{
                "=: phoneNumber@phone_number:str",
                "=: country@country:str",
                "=:payId@pay_id:str",
                "=:status@status:int",
                "=:email@email:str",
                "=:verifyAccount@verify_account:int"
        });
        IPage<PayUser> iPage = payUserMapper.selectPage(new Page<>(page, size), queryWrapper);

        List<UserListVo> list = new ArrayList<>();
        for (PayUser user : iPage.getRecords()) {
            UserListVo vo = new UserListVo();
            BeanUtils.copyProperties(user, vo);
            vo.setCreateTime(user.getCreateTime().replace(".0", ""));
            vo.setStatusMSG(MappingUtils.mapUserStatus(user.getStatus()));
            vo.setVerifyAccountMSG(MappingUtils.mapUserVerify(user.getVerifyAccount()));
            list.add(vo);
        }
        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list);
    }

    @Override
    public UserDetailVo detail(String uuid) {

        PayUser payUser = payUserMapper.selectOne(new QueryWrapper<PayUser>()
                .eq(Constants.UUID, uuid));
        UserDetailVo vo = new UserDetailVo();
        BeanUtils.copyProperties(payUser, vo);
        vo.setStatusMSG(MappingUtils.mapUserStatus(payUser.getStatus()));
        vo.setVerifyAccountMSG(MappingUtils.mapUserVerify(payUser.getVerifyAccount()));
        vo.setCreateTime(payUser.getCreateTime().replace(".0", ""));

        List<PayUserAccount> userAccount = payUserAccountMapper.selectList(new QueryWrapper<PayUserAccount>()
                .eq(Constants.USERID, uuid));
        for (PayUserAccount account : userAccount) {
            if (account.getCurrencyType() == CurrencyEnum.TRY.getCode()) {
                vo.setBalanceForTry(account.getBalance());
                vo.setCurrencyTRY(MappingUtils.mapCurrency(account.getCurrencyType()));
            } else if (account.getCurrencyType() == CurrencyEnum.USD.getCode()) {
                vo.setBalanceForUsd(account.getBalance());
                vo.setCurrencyUSD(MappingUtils.mapCurrency(account.getCurrencyType()));
            } else if (account.getCurrencyType() == CurrencyEnum.EUR.getCode()) {
                vo.setBalanceForEur(account.getBalance());
                vo.setCurrencyEUR(MappingUtils.mapCurrency(account.getCurrencyType()));
            }
        }
        return vo;
    }

    @Override
    public boolean updateUserStatus(UserStatusValidate validate) {
        PayUser change = payUserMapper.selectOne(new QueryWrapper<PayUser>()
                .eq(Constants.UUID, validate.getUserId()));
        if (change.getStatus() == StatusEnums.FAIL.getCode()) {
            change.setStatus(StatusEnums.SUCCESS.getCode());
        } else if (change.getStatus() == StatusEnums.SUCCESS.getCode()) {
            change.setStatus(StatusEnums.FAIL.getCode());
        }
        int result = payUserMapper.updateById(change);
        if (result < 1) {
            throw new OperateException(ErrorEnum.ASSERT_MYBATIS_ERROR.getMsg(), ErrorEnum.ASSERT_MYBATIS_ERROR.getCode());
        }
        return true;
    }

    @Override
    public boolean updateUserVerify(UserStatusValidate validate) {
        Integer status = validate.getStatus();
        if (status == 1) {
            UpdateWrapper<PayUser> userVerify = new UpdateWrapper<>();
            userVerify.eq(Constants.UUID, validate.getUserId());
            userVerify.set("verify_account", UserVerifyEnum.VERIFIED.getCode());
            int update = payUserMapper.update(null, userVerify);
            if (update < 1) {
                throw new OperateException(ErrorEnum.ASSERT_MYBATIS_ERROR.getMsg(), ErrorEnum.ASSERT_MYBATIS_ERROR.getCode());
            }
            UpdateWrapper<PayUserAccount> accountVerify = new UpdateWrapper<>();
            accountVerify.eq(Constants.USERID, validate.getUserId());
            accountVerify.set("verify_account", UserVerifyEnum.VERIFIED.getCode());
            int updateVerify = payUserAccountMapper.update(null, accountVerify);
            if (updateVerify < 1) {
                throw new OperateException(ErrorEnum.ASSERT_MYBATIS_ERROR.getMsg(), ErrorEnum.ASSERT_MYBATIS_ERROR.getCode());
            }
            PayDepositConfigure tryDepositConfig = payDepositLimitsMapper.selectOne(new QueryWrapper<PayDepositConfigure>()
                    .select("verified_user_fee"));
            if (tryDepositConfig != null) {
                BigDecimal verifiedUserFeeTry = tryDepositConfig.getVerifiedUserFee();
                UpdateWrapper<PayUserAccount> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq(Constants.CURRENCY_TYPE, CurrencyEnum.TRY.getCode())
                        .set("deposit_limit", verifiedUserFeeTry);
                int updateDepositLimit = payUserAccountMapper.update(null, updateWrapper);
                if (updateDepositLimit < 1) {
                    throw new OperateException(ErrorEnum.ASSERT_MYBATIS_ERROR.getMsg(), ErrorEnum.ASSERT_MYBATIS_ERROR.getCode());
                }

                PayWithdrawalConfigure tryWithdrawalConfig = payWithdrawalLimitsMapper.selectOne(new QueryWrapper<PayWithdrawalConfigure>()
                        .select("verified_user_fee"));
                if (tryWithdrawalConfig != null) {
                    BigDecimal verifiedUserFee = tryWithdrawalConfig.getVerifiedUserFee();
                    UpdateWrapper<PayUserAccount> updateWithdrawalLimit = new UpdateWrapper<>();
                    updateWithdrawalLimit.eq(Constants.CURRENCY_TYPE, CurrencyEnum.TRY.getCode())
                            .set("withdrawal_limit", verifiedUserFee);
                    int updateUserWithdrawalLimit = payUserAccountMapper.update(null, updateWithdrawalLimit);
                    if (updateUserWithdrawalLimit < 1) {
                        throw new OperateException(ErrorEnum.ASSERT_MYBATIS_ERROR.getMsg(), ErrorEnum.ASSERT_MYBATIS_ERROR.getCode());
                    }

                    List<PayTransferConfigure> payTransferConfigures = payTransferLimitsMapper.selectList(new QueryWrapper<PayTransferConfigure>()
                            .select("verified_user_fee", "currency_id"));
                    for (PayTransferConfigure transferConfigure : payTransferConfigures) {
                        BigDecimal verifiedUser = transferConfigure.getVerifiedUserFee();
                        Integer currencyId = transferConfigure.getCurrencyId();
                        UpdateWrapper<PayUserAccount> updateTransferLimit = new UpdateWrapper<>();
                        updateTransferLimit.eq("currency_type", currencyId);
                        updateTransferLimit.set("transfer_limit", verifiedUser);
                        int updateUserTransferLimit = payUserAccountMapper.update(null, updateTransferLimit);
                        if (updateUserTransferLimit < 1) {
                            throw new OperateException(ErrorEnum.ASSERT_MYBATIS_ERROR.getMsg(), ErrorEnum.ASSERT_MYBATIS_ERROR.getCode());
                        }
                    }
                }
            }
            return true;

        } else if (status == -1) {
            UpdateWrapper<PayUser> userVerify = new UpdateWrapper<>();
            userVerify.eq(Constants.UUID, validate.getUserId());
            userVerify.set("verify_account", UserVerifyEnum.UNVERIFIED.getCode());
            int update = payUserMapper.update(null, userVerify);
            if (update < 1) {
                throw new OperateException(ErrorEnum.ASSERT_MYBATIS_ERROR.getMsg(), ErrorEnum.ASSERT_MYBATIS_ERROR.getCode());
            }
            UpdateWrapper<PayUserAccount> accountVerify = new UpdateWrapper<>();
            accountVerify.eq(Constants.USERID, validate.getUserId());
            accountVerify.set("verify_account", UserVerifyEnum.UNVERIFIED.getCode());
            int updateVerify = payUserAccountMapper.update(null, accountVerify);
            if (updateVerify < 1) {
                throw new OperateException(ErrorEnum.ASSERT_MYBATIS_ERROR.getMsg(), ErrorEnum.ASSERT_MYBATIS_ERROR.getCode());
            }
            return true;
        }

        return false;
    }

    @Override
    public List<Object> getCountry() {
        QueryWrapper<PayUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT country");
        List<Object> countryList = payUserMapper.selectObjs(queryWrapper);
        return countryList;

    }

}

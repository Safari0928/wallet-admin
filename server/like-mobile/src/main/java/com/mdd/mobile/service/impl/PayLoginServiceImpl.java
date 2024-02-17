package com.mdd.mobile.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.mdd.common.entity.payAcoounts.PayUserAccount;
import com.mdd.common.entity.payLimits.PayDepositConfigure;
import com.mdd.common.entity.payLimits.PayTransferConfigure;
import com.mdd.common.entity.payLimits.PayWithdrawalConfigure;
import com.mdd.common.entity.payUser.PayUser;
import com.mdd.common.enums.CurrencyEnum;
import com.mdd.common.mapper.payAccount.PayUserAccountMapper;
import com.mdd.common.mapper.payLimits.PayDepositLimitsMapper;
import com.mdd.common.mapper.payLimits.PayTransferLimitsMapper;
import com.mdd.common.mapper.payLimits.PayWithdrawalLimitsMapper;
import com.mdd.common.mapper.payUser.PayUserMapper;
import com.mdd.common.util.IpUtils;
import com.mdd.common.util.PayUtils;
import com.mdd.common.util.RedisUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.mobile.enums.StatusEnums;
import com.mdd.mobile.service.IRedisPanelDataService;
import com.mdd.mobile.validate.common.Constants;
import com.mdd.mobile.validate.login.PayRegisterValidate;
import com.mdd.mobile.vo.payLogin.PayLoginVo;
import lombok.extern.slf4j.Slf4j;
import com.mdd.mobile.service.IPayLoginService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class PayLoginServiceImpl implements IPayLoginService {

    @Resource
    PayUserMapper payUserMapper;

    @Resource
    PayUserAccountMapper payUserAccountMapper;

    @Resource
    PayDepositLimitsMapper payDepositLimitsMapper;

    @Resource
    PayWithdrawalLimitsMapper payWithdrawalLimitsMapper;

    @Resource
    PayTransferLimitsMapper payTransferLimitsMapper;

    @Resource
    IRedisPanelDataService iRedisPanelDataService;

    public PayLoginVo mobileLogin(String mobile, String password) {

        PayUser user = payUserMapper.selectOne(new QueryWrapper<PayUser>()
                .eq("phone_number", mobile)
        );

        Assert.notNull(user, "Account does not exist!");
        String pwd = ToolUtils.makeMd5(password + user.getSalt());
        Assert.isFalse(!pwd.equals(user.getPassword()), "Incorrect username or password!");
        Assert.isFalse(user.getStatus().equals(-1), "Sorry account has been disabled!"); //status if 0=Disabled

        return this.loginToken(user.getUuid(), user.getPhoneNumber(), user.getStatus());
    }

    @Override
    public void logout(String token) {
        StpUtil.logout();
    }

    @Override
    public Boolean forgetPassword(String password) {
        String userId = (String) StpUtil.getLoginId();

        PayUser user = payUserMapper.selectOne(new QueryWrapper<PayUser>()
                .eq(Constants.UUID, userId)
        );

        Assert.notNull(user, "Account does not exist!");
        String salt = ToolUtils.randomString(5);
        String pwd = ToolUtils.makeMd5(password + salt);

        user.setPassword(pwd);
        user.setSalt(salt);

        int err = payUserMapper.updateById(user);
        if (err != 1)
            return false;
        else
            return true;
    }

    @Override
    public Boolean register(PayRegisterValidate payRegisterValidate) {

        PayUser model = payUserMapper.selectOne(new QueryWrapper<PayUser>()
                .eq("phone_number", payRegisterValidate.getPhone())
                .eq("email", payRegisterValidate.getEmail())
        );

        Assert.isNull(model, "The account already exists, change it !");

        String key = "Country:" + payRegisterValidate.getCountryCode();
        String country = RedisUtils.get(key).toString();
        String airPayId = chechkPayId();
        String nickName = "User-" + ToolUtils.randomString(5);
        String salt = ToolUtils.randomString(5);
        String pwd = ToolUtils.makeMd5(payRegisterValidate.getPassword() + salt);

        PayUser user = new PayUser();
        user.setUuid(ToolUtils.makeUUID());
        user.setPayId(airPayId);
        user.setPhoneNumber(payRegisterValidate.getPhone());
        user.setCountry(country);
        user.setEmail(payRegisterValidate.getEmail());
        user.setCreateTime(PayUtils.createDateTime());
        user.setSalt(salt);
        user.setPassword(pwd);
        user.setNickName(nickName);
        user.setAvatar("./img/avatar.png");

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

        List<PayTransferConfigure> transferConfigureList = payTransferLimitsMapper.selectList(new QueryWrapper<PayTransferConfigure>()
                .eq("is_active", StatusEnums.NOT_DELETE.getCode())
        );
        Assert.notNull(transferConfigureList, "Limit is not found please for Transfer, try agan");

        List<PayUserAccount> list = new LinkedList<>();
        PayUserAccount account1 = new PayUserAccount();
        account1.setUuid(ToolUtils.makeUUID());
        account1.setPayId(airPayId);
        account1.setBalance(BigDecimal.valueOf(0.00));
        account1.setUserId(user.getUuid());
        account1.setNickName(nickName);
        account1.setCurrencyType(CurrencyEnum.TRY.getCode());
        account1.setDepositLimit(depositLimits.getUnverifiedUserFee());
        account1.setWithdrawalLimit(withdrawalLimits.getUnverifiedUserFee());
        list.add(account1);

        PayUserAccount account2 = new PayUserAccount();
        account2.setUuid(ToolUtils.makeUUID());
        account2.setPayId(airPayId);
        account2.setBalance(BigDecimal.valueOf(0.00));
        account2.setUserId(user.getUuid());
        account2.setNickName(nickName);
        account2.setCurrencyType(CurrencyEnum.USD.getCode());
        list.add(account2);


        PayUserAccount account3 = new PayUserAccount();
        account3.setUuid(ToolUtils.makeUUID());
        account3.setPayId(airPayId);
        account3.setBalance(BigDecimal.valueOf(0.00));
        account3.setUserId(user.getUuid());
        account3.setNickName(nickName);
        account3.setCurrencyType(CurrencyEnum.EUR.getCode());
        list.add(account3);

        for (PayUserAccount payUserAccount : list) {
            payUserAccount.setCreateTime(PayUtils.createDateTime());
            for (PayTransferConfigure transferConfigure:transferConfigureList){
                if (transferConfigure.getCurrencyId().equals(payUserAccount.getCurrencyType())){
                    payUserAccount.setTransferLimit(transferConfigure.getUnverifiedUserFee());
                }
            }
            int result = payUserAccountMapper.insert(payUserAccount);
            if (result != 1) {
                return false;
            }
        }
        int err = payUserMapper.insert(user);
        assert err <= 0 : "Failed to create user";
        iRedisPanelDataService.addUserCount();

        return true;
    }

    /**
     * @param userId UUID
     * @param number accountNumber
     * @return PayloginVo
     */
    private PayLoginVo loginToken(String userId, String number, Integer status) {
        //Get valid session account ID
        StpUtil.login(userId);

        PayUser user = new PayUser();
        user.setLastLoginIp(IpUtils.getHostIp());
        user.setLastLoginTime(PayUtils.createDateTime());
        payUserMapper.update(user, new QueryWrapper<PayUser>().eq("uuid", userId));

        PayLoginVo vo = new PayLoginVo();
        vo.setUuid(userId);
        vo.setAccountNumber(number);
        vo.setStatus(status);
        vo.setToken(StpUtil.getTokenValue());
        return vo;
    }

    private Boolean checkMailCode(String mail, String code) {
        String redisCode = RedisUtils.get(mail).toString();
        if (code.equals(redisCode))
            return true;
        else
            return false;
    }

    private String chechkPayId() {
        String key = "payId";
        String newPayId = RedisUtils.get(key).toString();
        String firstPayId = "1111111111";
        if (newPayId == null) {
            RedisUtils.set(key, firstPayId);
            return firstPayId;
        } else {
            int incrementedValue = Integer.parseInt(newPayId) + 1;
            RedisUtils.set(key, String.valueOf(incrementedValue));
            return String.valueOf(incrementedValue);
        }
    }


}

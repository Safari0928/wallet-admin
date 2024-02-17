package com.mdd.mobile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.payBankCard.PayBankCards;
import com.mdd.common.mapper.payBankCard.PayBankCardMapper;
import com.mdd.common.util.PayUtils;
import com.mdd.common.util.ToolUtils;
import com.mdd.mobile.LikeMobileThreadLocal;
import com.mdd.mobile.enums.ActiveEnums;
import com.mdd.mobile.enums.PayError;
import com.mdd.mobile.enums.StatusEnums;
import com.mdd.mobile.service.IBankCardService;
import com.mdd.mobile.validate.bankCard.BankCardValidate;
import com.mdd.mobile.validate.common.Constants;
import com.mdd.mobile.validate.common.PageValidate;
import com.mdd.mobile.vo.BankCardVo;
import com.mdd.mobile.vo.deposit.ErrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Service
public class BankCardServiceImpl implements IBankCardService {
    @Resource
    PayBankCardMapper payBankCardMapper;
    /**
     * 文章分类
     *
     * @return List<BackCardVo>
     * @author Carullah
     */
    @Override
    @Transactional
    public PageResult<BankCardVo> list(PageValidate pageValidate, String userId) {
        Integer pageNo = pageValidate.getPageNo();
        Integer pageSize = pageValidate.getPageSize();

        QueryWrapper<PayBankCards> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Constants.USERID, userId);
        queryWrapper.eq("is_delete",1);
        queryWrapper.orderByDesc(Constants.CREATETIME);

        IPage<PayBankCards> iPage = payBankCardMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);

        List<BankCardVo> list = new LinkedList<>();
        for (PayBankCards bankCards : iPage.getRecords()) {
            BankCardVo vo = new BankCardVo();
            vo.setBankName(bankCards.getBankName());
            vo.setCartNumber(PayUtils.replaceSubstringWithChar(bankCards.getCardNumber(),4,12,'*'));
            vo.setBankName(bankCards.getBankName());
            vo.setBankType(bankCards.getCardType());
            vo.setUuid(bankCards.getUuid());
            vo.setUserId(bankCards.getUserId());
            vo.setCreateTime(PayUtils.createDateTime());
            BeanUtils.copyProperties(bankCards, vo);
            list.add(vo);
        }

        return PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), list);
    }

    @Override
    @Transactional
    public ErrVo addBankCard(BankCardValidate bankCardValidate) {

        String userId= LikeMobileThreadLocal.getUserId();
        ErrVo errVo = new ErrVo();
        PayBankCards payBankCards = payBankCardMapper.selectOne(
                new QueryWrapper<PayBankCards>()
                        .eq("card_number", bankCardValidate.getCardNumber())
        );

        if (payBankCards != null) {
            errVo.setMsg(PayError.BANKCARD_ALREADY_EXIST.getMsg());
            errVo.setCode(PayError.BANKCARD_ALREADY_EXIST.getCode());
            errVo.setData(null);
            return errVo;
        }

        PayBankCards model = new PayBankCards();
        model.setUuid(ToolUtils.makeUUID());
        model.setUserId(userId);
        model.setBankName(bankCardValidate.getBankName());
        model.setCardNumber(bankCardValidate.getCardNumber());
        model.setCardType(bankCardValidate.getCardType());
        model.setCvv(bankCardValidate.getCvv());
        model.setFullName(bankCardValidate.getFullName());
        model.setCreateTime(PayUtils.createDateTime());
        model.setExpiryDate(bankCardValidate.getExpiryDate());
        model.setIsDelete(StatusEnums.NOT_DELETE.getCode());
        int err = payBankCardMapper.insert(model);

        if (err != 1) {
            errVo.setMsg(PayError.ASSERT_MYBATIS_ERROR.getMsg());
            errVo.setCode(PayError.ASSERT_MYBATIS_ERROR.getCode());
            return errVo;
        }

        errVo.setMsg(PayError.SUCCESS.getMsg());
        errVo.setCode(PayError.SUCCESS.getCode());
        return errVo;
    }

    @Transactional
    @Override
    public ErrVo cancelBankCard(String userId, String bankCardId) {
        ErrVo errVo = new ErrVo();
            PayBankCards payBankCards = payBankCardMapper.selectOne(
                    new QueryWrapper<PayBankCards>()
                            .eq(Constants.USERID, userId)
                            .eq(Constants.UUID, bankCardId)
                            .eq("is_delete", ActiveEnums.ACTIVE.getCode())
            );

            if (payBankCards == null) {
                errVo.setMsg(PayError.BANKCARD_NOT_FOUND.getMsg());
                errVo.setCode(PayError.BANKCARD_NOT_FOUND.getCode());
                return errVo;
            }
            payBankCards.setIsDelete(ActiveEnums.NOT_ACTIVE.getCode());
            payBankCards.setUpdateTime(PayUtils.createDateTime());
            int err = payBankCardMapper.updateById(payBankCards);
            if (err != 1) {
                errVo.setMsg(PayError.ASSERT_MYBATIS_ERROR.getMsg());
                errVo.setCode(PayError.ASSERT_MYBATIS_ERROR.getCode());
                return errVo;
            }
            errVo.setCode(PayError.SUCCESS.getCode());
            errVo.setMsg(PayError.SUCCESS.getMsg());

        return errVo;
    }

}

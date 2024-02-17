package com.mdd.payadmin.service;

import com.mdd.payadmin.validate.currency.CurrencyValidate;
import com.mdd.payadmin.vo.CurrencyConfigureVo;
import com.mdd.payadmin.vo.CurrencyEditVo;

import java.util.List;

public interface ICurrencyConfigureService {

    CurrencyEditVo detail(String currencyID);

    boolean editCurrency( CurrencyValidate currencyValidate);

    List<CurrencyConfigureVo> list();
}

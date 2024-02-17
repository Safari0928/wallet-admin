package com.mdd.payadmin.service;

import com.mdd.payadmin.validate.transfer.*;
import com.mdd.payadmin.vo.TransferConfigureVo;

public interface ITransferConfigureService {

    TransferConfigureVo defaultPage(Integer currency);

    Boolean update(TransferConfigureValidate validate);
}

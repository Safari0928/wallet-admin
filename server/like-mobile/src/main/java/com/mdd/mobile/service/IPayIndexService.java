package com.mdd.mobile.service;

import com.mdd.mobile.validate.account.VerifyAccountValidate;
import java.util.Map;

public interface IPayIndexService {

    Map<String, Object> userAccount(String userId);

    Boolean verifyAccount(VerifyAccountValidate verifyAccountValidate);

}

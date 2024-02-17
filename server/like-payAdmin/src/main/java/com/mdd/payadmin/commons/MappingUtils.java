package com.mdd.payadmin.commons;

import com.mdd.common.enums.CurrencyEnum;
import com.mdd.payadmin.enums.*;

public class MappingUtils {
    public static String mapStatus(int status) {
        switch (status) {
            case 1:
                return StatusEnums.SUCCESS.getMsg();
            case -1:
                return StatusEnums.FAIL.getMsg();
            case 2:
                return StatusEnums.PENDING.getMsg();
            default:
                return "Unknown Status";
        }
    }

    public static String mapTypeDetail(int typeDetail) {
        if (typeDetail >= 1 && typeDetail <= TypeDetailEnums.values().length) {
            return TypeDetailEnums.values()[typeDetail - 1].getMsg();
        } else {
            return "Unknown Type";
        }
    }

    public static String mapCardType(int cardType) {
        if (cardType >= 1 && cardType <= CardTypeEnum.values().length) {
            return CardTypeEnum.values()[cardType - 1].getMsg();
        } else {
            return "Unknown Type";
        }
    }

    public static String mapCurrency(int currency){
        if (currency >= 1 && currency <= CurrencyEnum.values().length) {
            return CurrencyEnum.values()[currency - 1].getMsg();
        } else {
            return "Unknown Type";
        }
    }


    public static String mapUserVerify(int status) {
        switch (status) {
            case 1:
                return UserVerifyEnum.VERIFIED.getMsg();
            case -1:
                return UserVerifyEnum.UNVERIFIED.getMsg();
            case 2:
                return UserVerifyEnum.PENDING.getMsg();
            default:
                return "Unknown Status";
        }
    }


    public static String mapUserStatus(int status) {
        switch (status) {
            case 1:
                return UserStatusEnum.NORMAL.getMsg();
            case -1:
                return UserStatusEnum.DISABLED.getMsg();
            default:
                return "Unknown Status";
        }
    }

}

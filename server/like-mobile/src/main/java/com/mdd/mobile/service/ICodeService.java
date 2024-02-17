package com.mdd.mobile.service;

public interface ICodeService {

   /**
    * send code to mail
    * @param email
    * @return Boolean
    */
   Boolean sendMailVerificationCode( String email);

   /**
    * send code to SMS
    * @param phone
    * @return Boolean
    */
   Boolean sendPhoneVerificationCode(String phone);

   /**
    * checks the sent code from redis
    * @param value
    * @param code
    * @return Boolean
    */
   Boolean codeCheck(String value, String code);

   Boolean saveVerificationCode(String key, String code);

   Boolean forgetPasswordCode(String phone);
}

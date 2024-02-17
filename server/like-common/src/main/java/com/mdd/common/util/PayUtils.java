package com.mdd.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class PayUtils {

    /**
     * Generate Transaction Number
     *
     * @return String
     */
    public static String generateTransactionNumber(String country, Integer type) {

        // Create the country, type, and date parts in an alphanumeric format
        String cType = "00" + type;
        String date = timestampToDate("yyMMdd");
        String time = timestampToDate("HHmmss");

        // Generate the random number part (7-digit random number)
        int randomNumber = Integer.parseInt(ToolUtils.randomInt(7));

        // Combine the parts to create the transaction number
        return country + cType + date + time + randomNumber;
    }

    public static Integer generateVerificationCode(){
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return code;
    }

    /**
     * Generate time by format
     *
     * @param format 格式串
     * @return String
     */
    public static String timestampToDate(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    /**
     * Generate DateTime
     *
     * @return String
     */
    public static String createDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String createDate(String time) {
        if (time == null) {
            time = "0";
        }
        long longTime = Long.parseLong(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date(longTime * 1000));
    }

    /**
     * Generate Nick Name
     *
     * @return String
     */
    public String generateNickName() {

        return "User-" + ToolUtils.randomString(5);
    }



    public static String replaceSubstringWithChar(String input, int start, int end, char replacementChar) {
        if (input == null || start < 0 || end > input.length() || start > end) {
            return null;
        }

        StringBuilder result = new StringBuilder(input);

        for (int i = start; i < end; i++) {
            result.setCharAt(i, replacementChar);
        }

        return result.toString();
    }

    public static String formatTransactionNumber(String transactionNumber) {
        if (transactionNumber == null) {
            return null;
        }
        String firstThreeDigits = transactionNumber.substring(0, 3);
        String lastFiveDigits = transactionNumber.substring(transactionNumber.length() - 5);
        String formattedTransactionNumber = firstThreeDigits + "....." + lastFiveDigits;
        return formattedTransactionNumber;
    }

}

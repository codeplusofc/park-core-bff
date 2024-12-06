package com.ParkCore.util;

import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern SSN_PATTERN = Pattern.compile("^\\d{11}$"); // CPF is similar to SSN
    private static final String PHONE_REGEX = "^(\\(\\d{2}\\)\\s?)?(\\d{4,5}-\\d{4})$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    public static boolean isPhoneNumberValid(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isSsnValid(String ssn) { // CPF is similar to SSN
        if (ssn == null || ssn.length() != 11 || SSN_PATTERN.matcher(ssn).matches() == false ||
                ssn.equals("00000000000") || ssn.equals("11111111111") || ssn.equals("22222222222") ||
                ssn.equals("33333333333") || ssn.equals("44444444444") || ssn.equals("55555555555") ||
                ssn.equals("66666666666") || ssn.equals("77777777777") || ssn.equals("88888888888") ||
                ssn.equals("99999999999") || ssn.equals("01234567890")) {
            return false;
        }

        int totalSum = 0;
        for (int i = 0; i < 9; i++) {
            totalSum += Character.getNumericValue(ssn.charAt(i)) * (10 - i);
        }

        int checkResult1 = totalSum % 11;
        int digit1 = (checkResult1 < 2) ? 0 : 11 - checkResult1;

        int totalSum2 = 0;
        for (int i = 0; i < 10; i++) {
            totalSum2 += Character.getNumericValue(ssn.charAt(i)) * (11 - i);
        }

        int checkResult2 = totalSum2 % 11;
        int digit2 = (checkResult2 < 2) ? 0 : 11 - checkResult2;

        return Character.getNumericValue(ssn.charAt(9)) == digit1 &&
                Character.getNumericValue(ssn.charAt(10)) == digit2;
    }
}

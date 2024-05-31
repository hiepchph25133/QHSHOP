package com.poly.utils;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class NumberFormater {
    public static String convertVND(double amount){
        long longAmount = (long) amount;
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        format.setCurrency(Currency.getInstance("VND"));
        String formattedAmount = format.format(longAmount);
        return formattedAmount;
    }
}

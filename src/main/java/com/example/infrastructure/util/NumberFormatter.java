package com.example.infrastructure.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

public class NumberFormatter {
    
    public static BigDecimal parseBigDecimal(String value, String decimalSeparator, 
                                           String groupingSeparator, Integer precision) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
            symbols.setDecimalSeparator(decimalSeparator.charAt(0));
            symbols.setGroupingSeparator(groupingSeparator.charAt(0));

            DecimalFormat decimalFormat = new DecimalFormat("#,##0.#");
            decimalFormat.setDecimalFormatSymbols(symbols);
            decimalFormat.setParseBigDecimal(true);

            BigDecimal number = (BigDecimal) decimalFormat.parse(value.trim());
            return precision != null ? number.setScale(precision, RoundingMode.HALF_UP) : number;
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse number: " + value, e);
        }
    }
} 
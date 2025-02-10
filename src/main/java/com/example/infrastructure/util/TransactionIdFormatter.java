package com.example.infrastructure.util;

import org.apache.commons.lang3.StringUtils;

public class TransactionIdFormatter {
    private static final int TRANSACTION_ID_LENGTH = 12;

    public static String formatTransactionId(String transactionId) {
        if (transactionId == null) {
            return null;
        }
        return StringUtils.leftPad(transactionId, TRANSACTION_ID_LENGTH, '0');
    }
} 
package com.hyperativa.services.card.dto;

public record Row(String identifier, String cardNumber) {

    private static final String CARD_LINE_IDENTIFIER = "C";

    public static Row parse(String row) {
        String rowIdentifier = row.substring(0, 1).trim();
        if (!rowIdentifier.equals(CARD_LINE_IDENTIFIER)) {
            throw new IllegalArgumentException("Invalid line identifier for card: " + rowIdentifier);
        }
        String identifier = row.substring(1, 7).trim();
        String cardNumber = row.substring(8, Math.min(26, row.length())).trim();
        return new Row(identifier, cardNumber);
    }
}
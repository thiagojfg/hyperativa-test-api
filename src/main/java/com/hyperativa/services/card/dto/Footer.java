package com.hyperativa.services.card.dto;

public record Footer(String batch, int recordsCount) {

    public static Footer parse(String footer) {
        String batch = footer.substring(0, 8).trim();
        String recordsCount = footer.substring(9, 14).trim();
        return new Footer(batch, Integer.parseInt(recordsCount));
    }
}
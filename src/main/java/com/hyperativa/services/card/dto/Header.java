package com.hyperativa.services.card.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record Header(String fileName, LocalDate data, String batchIdentifier, int recordsCount) {

    public static Header parse(String header) {
        String fileName = header.substring(0, 28).trim();
        String date = header.substring(29, 37).trim();
        String lot = header.substring(37, 45).trim();
        String recordCount = header.substring(46, 51).trim();
        return new Header(
                fileName,
                LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd")),
                lot,
                Integer.parseInt(recordCount));
    }
}
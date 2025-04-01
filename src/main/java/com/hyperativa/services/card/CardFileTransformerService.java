package com.hyperativa.services.card;

import com.hyperativa.exceptions.CardFileMalformedException;
import com.hyperativa.services.card.dto.CardFileContent;
import com.hyperativa.services.card.dto.Footer;
import com.hyperativa.services.card.dto.Header;
import com.hyperativa.services.card.dto.Row;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CardFileTransformerService {

    private final CardFileValidatorService cardFileValidatorService;

    public CardFileContent transform(List<String> extractedLines) {
        extractedLines.removeIf(String::isBlank);
        if (!hasEnoughContent(extractedLines)) {
            throw new CardFileMalformedException("File doesn't have a valid format");
        }
        Header header = Header.parse(extractedLines.getFirst());
        List<Row> cardLines = extractedLines.subList(1, extractedLines.size() - 1)
                .stream()
                .map(Row::parse)
                .toList();
        Footer footer = Footer.parse(extractedLines.getLast());
        CardFileContent cardFileContent = new CardFileContent(header, cardLines, footer);
        cardFileValidatorService.validate(cardFileContent);
        return cardFileContent;
    }

    private boolean hasEnoughContent(List<String> extractedLines) {
        return extractedLines.size() >= 2; // file must have at least header and footer
    }
}

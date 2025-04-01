package com.hyperativa.services.card;

import com.hyperativa.exceptions.CardFileProcessingException;
import com.hyperativa.services.card.dto.CardFileContent;
import org.springframework.stereotype.Service;

@Service
public class CardFileValidatorService {

    public void validate(CardFileContent cardFileContent) throws CardFileProcessingException {
        if (cardFileContent.header().recordsCount() != cardFileContent.footer().recordsCount()) {
            throw new CardFileProcessingException("Record count in header and footer don't match");
        }
        if (cardFileContent.header().recordsCount() != cardFileContent.rows().size()) {
            throw new CardFileProcessingException("Record count in header and number of rows don't match");
        }
    }
}

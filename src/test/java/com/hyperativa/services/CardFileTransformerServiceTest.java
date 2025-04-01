package com.hyperativa.services;

import com.hyperativa.exceptions.CardFileProcessingException;
import com.hyperativa.services.card.CardFileTransformerService;
import com.hyperativa.services.card.CardFileValidatorService;
import com.hyperativa.services.card.dto.CardFileContent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CardFileTransformerServiceTest {

    @Mock
    CardFileValidatorService cardFileValidatorService;

    @Test
    void shouldTransformAndMatchData() throws IOException, CardFileProcessingException {
        List<String> lines = Files.readAllLines(Paths.get("src/test/resources/DESAFIO-HYPERATIVA.txt"));

        CardFileTransformerService transformer = new CardFileTransformerService(cardFileValidatorService);
        CardFileContent cardFileContent = transformer.transform(lines);

        assertNotNull(cardFileContent);
        assertNotNull(cardFileContent.header());
        assertNotNull(cardFileContent.footer());
        assertEquals(10, cardFileContent.rows().size());
    }
}

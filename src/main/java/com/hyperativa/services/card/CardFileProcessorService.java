package com.hyperativa.services.card;

import com.hyperativa.exceptions.CardFileMalformedException;
import com.hyperativa.exceptions.CardFileProcessingException;
import com.hyperativa.services.card.dto.CardFileContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CardFileProcessorService {

    private final CardFileTransformerService cardFileTransformerService;
    private final CardFilePersistenceService cardFilePersistenceService;

    @Async
    public void processFileContentAsync(MultipartFile file) {

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            List<String> extractedLines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                extractedLines.add(line);
            }

            CardFileContent cardFileContent = cardFileTransformerService.transform(extractedLines);
            if (!cardFileContent.header().fileName().concat(".txt").equals(file.getOriginalFilename())) {
                throw new CardFileMalformedException("File name doesn't match with internal name");
            }

            log.info("Processed {} cards from file: {}", cardFileContent.rows().size(), file.getOriginalFilename());

            cardFilePersistenceService.persist(cardFileContent);
        } catch (IOException e) {
            throw new CardFileProcessingException("Failed to read list of cards from txt file", e);
        }
    }
}

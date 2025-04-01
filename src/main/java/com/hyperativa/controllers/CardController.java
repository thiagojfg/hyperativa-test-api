package com.hyperativa.controllers;

import com.hyperativa.services.card.CardFileProcessorService;
import com.hyperativa.services.card.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardFileProcessorService cardFileProcessorService;
    private final CardService cardService;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }
        cardFileProcessorService.processFileContentAsync(file);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Long> searchCard(@RequestParam("cardNumber") String cardNumber) {
        Long cardId = cardService.findByCardNumber(cardNumber);
        if (cardId == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cardId);
    }

    @PutMapping
    public ResponseEntity<Long> registerCard(@RequestParam("cardNumber") String cardNumber) {
        Long cardId = cardService.saveCardNumber(cardNumber);
        return ResponseEntity.ok(cardId);
    }
}

package com.hyperativa.services.card;

import com.hyperativa.models.CardEntity;
import com.hyperativa.repositories.CardsRepository;
import com.hyperativa.services.card.dto.CardFileContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CardFilePersistenceService {

    private final CardsRepository cardsRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, timeout = 300)
    public void persist(CardFileContent cardFileContent) {
        List<CardEntity> cardEntities = cardFileContent.rows().stream()
                .map(row -> {
                    CardEntity cardEntity = new CardEntity();
                    cardEntity.setCardNumber(row.cardNumber());
                    return cardEntity;
                }).toList();

        for (CardEntity cardEntity : cardEntities) {
            if (cardsRepository.findByCardNumber(cardEntity.getCardNumber()).isEmpty()) {
                cardsRepository.save(cardEntity);
            } else {
                log.info("Card number {} already exists, ignoring...", cardEntity.getCardNumber());
            }
        }
    }
}

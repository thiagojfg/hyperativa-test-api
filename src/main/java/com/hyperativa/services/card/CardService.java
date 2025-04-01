package com.hyperativa.services.card;

import com.hyperativa.models.CardEntity;
import com.hyperativa.repositories.CardsRepository;
import com.hyperativa.exceptions.DuplicatedCardNumberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CardService {

    private final CardsRepository cardsRepository;

    public Long findByCardNumber(String cardNumber) {
        return cardsRepository.findByCardNumber(cardNumber)
                .map(CardEntity::getId)
                .orElse(null);
    }

    public Long saveCardNumber(String cardNumber) {
        if (findByCardNumber(cardNumber) != null) {
            throw new DuplicatedCardNumberException(cardNumber);
        }
        CardEntity cardEntity = new CardEntity();
        cardEntity.setCardNumber(cardNumber);
        return cardsRepository.save(cardEntity).getId();
    }
}

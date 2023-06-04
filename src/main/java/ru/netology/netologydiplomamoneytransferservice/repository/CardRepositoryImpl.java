package ru.netology.netologydiplomamoneytransferservice.repository;

import lombok.Getter;
import org.springframework.stereotype.Repository;
import ru.netology.netologydiplomamoneytransferservice.domain.Card;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CardRepositoryImpl implements CardRepository {

    @Getter
    private final Map<String, Card> cards = new ConcurrentHashMap<>();

    @Override
    public Optional<Card> getCardByNumber(String cardNumber) {
        return Optional.ofNullable(cards.get(cardNumber));
    }
}

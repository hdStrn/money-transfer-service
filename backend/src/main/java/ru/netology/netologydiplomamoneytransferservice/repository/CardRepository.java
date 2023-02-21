package ru.netology.netologydiplomamoneytransferservice.repository;

import ru.netology.netologydiplomamoneytransferservice.domain.Card;

import java.util.Optional;

public interface CardRepository {

    Optional<Card> getCardByNumber(String cardNumber);
}

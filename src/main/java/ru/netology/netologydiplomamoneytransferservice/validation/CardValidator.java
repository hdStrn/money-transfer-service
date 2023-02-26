package ru.netology.netologydiplomamoneytransferservice.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.netology.netologydiplomamoneytransferservice.api.dto.Transfer;
import ru.netology.netologydiplomamoneytransferservice.api.error.InvalidCardDataException;
import ru.netology.netologydiplomamoneytransferservice.domain.Card;
import ru.netology.netologydiplomamoneytransferservice.repository.CardRepository;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CardValidator {

    @Value("${transfer.commission:0}")
    private Double transferCommission;
    private final CardRepository cardRepository;

    public boolean validateCardData(Transfer transfer) {
        String cardFromNumber = transfer.getCardFromNumber();
        Card validCardFrom = cardRepository.getCardByNumber(cardFromNumber).orElseThrow(
                () -> new InvalidCardDataException("Карта с номером " + cardFromNumber + " не найдена в базе")
        );

        validateDateAndCvv(transfer, validCardFrom);
        validateCurrency(transfer, validCardFrom);
        validateAmount(transfer, validCardFrom);

        return true;
    }

    public boolean validateDateAndCvv(Transfer transfer, Card validCardFrom) {
        boolean validTillIsCorrect = Objects.equals(validCardFrom.getValidTill(), transfer.getCardFromValidTill());
        boolean cvvIsCorrect = Objects.equals(validCardFrom.getCvv(), transfer.getCardFromCVV());
        if (!validTillIsCorrect || !cvvIsCorrect) {
            throw new InvalidCardDataException("Введены неверные данные карты (срок действия / CVV номер)");
        }
        return true;
    }

    public boolean validateCurrency(Transfer transfer, Card validCardFrom) {
        String transferCurrency = transfer.getAmount().getCurrency();
        if (!validCardFrom.getAmounts().containsKey(transferCurrency)) {
            throw new InvalidCardDataException("На выбранной карте отсутствует счет в валюте " + transferCurrency);
        }
        return true;
    }

    public boolean validateAmount(Transfer transfer, Card validCardFrom) {
        String transferCurrency = transfer.getAmount().getCurrency();
        Integer cardAvailableAmount = validCardFrom.getAmounts().get(transferCurrency).getValue();
        Integer transferAmountWithCommission = (int) (transfer.getAmount().getValue() * (1 + transferCommission));
        if (cardAvailableAmount < transferAmountWithCommission) {
            throw new InvalidCardDataException("На выбранной карте недостаточно средств. На карте имеется " +
                    cardAvailableAmount + ", необходимо (с учетом комиссии) " + transferAmountWithCommission);
        }
        return true;
    }
}

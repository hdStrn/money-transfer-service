package ru.netology.netologydiplomamoneytransferservice.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.netology.netologydiplomamoneytransferservice.api.dto.*;
import ru.netology.netologydiplomamoneytransferservice.domain.Card;
import ru.netology.netologydiplomamoneytransferservice.api.error.InvalidCardDataException;
import ru.netology.netologydiplomamoneytransferservice.api.error.InvalidConfirmationDataException;
import ru.netology.netologydiplomamoneytransferservice.repository.CardRepository;
import ru.netology.netologydiplomamoneytransferservice.repository.TransferRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class TransferServiceImpl implements TransferService {

    @Value("${verification.code:0000}")
    private String verificationCode;
    @Value("${transfer.commission:0}")
    private Double transferCommission;
    private final TransferRepository transferRepository;
    private final CardRepository cardRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger("file-logger");

    @Override
    public SuccessTransfer transfer(Transfer transfer) {
        // делим сумму на 100, т.к. с фронта приходит неверное значение (умноженное на 100)
        transfer.getAmount().setValue(transfer.getAmount().getValue() / 100);

        String cardFromNumber = transfer.getCardFromNumber();
        Card validCardFrom = cardRepository.getCardByNumber(cardFromNumber).orElseThrow(
                () -> new InvalidCardDataException("Карта с номером " + cardFromNumber + " не найдена в базе")
        );

        validateCardData(transfer, validCardFrom);

        Long operationId = transferRepository.addTransfer(transfer);
        return new SuccessTransfer(String.valueOf(operationId));
    }

    @Override
    public SuccessConfirmation confirm(OperationConfirmation confirmation) {

        if (!confirmation.getCode().equals(verificationCode)) {
            throw new InvalidConfirmationDataException("Неверный код подтверждения (" + confirmation.getCode() + ")");
        }

        if (!transferRepository.confirmOperation(confirmation)) {
            throw new InvalidConfirmationDataException("Нет операции с id " + confirmation.getOperationId());
        }

        executeTransfer(confirmation.getOperationId());

        return new SuccessConfirmation(confirmation.getOperationId());
    }

    @Override
    public void executeTransfer(String operationId) {

        Transfer transfer = transferRepository.getTransferById(operationId);
        Card validCardFrom = cardRepository.getCardByNumber(transfer.getCardFromNumber()).get();

        String transferCurrency = transfer.getAmount().getCurrency();
        Integer transferAmountWithCommission = (int) (transfer.getAmount().getValue() * (1 + transferCommission));
        Integer balance = validCardFrom.getAmounts().get(transferCurrency).getValue() - transferAmountWithCommission;

        validCardFrom.getAmounts().put(transferCurrency, new Amount(balance, transferCurrency));

        LOGGER.info("С карты {} успешно переведена сумма в размере {} на карту {}. Размер комиссии составил {} {}. " +
                "Остаток на карте: {} {}. ID операции: {}",
                transfer.getCardFromNumber(),
                transfer.getAmount(),
                transfer.getCardToNumber(),
                transfer.getAmount().getValue() * transferCommission, transferCurrency,
                balance, transferCurrency,
                operationId);
    }

    @Override
    public void validateCardData(Transfer transfer, Card validCardFrom) throws InvalidCardDataException {

        boolean validTillIsCorrect = Objects.equals(validCardFrom.getValidTill(), transfer.getCardFromValidTill());
        boolean cvvIsCorrect = Objects.equals(validCardFrom.getCvv(), transfer.getCardFromCVV());
        if (!validTillIsCorrect || !cvvIsCorrect) {
            throw new InvalidCardDataException("Введены неверные данные карты (срок действия / CVV номер)");
        }

        String transferCurrency = transfer.getAmount().getCurrency();
        if (!validCardFrom.getAmounts().containsKey(transferCurrency)) {
            throw new InvalidCardDataException("На выбранной карте отсутствует счет в валюте " + transferCurrency);
        }

        Integer cardAvailableAmount = validCardFrom.getAmounts().get(transferCurrency).getValue();
        Integer transferAmountWithCommission = (int) (transfer.getAmount().getValue() * (1 + transferCommission));
        if (cardAvailableAmount < transferAmountWithCommission) {
            throw new InvalidCardDataException("На выбранной карте недостаточно средств. На карте имеется " +
                    cardAvailableAmount + ", необходимо (с учетом комиссии) " + transferAmountWithCommission);
        }
    }
}

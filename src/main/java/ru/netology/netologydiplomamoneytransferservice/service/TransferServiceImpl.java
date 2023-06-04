package ru.netology.netologydiplomamoneytransferservice.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.netology.netologydiplomamoneytransferservice.api.dto.*;
import ru.netology.netologydiplomamoneytransferservice.domain.Card;
import ru.netology.netologydiplomamoneytransferservice.repository.CardRepository;
import ru.netology.netologydiplomamoneytransferservice.repository.TransferRepository;
import ru.netology.netologydiplomamoneytransferservice.validation.CardValidator;
import ru.netology.netologydiplomamoneytransferservice.validation.ConfirmationValidator;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    @Value("${transfer.commission:0}")
    private Double transferCommission;
    private final TransferRepository transferRepository;
    private final CardRepository cardRepository;
    private final CardValidator cardValidator;
    private final ConfirmationValidator confirmationValidator;
    private static final String LOGGER_NAME = "file-logger";
    private static final Logger LOGGER = LoggerFactory.getLogger(LOGGER_NAME);

    @Override
    public SuccessTransfer transfer(Transfer transfer) {
        // делим сумму на 100, т.к. с фронта приходит неверное значение (умноженное на 100)
        transfer.getAmount().setValue(transfer.getAmount().getValue() / 100);

        return Optional.of(transfer)
                .filter(cardValidator::validateCardData)
                .map(transferRepository::addTransfer)
                .map(String::valueOf)
                .map(SuccessTransfer::new)
                .orElseThrow();
    }

    @Override
    public SuccessConfirmation confirm(OperationConfirmation confirmation) {

        return Optional.of(confirmation)
                .filter(confirmationValidator::validateConfirmation)
                .map(OperationConfirmation::getOperationId)
                .filter(this::executeTransfer)
                .map(SuccessConfirmation::new)
                .orElseThrow();
    }

    @Override
    public boolean executeTransfer(String operationId) {

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

        return true;
    }
}

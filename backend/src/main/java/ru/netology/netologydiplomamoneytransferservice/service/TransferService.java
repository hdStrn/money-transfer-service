package ru.netology.netologydiplomamoneytransferservice.service;

import ru.netology.netologydiplomamoneytransferservice.domain.Card;
import ru.netology.netologydiplomamoneytransferservice.domain.OperationConfirmation;
import ru.netology.netologydiplomamoneytransferservice.domain.Transfer;
import ru.netology.netologydiplomamoneytransferservice.exception.InvalidCardDataException;
import ru.netology.netologydiplomamoneytransferservice.exception.InvalidConfirmationDataException;

public interface TransferService {

    Long transfer(Transfer transfer) throws InvalidCardDataException;
    boolean confirm(OperationConfirmation confirmation) throws InvalidConfirmationDataException;
    void executeTransfer(String operationId);
    void validateCardData(Transfer transfer, Card validCardFrom) throws InvalidCardDataException;
}

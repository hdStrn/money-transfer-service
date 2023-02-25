package ru.netology.netologydiplomamoneytransferservice.service;

import ru.netology.netologydiplomamoneytransferservice.domain.Card;
import ru.netology.netologydiplomamoneytransferservice.dto.OperationConfirmation;
import ru.netology.netologydiplomamoneytransferservice.dto.Transfer;
import ru.netology.netologydiplomamoneytransferservice.dto.SuccessConfirmation;
import ru.netology.netologydiplomamoneytransferservice.dto.SuccessTransfer;

public interface TransferService {

    SuccessTransfer transfer(Transfer transfer);
    SuccessConfirmation confirm(OperationConfirmation confirmation);
    void executeTransfer(String operationId);
    void validateCardData(Transfer transfer, Card validCardFrom);
}

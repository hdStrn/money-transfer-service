package ru.netology.netologydiplomamoneytransferservice.service;

import ru.netology.netologydiplomamoneytransferservice.api.dto.OperationConfirmation;
import ru.netology.netologydiplomamoneytransferservice.api.dto.Transfer;
import ru.netology.netologydiplomamoneytransferservice.api.dto.SuccessConfirmation;
import ru.netology.netologydiplomamoneytransferservice.api.dto.SuccessTransfer;

public interface TransferService {

    SuccessTransfer transfer(Transfer transfer);
    SuccessConfirmation confirm(OperationConfirmation confirmation);
    boolean executeTransfer(String operationId);
}

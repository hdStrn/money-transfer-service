package ru.netology.netologydiplomamoneytransferservice.repository;

import org.springframework.stereotype.Repository;
import ru.netology.netologydiplomamoneytransferservice.domain.OperationConfirmation;
import ru.netology.netologydiplomamoneytransferservice.domain.Transfer;

@Repository
public interface TransferRepository {

    Long addTransfer(Transfer transfer);
    boolean confirmOperation(OperationConfirmation confirmation);
    Transfer getTransferById(String id);
}

package ru.netology.netologydiplomamoneytransferservice.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import ru.netology.netologydiplomamoneytransferservice.api.dto.OperationConfirmation;
import ru.netology.netologydiplomamoneytransferservice.api.dto.Transfer;

public interface TransferResource {

    @PostMapping("/transfer")
    ResponseEntity transferMoney(Transfer transfer);
    @PostMapping("/confirmOperation")
    ResponseEntity confirmOperation(OperationConfirmation confirmation);
}

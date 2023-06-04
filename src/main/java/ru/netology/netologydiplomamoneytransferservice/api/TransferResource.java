package ru.netology.netologydiplomamoneytransferservice.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.netology.netologydiplomamoneytransferservice.api.dto.OperationConfirmation;
import ru.netology.netologydiplomamoneytransferservice.api.dto.SuccessConfirmation;
import ru.netology.netologydiplomamoneytransferservice.api.dto.SuccessTransfer;
import ru.netology.netologydiplomamoneytransferservice.api.dto.Transfer;

@RequestMapping
public interface TransferResource {

    @PostMapping("/transfer")
    ResponseEntity<SuccessTransfer> transferMoney(Transfer transfer);
    @PostMapping("/confirmOperation")
    ResponseEntity<SuccessConfirmation> confirmOperation(OperationConfirmation confirmation);
}

package ru.netology.netologydiplomamoneytransferservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.netologydiplomamoneytransferservice.domain.OperationConfirmation;
import ru.netology.netologydiplomamoneytransferservice.domain.Transfer;
import ru.netology.netologydiplomamoneytransferservice.domain.response.SuccessConfirmation;
import ru.netology.netologydiplomamoneytransferservice.domain.response.SuccessTransfer;
import ru.netology.netologydiplomamoneytransferservice.exception.InvalidCardDataException;
import ru.netology.netologydiplomamoneytransferservice.exception.InvalidConfirmationDataException;
import ru.netology.netologydiplomamoneytransferservice.service.TransferService;

@RestController
@CrossOrigin
public class TransferController {

    @Autowired
    private TransferService transferService;

    @PostMapping("/transfer")
    public ResponseEntity<SuccessTransfer> transferMoney(@RequestBody Transfer transfer)
            throws InvalidCardDataException {

        Long id = transferService.transfer(transfer);
        return ResponseEntity.ok(new SuccessTransfer(String.valueOf(id)));
    }

    @PostMapping("/confirmOperation")
    public ResponseEntity<SuccessConfirmation> confirmOperation(@RequestBody OperationConfirmation confirmation)
            throws InvalidConfirmationDataException {

        transferService.confirm(confirmation);
        return ResponseEntity.ok(new SuccessConfirmation(confirmation.getOperationId()));
    }

}

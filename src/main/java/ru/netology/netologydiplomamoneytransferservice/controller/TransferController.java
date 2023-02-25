package ru.netology.netologydiplomamoneytransferservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.netologydiplomamoneytransferservice.dto.OperationConfirmation;
import ru.netology.netologydiplomamoneytransferservice.dto.Transfer;
import ru.netology.netologydiplomamoneytransferservice.dto.SuccessConfirmation;
import ru.netology.netologydiplomamoneytransferservice.dto.SuccessTransfer;
import ru.netology.netologydiplomamoneytransferservice.service.TransferService;

@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@CrossOrigin(
        origins = {"${cors.allowed-origins}"},
        methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class TransferController {

    private final TransferService transferService;

    @PostMapping("/transfer")
    public ResponseEntity<SuccessTransfer> transferMoney(@RequestBody Transfer transfer) {
        return ResponseEntity.ok(transferService.transfer(transfer));
    }

    @PostMapping("/confirmOperation")
    public ResponseEntity<SuccessConfirmation> confirmOperation(@RequestBody OperationConfirmation confirmation) {
        return ResponseEntity.ok(transferService.confirm(confirmation));
    }

}

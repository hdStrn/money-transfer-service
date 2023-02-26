package ru.netology.netologydiplomamoneytransferservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.netologydiplomamoneytransferservice.api.TransferResource;
import ru.netology.netologydiplomamoneytransferservice.api.dto.OperationConfirmation;
import ru.netology.netologydiplomamoneytransferservice.api.dto.Transfer;
import ru.netology.netologydiplomamoneytransferservice.api.dto.SuccessConfirmation;
import ru.netology.netologydiplomamoneytransferservice.api.dto.SuccessTransfer;
import ru.netology.netologydiplomamoneytransferservice.service.TransferService;

@RestController
@RequiredArgsConstructor
@CrossOrigin(
        origins = {"${cors.allowed-origins}"},
        methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class TransferController implements TransferResource {

    private final TransferService transferService;

    @Override
    public ResponseEntity<SuccessTransfer> transferMoney(@RequestBody Transfer transfer) {
        return ResponseEntity.ok(transferService.transfer(transfer));
    }

    @Override
    public ResponseEntity<SuccessConfirmation> confirmOperation(@RequestBody OperationConfirmation confirmation) {
        return ResponseEntity.ok(transferService.confirm(confirmation));
    }

}

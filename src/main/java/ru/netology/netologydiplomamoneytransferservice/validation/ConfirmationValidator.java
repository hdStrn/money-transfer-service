package ru.netology.netologydiplomamoneytransferservice.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.netology.netologydiplomamoneytransferservice.api.dto.OperationConfirmation;
import ru.netology.netologydiplomamoneytransferservice.api.error.InvalidConfirmationDataException;
import ru.netology.netologydiplomamoneytransferservice.repository.TransferRepository;

@Component
@RequiredArgsConstructor
public class ConfirmationValidator {

    @Value("${verification.code:0000}")
    private String verificationCode;
    private final TransferRepository transferRepository;

    public boolean validateConfirmation(OperationConfirmation confirmation) {
        if (!confirmation.getCode().equals(verificationCode)) {
            throw new InvalidConfirmationDataException("Неверный код подтверждения (" + confirmation.getCode() + ")");
        }

        if (!transferRepository.confirmOperation(confirmation)) {
            throw new InvalidConfirmationDataException("Нет операции с id " + confirmation.getOperationId());
        }

        return true;
    }
}

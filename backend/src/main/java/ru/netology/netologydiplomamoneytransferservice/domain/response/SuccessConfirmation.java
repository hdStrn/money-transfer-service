package ru.netology.netologydiplomamoneytransferservice.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessConfirmation {

    private String operationId;
}

package ru.netology.netologydiplomamoneytransferservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import ru.netology.netologydiplomamoneytransferservice.api.dto.Amount;
import ru.netology.netologydiplomamoneytransferservice.api.dto.Transfer;
import ru.netology.netologydiplomamoneytransferservice.api.error.InvalidCardDataException;
import ru.netology.netologydiplomamoneytransferservice.repository.TransferRepository;
import ru.netology.netologydiplomamoneytransferservice.service.TransferServiceImpl;
import ru.netology.netologydiplomamoneytransferservice.validation.CardValidator;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TransferServiceTest {

    private final static Transfer VALID_TRANSFER = new Transfer(
            "1111222233334444", "01/30", "111", "1234567890123456",
            new Amount(50000, "RUR"));
    private final static Transfer INVALID_CARD_TRANSFER = new Transfer(
            "9999888877776666", "01/25", "555", "1234567890123456",
            new Amount(50000, "RUR"));
    @Mock
    private TransferRepository transferRepository;
    @Mock
    private CardValidator cardValidator;
    @InjectMocks
    private TransferServiceImpl transferService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(transferService, "transferCommission", 0.01d);
    }

    @Test
    public void testTransferWhenValidTransferThenReturnOperationId() {
        Mockito.when(cardValidator.validateCardData(VALID_TRANSFER)).thenReturn(true);
        Mockito.when(transferRepository.addTransfer(VALID_TRANSFER)).thenReturn(1L);

        String operationId = transferService.transfer(VALID_TRANSFER).getOperationId();

        Assertions.assertEquals("1", operationId);
    }

    @Test
    public void testTransferWhenInvalidCardTransferThenThrowEx() {
        Mockito.when(cardValidator.validateCardData(INVALID_CARD_TRANSFER)).thenThrow(InvalidCardDataException.class);

        Assertions.assertThrows(InvalidCardDataException.class, () -> transferService.transfer(INVALID_CARD_TRANSFER));
    }
}

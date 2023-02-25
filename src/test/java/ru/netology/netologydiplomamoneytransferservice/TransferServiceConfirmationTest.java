package ru.netology.netologydiplomamoneytransferservice;

import com.google.common.truth.Truth;
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
import ru.netology.netologydiplomamoneytransferservice.domain.Card;
import ru.netology.netologydiplomamoneytransferservice.api.dto.OperationConfirmation;
import ru.netology.netologydiplomamoneytransferservice.api.dto.Transfer;
import ru.netology.netologydiplomamoneytransferservice.api.error.InvalidConfirmationDataException;
import ru.netology.netologydiplomamoneytransferservice.repository.CardRepository;
import ru.netology.netologydiplomamoneytransferservice.repository.TransferRepository;
import ru.netology.netologydiplomamoneytransferservice.service.TransferServiceImpl;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TransferServiceConfirmationTest {

    private final static Card VALID_CARD = new Card("1111222233334444", "01/30", "111",
            new ConcurrentHashMap<>(Map.of("RUR", new Amount(100000, "RUR"))));
    private final static Transfer VALID_TRANSFER = new Transfer(
            "1111222233334444", "01/30", "111", "1234567890123456",
            new Amount(50000, "RUR"));
    @Mock
    private TransferRepository transferRepository;
    @Mock
    private CardRepository cardRepository;
    @InjectMocks
    private TransferServiceImpl transferService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(transferService, "verificationCode",
                "0000");
        ReflectionTestUtils.setField(transferService, "transferCommission",
                0.01d);
    }

    @Test
    public void testConfirmWhenValidOperationThenReturnTrue() throws InvalidConfirmationDataException {
        OperationConfirmation validConfirmation = new OperationConfirmation("0000", "1");

        Mockito.when(transferRepository.confirmOperation(validConfirmation)).thenReturn(true);
        Mockito.when(transferRepository.getTransferById(validConfirmation.getOperationId()))
                .thenReturn(VALID_TRANSFER);
        Mockito.when(cardRepository.getCardByNumber(VALID_TRANSFER.getCardFromNumber()))
                .thenReturn(Optional.of(VALID_CARD));

        String operationId = transferService.confirm(validConfirmation).getOperationId();
        Assertions.assertEquals("1", operationId);
    }

    @Test
    public void testConfirmWhenInvalidCodeConfirmationThenThrowEx() {
        OperationConfirmation invalidCodeConfirmation = new OperationConfirmation("1111", "1");

        Exception ex = Assertions.assertThrows(InvalidConfirmationDataException.class,
                () -> transferService.confirm(invalidCodeConfirmation));

        Truth.assertThat(ex).hasMessageThat().contains("Неверный код подтверждения");
    }

    @Test
    public void testConfirmWhenInvalidOperationConfirmationThenThrowEx() {
        OperationConfirmation invalidOperationConfirmation = new OperationConfirmation("0000", "99");

        Mockito.when(transferRepository.confirmOperation(invalidOperationConfirmation)).thenReturn(false);

        Exception ex = Assertions.assertThrows(InvalidConfirmationDataException.class,
                () -> transferService.confirm(invalidOperationConfirmation));

        Truth.assertThat(ex).hasMessageThat().contains("Нет операции с id");
    }
}

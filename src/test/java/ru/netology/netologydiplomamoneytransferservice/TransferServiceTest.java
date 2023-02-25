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
import ru.netology.netologydiplomamoneytransferservice.api.dto.Transfer;
import ru.netology.netologydiplomamoneytransferservice.api.error.InvalidCardDataException;
import ru.netology.netologydiplomamoneytransferservice.repository.CardRepository;
import ru.netology.netologydiplomamoneytransferservice.repository.TransferRepository;
import ru.netology.netologydiplomamoneytransferservice.service.TransferServiceImpl;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TransferServiceTest {

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
        ReflectionTestUtils.setField(transferService, "verificationCode", "0000");
        ReflectionTestUtils.setField(transferService, "transferCommission", 0.01d);
    }

    @Test
    public void testTransferWhenValidTransferThenReturnOperationId() throws InvalidCardDataException {
        Mockito.when(transferRepository.addTransfer(VALID_TRANSFER)).thenReturn(1L);
        Mockito.when(cardRepository.getCardByNumber(VALID_TRANSFER.getCardFromNumber()))
                .thenReturn(Optional.of(VALID_CARD));

        String operationId = transferService.transfer(VALID_TRANSFER).getOperationId();

        Assertions.assertEquals("1", operationId);
    }

    @Test
    public void testTransferWhenInvalidCardTransferThenThrowEx() throws InvalidCardDataException {
        Transfer invalidCardTransfer = new Transfer(
                "9999888877776666", "01/25", "555", "1234567890123456",
                new Amount(50000, "RUR"));
        String cardFromNumber = invalidCardTransfer.getCardFromNumber();

        Mockito.when(cardRepository.getCardByNumber(invalidCardTransfer.getCardFromNumber()))
                .thenReturn(Optional.empty());

        Exception ex = Assertions.assertThrows(InvalidCardDataException.class,
                () -> transferService.transfer(invalidCardTransfer));

        Truth.assertThat(ex).hasMessageThat().contains("Карта с номером " + cardFromNumber + " не найдена в базе");
    }
}

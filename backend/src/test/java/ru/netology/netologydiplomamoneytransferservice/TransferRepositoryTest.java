package ru.netology.netologydiplomamoneytransferservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import ru.netology.netologydiplomamoneytransferservice.domain.Amount;
import ru.netology.netologydiplomamoneytransferservice.domain.OperationConfirmation;
import ru.netology.netologydiplomamoneytransferservice.domain.Transfer;
import ru.netology.netologydiplomamoneytransferservice.repository.TransferRepositoryImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootTest
public class TransferRepositoryTest {

    @InjectMocks
    private TransferRepositoryImpl transferRepository;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(transferRepository, "transfers",
                new ConcurrentHashMap<>(Map.of(
                        1L, new Transfer(
                        "1111222233334444", "01/30", "111", "1234567890123456",
                        new Amount(50000, "RUR")))));
        ReflectionTestUtils.setField(transferRepository, "id", new AtomicLong(1));
    }

    @Test
    public void testAddTransferWhenAddTransferThenReturnCorrectId() {
        Transfer newTransfer = new Transfer("5555666677778888", "12/25", "999",
                "0000111122223333", new Amount(10000, "RUR"));

        Long id = transferRepository.addTransfer(newTransfer);
        Assertions.assertEquals(2, id);
    }

    @Test
    public void testConfirmOperationWhenValidOperationIdThenReturnTrue() {
        OperationConfirmation confirmation = new OperationConfirmation("0000", "1");

        Assertions.assertTrue(transferRepository.confirmOperation(confirmation));
    }

    @Test
    public void testConfirmOperationWhenInvalidOperationIdThenReturnFalse() {
        OperationConfirmation confirmation = new OperationConfirmation("0000", "5");

        Assertions.assertFalse(transferRepository.confirmOperation(confirmation));
    }
}

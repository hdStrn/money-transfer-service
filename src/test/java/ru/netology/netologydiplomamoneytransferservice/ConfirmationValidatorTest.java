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
import ru.netology.netologydiplomamoneytransferservice.api.dto.OperationConfirmation;
import ru.netology.netologydiplomamoneytransferservice.api.error.InvalidConfirmationDataException;
import ru.netology.netologydiplomamoneytransferservice.repository.TransferRepository;
import ru.netology.netologydiplomamoneytransferservice.validation.ConfirmationValidator;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ConfirmationValidatorTest {

    @Mock
    private TransferRepository transferRepository;
    @InjectMocks
    private ConfirmationValidator confirmationValidator;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(confirmationValidator, "verificationCode",
                "0000");
    }

    @Test
    public void testConfirmWhenValidOperationThenReturnTrue() {
        OperationConfirmation validConfirmation = new OperationConfirmation("0000", "1");

        Mockito.when(transferRepository.confirmOperation(validConfirmation)).thenReturn(true);

        Assertions.assertTrue(confirmationValidator.validateConfirmation(validConfirmation));
    }

    @Test
    public void testConfirmWhenInvalidCodeConfirmationThenThrowEx() {
        OperationConfirmation invalidCodeConfirmation = new OperationConfirmation("1111", "1");

        Exception ex = Assertions.assertThrows(InvalidConfirmationDataException.class,
                () -> confirmationValidator.validateConfirmation(invalidCodeConfirmation));

        Truth.assertThat(ex).hasMessageThat().contains("Неверный код подтверждения");
    }

    @Test
    public void testConfirmWhenInvalidOperationConfirmationThenThrowEx() {
        OperationConfirmation invalidOperationConfirmation = new OperationConfirmation("0000", "99");

        Mockito.when(transferRepository.confirmOperation(invalidOperationConfirmation)).thenReturn(false);

        Exception ex = Assertions.assertThrows(InvalidConfirmationDataException.class,
                () -> confirmationValidator.validateConfirmation(invalidOperationConfirmation));

        Truth.assertThat(ex).hasMessageThat().contains("Нет операции с id");
    }
}

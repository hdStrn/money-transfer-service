package ru.netology.netologydiplomamoneytransferservice;

import com.google.common.truth.Truth;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import ru.netology.netologydiplomamoneytransferservice.domain.Amount;
import ru.netology.netologydiplomamoneytransferservice.domain.Card;
import ru.netology.netologydiplomamoneytransferservice.domain.Transfer;
import ru.netology.netologydiplomamoneytransferservice.exception.InvalidCardDataException;
import ru.netology.netologydiplomamoneytransferservice.service.TransferServiceImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TransferServiceCardValidationTest {

    @InjectMocks
    private TransferServiceImpl transferService;
    private final static Card VALID_CARD = new Card("1111222233334444", "01/30", "111",
            new ConcurrentHashMap<>(Map.of("RUR", new Amount(100000, "RUR"))));

    @Test
    public void testValidateCardDataWhenInvalidTillDateThenThrowEx() {
        Transfer invalidTillDateTransfer = new Transfer(
                "1111222233334444", "01/25", "111", "1234567890123456",
                new Amount(40000, "RUR"));

        Exception ex = Assertions.assertThrows(InvalidCardDataException.class,
                () -> transferService.validateCardData(invalidTillDateTransfer, VALID_CARD));

        Truth.assertThat(ex).hasMessageThat().contains("Введены неверные данные карты (срок действия / CVV номер)");
    }

    @Test
    public void testValidateCardDataWhenInvalidCVVThenThrowEx() {
        Transfer invalidCVVTransfer = new Transfer(
                "1111222233334444", "01/30", "222", "1234567890123456",
                new Amount(40000, "RUR"));

        Exception ex = Assertions.assertThrows(InvalidCardDataException.class,
                () -> transferService.validateCardData(invalidCVVTransfer, VALID_CARD));

        Truth.assertThat(ex).hasMessageThat().contains("Введены неверные данные карты (срок действия / CVV номер)");
    }

    @Test
    public void testValidateCardDataWhenInvalidCurrencyThenThrowEx() {
        Transfer invalidCurrencyTransfer = new Transfer(
                "1111222233334444", "01/30", "111", "1234567890123456",
                new Amount(2000, "USD"));
        String transferCurrency = invalidCurrencyTransfer.getAmount().getCurrency();

        Exception ex = Assertions.assertThrows(InvalidCardDataException.class,
                () -> transferService.validateCardData(invalidCurrencyTransfer, VALID_CARD));

        Truth.assertThat(ex).hasMessageThat().contains("На выбранной карте отсутствует счет в валюте " + transferCurrency);
    }

    @Test
    public void testValidateCardDataWhenInvalidAmountThenThrowEx() {
        ReflectionTestUtils.setField(transferService, "transferCommission", 0.01d);
        Transfer invalidAmountTransfer = new Transfer(
                "1111222233334444", "01/30", "111", "1234567890123456",
                new Amount(150000, "RUR"));

        Exception ex = Assertions.assertThrows(InvalidCardDataException.class,
                () -> transferService.validateCardData(invalidAmountTransfer, VALID_CARD));

        Truth.assertThat(ex).hasMessageThat().contains("На выбранной карте недостаточно средств");
    }
}

package ru.netology.netologydiplomamoneytransferservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.netology.netologydiplomamoneytransferservice.api.dto.Amount;
import ru.netology.netologydiplomamoneytransferservice.domain.Card;
import ru.netology.netologydiplomamoneytransferservice.repository.CardRepositoryImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
public class NetologyDiplomaMoneyTransferServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetologyDiplomaMoneyTransferServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner initializeCards(CardRepositoryImpl cardRepository) {
        return args -> {
            Map<String, Card> cards = cardRepository.getCards();

            cards.put("1111222233334444", new Card("1111222233334444", "01/30", "111",
                    new ConcurrentHashMap<>(Map.of("RUR", new Amount(50000, "RUR")))));
            cards.put("9999888877776666", new Card("9999888877776666", "10/25", "222",
                    new ConcurrentHashMap<>(Map.of("EUR", new Amount(100000, "EUR")))));
            cards.put("1234567890123456", new Card("1234567890123456", "05/27", "333",
                    new ConcurrentHashMap<>(Map.of("USD", new Amount(10000, "USD")))));
            cards.put("1122334455667788", new Card("1122334455667788", "03/31", "444",
                    new ConcurrentHashMap<>(Map.of(
                            "RUR", new Amount(150000, "RUR"),
                            "EUR", new Amount(20000, "EUR")))));
            cards.put("9876543210987654", new Card("9876543210987654", "08/24", "555",
                    new ConcurrentHashMap<>(Map.of(
                            "RUR", new Amount(250000, "RUR"),
                            "USD", new Amount(5000, "USD"),
                            "EUR", new Amount(1000, "EUR")))));
        };
    }
}

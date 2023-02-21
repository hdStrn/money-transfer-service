package ru.netology.netologydiplomamoneytransferservice;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.netology.netologydiplomamoneytransferservice.domain.Amount;
import ru.netology.netologydiplomamoneytransferservice.domain.OperationConfirmation;
import ru.netology.netologydiplomamoneytransferservice.domain.Transfer;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransferIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;
    private static final int PORT = 9999;
    @Container
    private static final GenericContainer<?> backend = new GenericContainer<>("backend")
            .withExposedPorts(8080)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(
                            new PortBinding(Ports.Binding.bindPort(PORT), new ExposedPort(8080)))));

    @Test
    @Order(1)
    void testTransferWhenValidTransferThenReturnCorrectOperationId() {
        Transfer validTransfer = new Transfer("1111222233334444", "01/30",
                "111", "1234567890123456", new Amount(40000, "RUR"));

        ResponseEntity<String> backendEntity = restTemplate.postForEntity(
                "http://localhost:" + PORT + "/transfer", validTransfer, String.class);
        String response = backendEntity.getBody();

        Assertions.assertEquals("{\"operationId\":\"1\"}", response);
    }

    @Order(2)
    @Test
    void testTransferWhenInvalidCardTransferThenReturnErrorMessage() {
        Transfer invalidCardTransfer = new Transfer("7456321215568799", "01/30",
                "111", "1234567890123456", new Amount(40000, "RUR"));

        ResponseEntity<String> backendEntity = restTemplate.postForEntity(
                "http://localhost:" + PORT + "/transfer", invalidCardTransfer, String.class);
        String response = backendEntity.getBody();

        Assertions.assertEquals("{\"id\":1,\"message\":\"Карта с номером 7456321215568799 " +
                "не найдена в базе\"}", response);
    }

    @Order(3)
    @Test
    void testConfirmationWhenValidOperationIdThenReturnOperationId() {
        OperationConfirmation validConfirmation = new OperationConfirmation("0000", "1");

        ResponseEntity<String> backendEntity = restTemplate.postForEntity(
                "http://localhost:" + PORT + "/confirmOperation", validConfirmation, String.class);
        String response = backendEntity.getBody();

        Assertions.assertEquals("{\"operationId\":\"1\"}", response);
    }

    @Order(4)
    @Test
    void testConfirmationWhenInvalidCodeThenReturnError() {
        OperationConfirmation invalidCodeConfirmation = new OperationConfirmation("1111", "1");

        ResponseEntity<String> backendEntity = restTemplate.postForEntity(
                "http://localhost:" + PORT + "/confirmOperation", invalidCodeConfirmation, String.class);
        String response = backendEntity.getBody();

        Assertions.assertEquals("{\"id\":2,\"message\":\"Неверный код подтверждения (1111)\"}", response);
    }

    @Order(5)
    @Test
    void testConfirmationWhenInvalidOperationIdThenReturnError() {
        OperationConfirmation invalidOperationIdConfirmation = new OperationConfirmation("0000", "5");

        ResponseEntity<String> backendEntity = restTemplate.postForEntity(
                "http://localhost:" + PORT + "/confirmOperation", invalidOperationIdConfirmation, String.class);
        String response = backendEntity.getBody();

        Assertions.assertEquals("{\"id\":3,\"message\":\"Нет операции с id 5\"}", response);
    }
}

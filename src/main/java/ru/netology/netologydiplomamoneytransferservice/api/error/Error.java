package ru.netology.netologydiplomamoneytransferservice.api.error;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Data
@AllArgsConstructor
public class Error {

    private String id;
    private String message;
}

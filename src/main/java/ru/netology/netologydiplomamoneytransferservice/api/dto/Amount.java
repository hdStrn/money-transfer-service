package ru.netology.netologydiplomamoneytransferservice.api.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class Amount {

    private Integer value;
    private String currency;

    @Override
    public String toString() {
        return value + " " + currency;
    }
}

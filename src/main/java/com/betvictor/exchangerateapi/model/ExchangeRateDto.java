package com.betvictor.exchangerateapi.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@Builder
public class ExchangeRateDto {
    private String from;
    private String to;
    private Double amount;
    private Double result;
}

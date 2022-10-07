package com.betvictor.exchangerateapi.model;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@Builder
public class ExchangeRate {
    private String from;
    private String to;
    private Double ratio;
    private LocalDateTime date = LocalDateTime.now();

    public ExchangeRate invert() {
        return ExchangeRate.builder()
                .to(this.from)
                .from(this.to)
                .ratio(this.ratio == null || Double.valueOf(0l).equals(this.ratio) ? 0 : 1 / ratio)
                .date(this.date)
                .build();
    }
}

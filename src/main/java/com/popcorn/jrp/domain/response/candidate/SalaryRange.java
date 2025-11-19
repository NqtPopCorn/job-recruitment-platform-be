package com.popcorn.jrp.domain.response.candidate;

import java.math.BigDecimal;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalaryRange {
    BigDecimal min;
    BigDecimal max;
    String currency;
    String displayText;
}

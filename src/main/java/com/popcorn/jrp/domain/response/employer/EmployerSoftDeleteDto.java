package com.popcorn.jrp.domain.response.employer;

import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmployerSoftDeleteDto {
    private String id;
    private String name;
    private boolean status;
    private Instant updatedAt;
}

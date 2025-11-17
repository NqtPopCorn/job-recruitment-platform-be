package com.popcorn.jrp.domain.response.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatusStatisticResponse {
    private long total;
    private long activeCount;
    private long lockedCount;
}

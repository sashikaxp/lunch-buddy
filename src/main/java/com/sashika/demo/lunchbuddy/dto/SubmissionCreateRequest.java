package com.sashika.demo.lunchbuddy.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UUID;

public record SubmissionCreateRequest(@NotNull @UUID String sessionId, @NotNull @Size(min = 1,max = 25) String suggestion) {
}

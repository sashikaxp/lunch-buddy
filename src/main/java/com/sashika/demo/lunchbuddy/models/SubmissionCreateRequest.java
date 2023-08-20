package com.sashika.demo.lunchbuddy.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SubmissionCreateRequest(@NotNull String sessionId, @NotNull @Size(max = 25) String suggestion) {
}

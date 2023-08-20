package com.sashika.demo.lunchbuddy.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

public record SessionCreateRequest(@Max(10) @Positive int numOfFriends) {
}

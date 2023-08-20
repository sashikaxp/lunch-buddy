package com.sashika.demo.lunchbuddy.models;

import jakarta.validation.constraints.Max;

public record SessionCreateRequest(@Max(10) int numOfFriends) {
}

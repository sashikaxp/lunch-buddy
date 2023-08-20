package com.sashika.demo.lunchbuddy.dto;

import java.util.List;

public record SessionResponse(List<String> subSessions, String mainSession, List<String> submissions) {
}

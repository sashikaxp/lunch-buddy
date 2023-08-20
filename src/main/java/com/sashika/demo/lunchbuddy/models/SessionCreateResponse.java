package com.sashika.demo.lunchbuddy.models;

import java.util.List;

public record SessionCreateResponse(List<String> subSessions, String mainSession, List<String> submissions) {
}

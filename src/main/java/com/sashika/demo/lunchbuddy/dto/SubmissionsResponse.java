package com.sashika.demo.lunchbuddy.dto;

import java.util.List;

public record SubmissionsResponse(List<String> submissionsSoFar, String mySubmission, String finalSelection) {
}

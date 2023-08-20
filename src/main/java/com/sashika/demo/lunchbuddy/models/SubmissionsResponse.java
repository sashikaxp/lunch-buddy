package com.sashika.demo.lunchbuddy.models;

import java.util.List;

public record SubmissionsResponse(List<String> submissionsSoFar, String mySubmission) {
}

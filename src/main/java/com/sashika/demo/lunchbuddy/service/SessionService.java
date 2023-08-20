package com.sashika.demo.lunchbuddy.service;

import com.sashika.demo.lunchbuddy.dto.SessionCreateRequest;
import com.sashika.demo.lunchbuddy.dto.SessionResponse;
import com.sashika.demo.lunchbuddy.dto.SubmissionCreateRequest;
import com.sashika.demo.lunchbuddy.dto.SubmissionsResponse;
import com.sashika.demo.lunchbuddy.exceptions.InvalidSessionException;

public interface SessionService {
    SessionResponse createNewSession(SessionCreateRequest sessionCreateRequest);

    SessionResponse getSession(String mainSessionId) throws InvalidSessionException;

    SubmissionsResponse closeSession(String mainSessionId) throws InvalidSessionException;

    SubmissionsResponse getSubmissions(String subSession) throws InvalidSessionException;

    SubmissionsResponse createSubmission(SubmissionCreateRequest submissionRequest) throws InvalidSessionException;
}

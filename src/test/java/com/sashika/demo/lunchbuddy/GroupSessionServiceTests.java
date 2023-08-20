package com.sashika.demo.lunchbuddy;

import com.sashika.demo.lunchbuddy.cache.AppCache;
import com.sashika.demo.lunchbuddy.cache.RedisCache;
import com.sashika.demo.lunchbuddy.dto.SessionCreateRequest;
import com.sashika.demo.lunchbuddy.dto.SessionResponse;
import com.sashika.demo.lunchbuddy.dto.SubmissionCreateRequest;
import com.sashika.demo.lunchbuddy.dto.SubmissionsResponse;
import com.sashika.demo.lunchbuddy.exceptions.InvalidSessionException;
import com.sashika.demo.lunchbuddy.service.GroupSessionService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class GroupSessionServiceTests {

    private AppCache appCache = new InMemoryCache();

    GroupSessionService sessionService = new GroupSessionService(appCache);

    @Test
    public void test_successful_create_session(){
        SessionResponse newSession = sessionService.createNewSession(new SessionCreateRequest(5));
        Assertions.assertThat(newSession.subSessions().size()).isEqualTo(5);
        Assertions.assertThat(newSession.mainSession()).isNotNull();
        Assertions.assertThat(newSession.submissions()).isEmpty();
    }

    @Test
    public void test_successful_get_session() throws InvalidSessionException {
       SessionResponse newSession = sessionService.createNewSession(new SessionCreateRequest(2));
        SessionResponse session = sessionService.getSession(newSession.mainSession());
        Assertions.assertThat(session.subSessions().size()).isEqualTo(2);
        Assertions.assertThat(session.submissions()).isEmpty();
    }

    @Test
    public void test_successful_create_submission(){
        SessionResponse newSession = sessionService.createNewSession(new SessionCreateRequest(2));
        List<SubmissionsResponse> submissionsResponses = newSession.subSessions().stream().map(s -> {
            try {
                return sessionService.createSubmission(new SubmissionCreateRequest(s, "SUBMISSION-" + s));
            } catch (InvalidSessionException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        Assertions.assertThat(submissionsResponses.size()).isEqualTo(2);
        Assertions.assertThat(submissionsResponses.get(0).submissionsSoFar().size()).isEqualTo(1);
        Assertions.assertThat(submissionsResponses.get(1).submissionsSoFar().size()).isEqualTo(2);
    }

    @Test
    public void test_successful_close_session() throws InvalidSessionException {
        SessionResponse newSession = sessionService.createNewSession(new SessionCreateRequest(2));
        List<SubmissionsResponse> submissionsResponses = newSession.subSessions().stream().map(s -> {
            try {
                return sessionService.createSubmission(new SubmissionCreateRequest(s, "SUBMISSION-" + s));
            } catch (InvalidSessionException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        SubmissionsResponse close = sessionService.closeSession(newSession.mainSession());
        Assertions.assertThat(close.submissionsSoFar().size()).isEqualTo(2);
        Assertions.assertThat(close.finalSelection()).isNotNull();
        Assertions.assertThat(close.finalSelection()).isIn(submissionsResponses.get(0).mySubmission(), submissionsResponses.get(1).mySubmission());
    }

    @Test
    public void test_un_successful_close_session() throws InvalidSessionException {
        SessionResponse newSession = sessionService.createNewSession(new SessionCreateRequest(2));
        List<SubmissionsResponse> submissionsResponses = newSession.subSessions().stream().map(s -> {
            try {
                return sessionService.createSubmission(new SubmissionCreateRequest(s, "SUBMISSION-" + s));
            } catch (InvalidSessionException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        //Try to close session using a sub session ID. Should throw exception
        Assertions.assertThatThrownBy(() -> sessionService.closeSession(newSession.subSessions().get(0)))
                .isInstanceOf(InvalidSessionException.class)
                        .hasMessage("Invalid or closed session");
    }

    @Test
    public void test_try_submission_on_closed_session() throws InvalidSessionException {
        SessionResponse newSession = sessionService.createNewSession(new SessionCreateRequest(2));
        List<SubmissionsResponse> submissionsResponses = newSession.subSessions().stream().map(s -> {
            try {
                return sessionService.createSubmission(new SubmissionCreateRequest(s, "SUBMISSION-" + s));
            } catch (InvalidSessionException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        SubmissionsResponse close = sessionService.closeSession(newSession.mainSession());

        //Now try submitting another suggestion
        Assertions.assertThatThrownBy(()->sessionService.createSubmission(new SubmissionCreateRequest(newSession.subSessions().get(0),"New suggestion")))
                .isInstanceOf(InvalidSessionException.class).hasMessage("Invalid or closed session");
    }

}

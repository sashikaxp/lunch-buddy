package com.sashika.demo.lunchbuddy.webservice;

import com.sashika.demo.lunchbuddy.dto.SessionCreateRequest;
import com.sashika.demo.lunchbuddy.dto.SessionResponse;
import com.sashika.demo.lunchbuddy.dto.SubmissionCreateRequest;
import com.sashika.demo.lunchbuddy.dto.SubmissionsResponse;
import com.sashika.demo.lunchbuddy.exceptions.InvalidSessionException;
import com.sashika.demo.lunchbuddy.service.GroupSessionService;
import com.sashika.demo.lunchbuddy.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000/", maxAge = 3600)
@RestController()
@RequestMapping("/sessions")
public class RestaurantController {

    SessionService sessionService;

    public RestaurantController(GroupSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public SessionResponse createSession(@Valid @RequestBody SessionCreateRequest sessionCreateRequest){
        return sessionService.createNewSession(sessionCreateRequest);

    }



    @GetMapping(value = "/{mainSessionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getSession(@PathVariable String mainSessionId) throws InvalidSessionException {
        SessionResponse session = sessionService.getSession(mainSessionId);
        return new ResponseEntity<>(session, HttpStatusCode.valueOf(200));
    }

    @DeleteMapping(value = "/{mainSessionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity closeSession(@PathVariable String mainSessionId) throws InvalidSessionException {
        SubmissionsResponse session = sessionService.closeSession(mainSessionId);
        return new ResponseEntity(session, HttpStatusCode.valueOf(200));
    }


    @GetMapping(value = "/submissions/{subSession}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SubmissionsResponse getSubmissions(@PathVariable String subSession) throws InvalidSessionException {
        return sessionService.getSubmissions(subSession);
    }

    @PostMapping(value = "/submissions", produces = MediaType.APPLICATION_JSON_VALUE)
    public SubmissionsResponse createSubmission(@Valid @RequestBody SubmissionCreateRequest submissionRequest) throws InvalidSessionException {
        return sessionService.createSubmission(submissionRequest);
    }

}

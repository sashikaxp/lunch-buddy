package com.sashika.demo.lunchbuddy.rest;

import com.sashika.demo.lunchbuddy.cache.RedisCache;
import com.sashika.demo.lunchbuddy.models.SessionCreateRequest;
import com.sashika.demo.lunchbuddy.models.SessionCreateResponse;
import com.sashika.demo.lunchbuddy.models.SubmissionCreateRequest;
import com.sashika.demo.lunchbuddy.models.SubmissionsResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController()
@RequestMapping("/sessions")
public class RestaurantController {

    private RedisCache redisCache;

    public RestaurantController(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public SessionCreateResponse createSession(@Valid @RequestBody SessionCreateRequest sessionCreateRequest){
        String mainSession = UUID.randomUUID().toString();
        List<String> subSessions = new ArrayList<>(sessionCreateRequest.numOfFriends());
        for (int i = 0; i < sessionCreateRequest.numOfFriends(); i++) {
            String subSession = UUID.randomUUID().toString();
            subSessions.add(subSession);
            //Track which sub sessions are related to main session
            redisCache.addToMembers(mainSession, subSession);

            //Keep track sub sessions reference to the parent session
            redisCache.setFieldValue(subSession, "main-session-ref", mainSession);
        }

        return new SessionCreateResponse(subSessions,mainSession, Collections.EMPTY_LIST);

    }

    @GetMapping(value = "/{mainSessionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SessionCreateResponse getSession(@PathVariable String mainSessionId){
        Set<String> members = redisCache.getMembers(mainSessionId);
        List<String> allSubmissions = members
                .stream()
                .map(member -> redisCache.getFieldValue(member, "my-submission").orElse(""))
                .filter(submission -> !"".equalsIgnoreCase(submission)).toList();
        return new SessionCreateResponse(members.stream().toList(),mainSessionId, allSubmissions);

    }

    @DeleteMapping(value = "/{mainSessionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void closeSession(@PathVariable String mainSessionId){

    }


    @GetMapping(value = "/submissions/{subSession}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SubmissionsResponse getSubmissions(@PathVariable String subSession) throws Exception {
        final String mySubmission = redisCache.getFieldValue(subSession, "my-submission").orElse("");
        final String mainSession = redisCache.getFieldValue(subSession, "main-session-ref").orElseThrow(Exception::new);

        List<String> allSubmissions = redisCache.getMembers(mainSession)
                .stream()
                .map(member -> redisCache.getFieldValue(member, "my-submission").orElse(""))
                .filter(submission -> !"".equalsIgnoreCase(submission)).toList();
        return new SubmissionsResponse(allSubmissions, mySubmission);
    }

    @PostMapping(value = "/submissions", produces = MediaType.APPLICATION_JSON_VALUE)
    public String createSubmission(@Valid @RequestBody SubmissionCreateRequest submissionRequest) throws Exception {
        Optional<String> mainSession = redisCache.getFieldValue(submissionRequest.sessionId(), "main-session-ref");
        if(redisCache.isMember(mainSession.orElseThrow(),submissionRequest.sessionId())){
            redisCache.setFieldValue(submissionRequest.sessionId(), "my-submission",submissionRequest.suggestion());
            return submissionRequest.suggestion();
        } else {
            throw new Exception("Invalid session ID");
        }
    }
}

package com.sashika.demo.lunchbuddy.service;

import com.sashika.demo.lunchbuddy.cache.AppCache;
import com.sashika.demo.lunchbuddy.cache.RedisCache;
import com.sashika.demo.lunchbuddy.dto.SessionCreateRequest;
import com.sashika.demo.lunchbuddy.dto.SessionResponse;
import com.sashika.demo.lunchbuddy.dto.SubmissionCreateRequest;
import com.sashika.demo.lunchbuddy.dto.SubmissionsResponse;
import com.sashika.demo.lunchbuddy.exceptions.InvalidSessionException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupSessionService implements SessionService {
    public static final String MY_SUBMISSION = "my-submission";
    public static final String FINAL_SELECTION = "final-selection";
    public static final String MAIN_SESSION_REF = "main-session-ref";
    private AppCache appCache;
    Random random  = new Random();

    public GroupSessionService(AppCache appCache) {
        this.appCache = appCache;
    }

    /**
     * Creates a new group session according to the values provided in the SessionCreateRequest. If the operation
     * is successful it will return the main sessionId and a set of sub sessionIds.
     * Sub session Ids can then be used by other users to submit their wish against the main session
     * @param sessionCreateRequest
     * @return
     */
    @Override
    public SessionResponse createNewSession(SessionCreateRequest sessionCreateRequest) {
        String mainSession = UUID.randomUUID().toString();
        List<String> subSessions = new ArrayList<>(sessionCreateRequest.numOfFriends());
        for (int i = 0; i < sessionCreateRequest.numOfFriends(); i++) {
            String subSession = UUID.randomUUID().toString();
            subSessions.add(subSession);
            //Track which sub sessions are related to main session
            appCache.addToMembers(mainSession, subSession);

            //Keep track sub sessions reference to the parent session
            appCache.setFieldValue(subSession, MAIN_SESSION_REF, mainSession);
        }

        return new SessionResponse(subSessions, mainSession, Collections.EMPTY_LIST);
    }

    /**
     * Retrieves the group session details if the provided mainSessionId is a valid one
     * @param mainSessionId
     * @return com.sashika.demo.lunchbuddy.dto.SessionResponse
     * @throws InvalidSessionException
     */
    @Override
    public SessionResponse getSession(String mainSessionId) throws InvalidSessionException {
        Set<String> members = appCache.getMembers(mainSessionId);
        if(members == null || members.isEmpty()){
           throw new InvalidSessionException("Invalid or closed session");
        }
        List<String> allSubmissions = members
                .stream()
                .map(member -> appCache.getFieldValue(member, MY_SUBMISSION).orElse(""))
                .filter(submission -> !"".equalsIgnoreCase(submission)).toList();
        return new SessionResponse(members.stream().toList(), mainSessionId, allSubmissions);
    }

    /**
     * Closes an active restaurant selection session identified by the provided mainSessionId. This will also
     * randomly select a restaurant from the user submissions and update the final-selection field of each user's
     * sub session, so that they can see what is the final selection.
     * @param mainSessionId
     * @return
     * @throws InvalidSessionException
     */
    @Override
    public SubmissionsResponse closeSession(String mainSessionId) throws InvalidSessionException {
        Set<String> members = appCache.getMembers(mainSessionId);
        if(members == null || members.isEmpty()){
            throw new InvalidSessionException("Invalid or closed session");
        }else {
            List<String> allSubmissions = members.stream().map(member -> appCache.getFieldValue(member, MY_SUBMISSION).orElse(""))
                    .filter(submission -> !"".equalsIgnoreCase(submission)).toList();
            final String chosen = allSubmissions.get(random.nextInt(allSubmissions.size()));
            members.forEach(m -> {
                appCache.setFieldValue(m, FINAL_SELECTION, chosen);
                //Disconnect sub sessions from main session as no longer needed.
                appCache.setFieldValue(m, MAIN_SESSION_REF, "");
            });
            appCache.removeKey(mainSessionId);
            return new SubmissionsResponse(allSubmissions, "", chosen);
        }
    }

    /**
     * Retrieves the details of the restaurant submissions if the provided sub session is a valid one
     * @param subSession
     * @return
     * @throws InvalidSessionException
     */
    @Override
    public SubmissionsResponse getSubmissions(String subSession) throws InvalidSessionException {
        Map<String, String> allFields = appCache.getAllFields(subSession);
        if (allFields == null || allFields.isEmpty()) {
            throw new InvalidSessionException("Provided session id is invalid");
        }
        final String mySubmission = allFields.get(MY_SUBMISSION);
        final String finalSelection = allFields.get(FINAL_SELECTION);
        final String mainSession = allFields.get(MAIN_SESSION_REF);

        List<String> allSubmissions = appCache.getMembers(mainSession)
                .stream()
                .map(member -> appCache.getFieldValue(member, MY_SUBMISSION).orElse(""))
                .filter(submission -> !"".equalsIgnoreCase(submission)).toList();
        return new SubmissionsResponse(allSubmissions, mySubmission, finalSelection);
    }

    /**
     * Creates a restaurant submission against a valid sub session id
     * @param submissionRequest
     * @return
     * @throws InvalidSessionException
     */
    @Override
    public SubmissionsResponse createSubmission(SubmissionCreateRequest submissionRequest) throws InvalidSessionException {
        Optional<String> mainSession = appCache.getFieldValue(submissionRequest.sessionId(), MAIN_SESSION_REF);
        if(appCache.isMember(mainSession.orElseThrow(),submissionRequest.sessionId())){
            appCache.setFieldValue(submissionRequest.sessionId(), MY_SUBMISSION,submissionRequest.suggestion());
            return getSubmissions(submissionRequest.sessionId());
        } else {
            throw new InvalidSessionException("Invalid or closed session");
        }
    }
}

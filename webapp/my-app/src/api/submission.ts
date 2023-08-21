import {external} from "./client";

const getSubmission = async (subSessionId: string) => {
    const res = await external.get(`http://localhost:8080/sessions/submissions/${subSessionId}`);
    return res.data;
}

const sendSubmission = async (subSessionId: string, submission: string) => {
    const payload = {sessionId: subSessionId, suggestion: submission};
    const res = await external.post(`http://localhost:8080/sessions/submissions`, payload);
    return res.data;
}

export default {
    getSubmission,
    sendSubmission,
}
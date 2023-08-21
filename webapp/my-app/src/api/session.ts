import {external} from "./client";

const createSession = async (numOfFriends: string) =>{
    const res = await external.post('http://localhost:8080/sessions', {numOfFriends: numOfFriends});
    return res.data;
}

const getSession = async (mainSessionId: string) =>{
    const res = await external.get(`http://localhost:8080/sessions/${mainSessionId}`);
    return res.data;
}

const closeSession = async (mainSessionId: string) => {
    const res = await external.delete(`http://localhost:8080/sessions/${mainSessionId}`);
}

export default {
    createSession,
    getSession,
    closeSession,
}
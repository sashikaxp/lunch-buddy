
import sessionApi from "../../api/session";
import {sessionResponse, sessionResType, submissionResponse, submissionResType} from "../../cutom-types";
import React from "react";
import Box from "@mui/material/Box";
import {Alert, Button, Chip, List, ListItem, ListItemText, Snackbar, Stack, TextField, Typography} from "@mui/material";

const CreateSession = () => {
    const [sessionId, setSessionId] = React.useState('');
    const [friends, setFriends] = React.useState('5');
    const [session, setSession] = React.useState<sessionResType>();
    const [submission, setSubmission] = React.useState<submissionResType>();
    const [open, setOpen] = React.useState(false);
    const [error, setError] = React.useState('');

    const handleClose = (event?: React.SyntheticEvent | Event, reason?: string) => {
        if (reason === 'clickaway') {
            return;
        }

        setOpen(false);
        setError('');
    };
    const onCreate = () => {
        sessionApi.createSession(friends).then(value => {
            const parse: sessionResType = sessionResponse.parse(value);
            setSession(parse);
        }).catch(reason => {
            setError(reason.message + ','+ reason.response.data);
            setOpen(true);
        });
    }

    const onSee = () => {
        sessionApi.getSession(sessionId).then(value => {
            const parse: sessionResType = sessionResponse.parse(value);
            setSession(parse);
        }).catch(reason => {
            setError(reason.message + ','+ reason.response.data);
            setOpen(true);
        });
    }

    const onClose = () => {
        sessionApi.closeSession(sessionId).then(value => {
            const parse: submissionResType = submissionResponse.parse(value);
            setSubmission(parse);
        }).catch(reason => {
            console.log('Error closing',reason);
            setError(reason.message + ','+ reason.response.data);
            setOpen(true);
        });
    }
    const onFriendsChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setFriends(event.target.value);
    };

    const onSessionIdChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setSessionId(event.target.value);
    };

    return (
        <Box m={2}>
            <Box>
                <Typography mb={2} variant={"h6"}>Create Session</Typography>
                <Stack direction="row" spacing={2}>
                    <TextField id="outlined-basic" label="# of friends to invite" variant="outlined" value={friends}
                               onChange={onFriendsChange}/>
                    <Button variant="contained" onClick={onCreate}>Create Session</Button>
                </Stack>
            </Box>
            {session &&(
                <>
                <Typography mt={2} mb={1} variant={"h6"}>Your main session id is {session.mainSession}</Typography>
            <Box mt={2}>
                <Typography mb={1} variant={"h6"}>Send these invite IDs to your friends</Typography>
                <List>
                    {session?.subSessions.map(value => {
                        return <ListItem><ListItemText primary={value} /></ListItem>;
                    })}
                </List>
            </Box>
                </>
                )}
            <Box>
                <Typography mt={2} mb={2} variant={"h6"}>OR Just see what others have suggested</Typography>
                <Stack direction="row" spacing={2}>
                    <TextField id="outlined-basic" label="Your session ID" variant="outlined" value={sessionId}
                               onChange={onSessionIdChange}/>
                    <Button variant="contained" onClick={onSee}>See</Button>
                </Stack>
            </Box>
            <Box>
                <Typography mb={2} variant={"h6"}>Close Session</Typography>
                <Stack direction="row" spacing={2}>
                    <TextField id="outlined-basic" label="Your session ID" variant="outlined" value={sessionId}
                               onChange={onSessionIdChange}/>
                    <Button variant="contained" onClick={onClose}>Close</Button>
                </Stack>
            </Box>
            <Box mt={2}>
                <Typography mb={1} variant={"h6"}>Suggestions so far</Typography>
                <List>
                    {session?.submissions.map(value => {
                        return <ListItem><ListItemText primary={value}/></ListItem>;
                    })}
                </List>
            </Box>
            { submission && (
                <Box mt={2}>
                    <Stack direction="row" spacing={1}>
                        <Typography mt={2} mb={2} variant={"h6"}>Final selection</Typography>
                        {
                            submission?.finalSelection ?(<Chip label={submission.finalSelection} color="success" />):(<Chip label="Not yet finalized" color="warning" />)
                        }
                    </Stack>
                </Box>
            )}
            <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
                <Alert onClose={handleClose} severity="error" sx={{ width: '100%' }}>
                    {error}
                </Alert>
            </Snackbar>
        </Box>
    );
}

export default CreateSession;
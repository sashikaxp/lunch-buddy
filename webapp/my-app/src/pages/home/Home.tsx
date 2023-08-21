import Box from "@mui/material/Box";
import {Button, Chip, Divider, Link, List, ListItem, ListItemText, Stack, TextField, Typography} from "@mui/material";
import React from "react";
import submissionApi from "../../api/submission";
import {submissionResponse, submissionResType} from "../../cutom-types";

const Home = () => {
    const [sessionId, setSessionId] = React.useState('');
    const [suggestion, setSuggestion] = React.useState('');
    const [submission, setSubmission] = React.useState<submissionResType>();

    const onAdd = () => {
        submissionApi.sendSubmission(sessionId,suggestion).then(value => {
            const parse: submissionResType = submissionResponse.parse(value);
            setSubmission(parse);
        });
    }

    const onSee = () => {
        submissionApi.getSubmission(sessionId).then(value => {
            const parse: submissionResType = submissionResponse.parse(value);
            setSubmission(parse);
        });
    }
    const onSessionIdChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setSessionId(event.target.value);
    };
    const onSuggestionChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setSuggestion(event.target.value);
    };
    return (
        <Box m={2}>
            <Box>
                <Typography mb={2} variant={"h6"}>Submit your restaurant suggestion</Typography>
                <Stack direction="row" spacing={2}>
                   <TextField id="outlined-basic" label="Your session ID" variant="outlined" value={sessionId} onChange={onSessionIdChange}/>
                   <TextField id="outlined-basic" label="Your suggestion" variant="outlined" value={suggestion} onChange={onSuggestionChange}/>
                    <Button variant="contained" onClick={onAdd}>Suggest</Button>
                </Stack>
            </Box>
            <Box>
                <Typography mt={2} mb={2} variant={"h6"}>OR Just see what others have suggested</Typography>
                <Stack direction="row" spacing={2}>
                    <TextField id="outlined-basic" label="Your session ID" variant="outlined" value={sessionId} onChange={onSessionIdChange}/>
                    <Button variant="contained" onClick={onSee}>See</Button>
                </Stack>
            </Box>
            <Box mt={2}>
                <Typography mb={1} variant={"h6"}>Suggestions so far</Typography>
                <List>
                    { submission?.submissionsSoFar.map(value => {
                        return <ListItem><ListItemText primary={value} secondary={value=== suggestion? 'This is your submission':''}/></ListItem>;
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

            <Box mt={2}>
                <Stack direction="row" spacing={1}>
                    <Typography mt={2} mb={2} variant={"button"}>OR create your own session and invite others</Typography>
                    <Link href="/create-session">Create session</Link>
                </Stack>
            </Box>

        </Box>
    );
}

export default Home;
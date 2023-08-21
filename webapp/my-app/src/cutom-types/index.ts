import z from 'zod';

export const submissionResponse = z.object({
    submissionsSoFar: z.array(z.string()),
    mySubmission: z.string().nullable(),
    finalSelection: z.string().nullable().optional(),
});

export const sessionResponse = z.object({
    subSessions: z.array(z.string()),
    mainSession: z.string(),
    submissions: z.array(z.string()),
});

export type submissionResType = z.infer<typeof submissionResponse>;
export type sessionResType = z.infer<typeof sessionResponse>;
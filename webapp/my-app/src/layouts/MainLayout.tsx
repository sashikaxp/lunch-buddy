import { FC } from 'react';
import Box from '@mui/material/Box';
import CssBaseline from '@mui/material/CssBaseline';
import {Outlet} from "react-router";


const MainLayout: FC = () => {
    return (
        <Box pt={8}>
            <CssBaseline />
            <Box component="main" sx={{ flexGrow: 1 }}>
                <Outlet />
            </Box>
        </Box>
    );
};

export default MainLayout;
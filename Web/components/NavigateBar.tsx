import { useState } from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import MenuItem from '@mui/material/MenuItem';
import Menu from '@mui/material/Menu';
import { createTheme, ThemeProvider } from '@mui/material/styles';

const NavigateBar = () => {
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);

    const handleMenu = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    const theme = createTheme({
        palette: {
            primary: {
                main: '#7986cb',
            },
        },
    });

    return (
        <ThemeProvider theme={theme}>
            <Box sx={{ flexGrow: 1 }}>
                <AppBar position="fixed" >
                    <Toolbar>
                        <Typography variant="h5" component="div" sx={{ flexGrow: 1 }}>
                            <Box sx={{ fontWeight: 'bold', m: 1 }}>シス研名前出る蔵</Box>
                        </Typography>
                        <IconButton
                            size="large"
                            aria-label="account of current user"
                            aria-controls="menu-appbar"
                            aria-haspopup="true"
                            onClick={handleMenu}
                            color="inherit"
                        >
                            <MenuIcon />
                        </IconButton>
                        <Menu
                            id="menu-appbar"
                            anchorEl={anchorEl}
                            anchorOrigin={{
                                vertical: 'top',
                                horizontal: 'right',
                            }}
                            keepMounted
                            transformOrigin={{
                                vertical: 'top',
                                horizontal: 'right',
                            }}
                            open={Boolean(anchorEl)}
                            onClose={handleClose}
                        >
                            <MenuItem onClick={handleClose}>TOP</MenuItem>
                            <MenuItem onClick={handleClose}>LOG</MenuItem>
                        </Menu>
                    </Toolbar>
                </AppBar>
            </Box>
        </ThemeProvider>
    );
};
export default NavigateBar;

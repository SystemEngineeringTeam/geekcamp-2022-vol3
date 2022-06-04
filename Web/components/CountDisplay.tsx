import type { NextPage } from 'next';
import axios from 'axios';
import { useState, useEffect } from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import styled from '@emotion/styled'

const CountDisplay: NextPage = () => {
    const [getUsers, setUsers] = useState();
    useEffect(() => {
        const getUser = async () => {
            const users = await axios.get('/api/status');
            console.log(users.data);
        };
        getUser();
    }, []);

    return (
        <>
            <Container fixed>
                <Card sx={{ minWidth: 275 }}>
                    <CardContent>
                        <Typography variant="h5" component="div">
                            現在: 0人
                        </Typography>
                    </CardContent>
                </Card>
            </Container>
        </>
    );
}

export default CountDisplay;

import type { NextPage } from 'next';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import { styled } from '@mui/material/styles';
import Avatar from '@mui/material/Avatar';
const Item = styled(Paper)(({ theme }) => ({
    backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#7986CB',
    ...theme.typography.body2,
    padding: theme.spacing(1),
    textAlign: 'center',
}));

const UserDisplay: NextPage = () => {

    return (
        <>
            <section className="card-list">
                <div className='card-list-top'>
                    リスト
                </div>
                <Box className="Bot-chip" sx={{ width: '60%' }}>
                    <Stack direction="column"
                        justifyContent="flex-start"

                        spacing={1}>
                        <Item className='Item'>
                            <Avatar className="Avatar-chip" sx={{ width: 30, height: 30 }} src="https://avatars.githubusercontent.com/u/26848713?v=4" />
                            <div className='Item-chip'>hiumikan</div>
                        </Item>
                        <Item className='Item'>
                            <Avatar className="Avatar-chip" sx={{ width: 30, height: 30 }} src="https://avatars.githubusercontent.com/u/26848713?v=4" />
                            <div className='Item-chip'>hiumikan</div>
                        </Item>
                    </Stack>
                </Box>
            </section>
        </>
    );
}

export default UserDisplay;

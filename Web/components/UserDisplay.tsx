import type { NextPage } from 'next';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import { styled } from '@mui/material/styles';
import Avatar from '@mui/material/Avatar';
import { userListState, userIconListState } from 'pages';
import { useRecoilValue } from 'recoil';
const Item = styled(Paper)(({ theme }) => ({
    backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#7986CB',
    ...theme.typography.body2,
    padding: theme.spacing(1),
    textAlign: 'center',
}));

const UserDisplay: NextPage = () => {

    const getUserList = useRecoilValue(userListState);
    const getUserIconList = useRecoilValue(userIconListState);
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
                        {getUserList.map((user, index) => (
                            <Item className='Item' key={index}>
                                <Avatar className="Avatar-chip" sx={{ width: 30, height: 30 }} src={getUserIconList[index]} />
                                <div className='Item-chip'>{user}</div>
                            </Item>
                        ))}
                    </Stack>
                </Box>
            </section>
        </>
    );
}

export default UserDisplay;

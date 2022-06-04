import type { NextPage } from 'next';
import UserDisplay from './UserDisplay';
import { useRecoilValue } from 'recoil';
import { userListState } from 'pages';


const CountDisplay: NextPage = () => {
    const getUserList = useRecoilValue(userListState);

    return (
        <>
            <section className="card">
                <div className='card-top'>
                    入室状況
                </div>
                <div className="card-content">
                    <h1 className="card-title">現在:</h1>
                    <h1 className="card-count">{getUserList.length}人</h1>
                </div>
            </section>
            <UserDisplay />
        </>
    );
}

export default CountDisplay;

import type { NextPage } from 'next';
import axios from 'axios';
import { useState, useEffect } from 'react';

const CountDisplay: NextPage = () => {
    const [getUsers, setUsers] = useState<number>(0);
    useEffect(() => {
        const getUser = async () => {
            const users = await axios.get('/api/status');
            console.log(users.data);
        };
        getUser();
    }, []);

    return (
        <>
            <section className="card">
                <div className='card-top'>
                    入室状況
                </div>
                <div className="card-content">
                    <h1 className="card-title">現在:</h1>
                    <h1 className="card-count">{getUsers}人</h1>
                </div>
            </section>
            <section className="card">
                <div className='card-top'>
                    リスト
                </div>
            </section>
        </>
    );
}

export default CountDisplay;

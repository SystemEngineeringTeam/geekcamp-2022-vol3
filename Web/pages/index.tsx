import type { NextPage } from 'next';
import axios from 'axios';
import { useState, useEffect } from 'react';
import NavigateBar from "../components/NavigateBar";
import CountDisplay from '../components/CountDisplay';

const Home: NextPage = () => {
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
      <NavigateBar />
      <div className='CountDisplay-margin'>
        <CountDisplay />
      </div>
    </>
  );
}

export default Home;

import type { NextPage } from 'next';
import axios from 'axios';
import { useState, useEffect } from 'react';

const Home: NextPage = () => {
  const [getUsers, setUsers] = useState();
  useEffect(() => {
    const getUser = async () => {
      const users = await axios.get('/api/status');
      console.log(users.data);
      setUsers(users.data.name);
    };
    getUser();
  }, []);

  return (
    <div>
      {getUsers}
    </div>
  );
}

export default Home;

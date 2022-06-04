import type { NextPage } from 'next';
import axios from 'axios';
import { atom, useRecoilState } from 'recoil';
import { useEffect } from 'react';
import NavigateBar from "../components/NavigateBar";
import CountDisplay from '../components/CountDisplay';


export const userListState = atom<string[]>({
  key: 'userListState',
  default: [],
});
export const userIconListState = atom<string[]>({
  key: 'userIconListState',
  default: [],
});



const Home: NextPage = () => {
  useEffect(() => {
    const getUser = async () => {
      const users = await axios.get('/api/status');
      console.log(users.data);
      setUserList(users.data.current_name);
      setUserIconList(users.data.icon_image_urls);
    };
    getUser();
  }, []);

  const [getUserList, setUserList] = useRecoilState(userListState);
  const [getUserIconList, setUserIconList] = useRecoilState(userIconListState);
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

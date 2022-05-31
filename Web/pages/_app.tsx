import '../styles/globals.css'
import type { AppProps } from 'next/app'

import {
  RecoilRoot,
  atom,
  selector,
  useRecoilState,
  useRecoilValue,
} from 'recoil';

function MyApp({ Component }: AppProps) {

  return (
    <RecoilRoot>
      <Component />
    </RecoilRoot>
  );
}

export default MyApp

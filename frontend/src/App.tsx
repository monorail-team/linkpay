import React, { useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

import Home from '@/pages';
import Register from '@/pages/register';
import MyWalletHistory from '@/pages/mywallet';
import Login from '@/pages/login';
import KakaoCallback from '@/pages/login/KakaoCallback';
import CreateCard from '@/pages/createcard';
import LinkWalletList from '@/pages/linkwalletlist';
import LinkCardList from '@/pages/linkcardlist';
import CardDetail from '@/pages/card/id';
import CreateLinkWallet from '@/pages/createlinkwallet';
import OwnedLinkWallet from '@/pages/owned-linkwallet/walletid';
import ParticipatedLinkWallet from '@/pages/participated-linkwallet/walletid';
import ManageLinkWallet from '@/pages/owned-linkwallet/walletid/manage';
import CreateLinkWalletCard from '@/pages/owned-linkwallet/walletid/create';

import { useThemeStore } from '@/store/themeStore';
import PrivateRoute from './components/PrivateRoute';
import Payment from '@/pages/payment';
import ExamplePage from '@/pages/payment/example';
import NfcWrite from 'pages/test/nfcwrite';
import NfcRead from 'pages/test/nfcread';
import { useFcm } from '@/hooks/useFcm';

const App: React.FC = () => {

  const { theme } = useThemeStore();


  useEffect(() => {
    const root = document.documentElement;
    if (theme === 'dark') {
      root.classList.add('dark');
    } else {
      root.classList.remove('dark');
    }
  }, [theme]);

  useFcm();

  return (
    <div className="w-full h-full md:w-[456px] md:h-[820px] mx-auto ">
      <Router>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/callback/login/kakao" element={<KakaoCallback />} />
          <Route path="/nfcwrite" element={<NfcWrite />} />
          <Route path="/nfcread" element={<NfcRead />} />
          <Route element={<PrivateRoute />}>
            <Route path="/" element={<Home />} />
            <Route path="/register" element={<Register />} />
            <Route path="/mywallet" element={<MyWalletHistory />} />
            <Route path="/createcard" element={<CreateCard />} />
            <Route path="/payment" element={<Payment />} />
            <Route path="/example" element={<ExamplePage />} />
            <Route path="/linkwalletlist" element={<LinkWalletList />} />
            <Route path="/createlinkwallet" element={<CreateLinkWallet />} />
            <Route path="/owned-linkwallet/:walletId" element={<OwnedLinkWallet />} />
            <Route path="/participated-linkwallet/:walletId" element={<ParticipatedLinkWallet />} />
            <Route path="/linkcardlist" element={<LinkCardList />} />
            <Route path="/cards/:id" element={<CardDetail />} />
            <Route path="/owned-linkwallet/:walletId/manage" element={<ManageLinkWallet />} />
            <Route path="/owned-linkwallet/:walletId/create" element={<CreateLinkWalletCard />} />
          </Route>
        </Routes>
      </Router>
    </div>
  );
};

export default App;

import React from 'react';
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
import Checkout from '@/pages/tossPayments/checkout';
import ManageLinkWallet from '@/pages/owned-linkwallet/walletid/manage';
import CreateLinkWalletCard from '@/pages/owned-linkwallet/walletid/create';

import { useThemeStore } from '@/store/themeStore';
import PrivateRoute from './components/PrivateRoute';
import Payment from '@/pages/payment';
import ExamplePage from '@/pages/payment/example';
import NfcWrite from 'pages/test/nfcwrite';
import NfcRead from 'pages/test/nfcread';
import { useFcm } from '@/hooks/useFcm';
import CheckoutSuccess from './pages/tossPayments/checkoutSuccess';
import CheckoutFail from './pages/tossPayments/checkoutFail';

const App: React.FC = () => {

  const { theme } = useThemeStore();

  useFcm();

  return (
    <div className="w-full h-screen md:w-[456px] md:h-[820px] mx-auto ">
      <Router>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/callback/login/kakao" element={<KakaoCallback />} />
          <Route path="/nfcwrite" element={<NfcWrite />} />
          <Route path="/nfcread" element={<NfcRead />} />
          <Route element={<PrivateRoute />}>
            <Route path="/" element={<Home />} />
            <Route path="/checkout" element={<Checkout />} />
            <Route path="/checkout/success" element={<CheckoutSuccess />} />
            <Route path="/checkout/fail" element={<CheckoutFail />} />
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

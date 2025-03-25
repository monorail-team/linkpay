import React, { useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

import Home from '@/pages';
import Register from '@/pages/register';
import MyWalletHistory from '@/pages/mywallet';
import Login from '@/pages/login';
import KakaoCallback from '@/pages/login/KakaoCallback';
import CreateCardPage from '@/pages/createcard';
import { useThemeStore } from '@/store/themeStore';
import PrivateRoute from './components/PrivateRoute';

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

  return (
    <div className="w-full h-full md:w-[456px] md:h-[820px] mx-auto border">
      <Router>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/callback/login/kakao" element={<KakaoCallback />} />
          
          <Route element={<PrivateRoute />}>
            <Route path="/" element={<Home />} />
            <Route path="/register" element={<Register />} />
            <Route path="/mywallet" element={<MyWalletHistory />} />
            <Route path="/createcard" element={<CreateCardPage />} />
          </Route>
        </Routes>
      </Router>
    </div>
  );
};

export default App;

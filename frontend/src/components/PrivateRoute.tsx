import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

const PrivateRoute: React.FC = () => {
  const token = sessionStorage.getItem('accessToken');

  // 토큰이 없으면 로그인 페이지로 리다이렉트
  return token ? <Outlet /> : <Navigate to="/login" replace />;
};

export default PrivateRoute;

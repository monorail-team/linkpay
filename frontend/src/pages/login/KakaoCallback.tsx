// src/pages/KakaoCallback.tsx
import React, { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';

const KakaoCallback: React.FC = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  useEffect(() => {
    const code = searchParams.get('code');
    console.log('발급받은 코드=' + code);
    if (!code) {
      return;
    }
    navigate('/');
  }, [searchParams]);

  return (
    <div className="flex justify-center items-center h-screen text-white bg-black">
      로그인 중입니다...
    </div>
  );
};

export default KakaoCallback;

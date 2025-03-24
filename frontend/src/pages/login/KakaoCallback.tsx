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
    // todo 실제 로그인 연결 시, 로그인 된 사용자는 뒤로가기 못하도록 로그인 체크 후 '/' 혹은 만료된 페이지로 보내는 코드 작성
    navigate('/', { replace: true });
  }, [searchParams]);

  return (
    <div className="flex justify-center items-center h-screen text-white bg-black">
      로그인 중입니다...
    </div>
  );
};

export default KakaoCallback;

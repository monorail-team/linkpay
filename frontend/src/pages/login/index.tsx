import React, { useEffect } from 'react';
import kakaoLoginImage from '@/assets/images/kakao-login-medium-wide.png';
import { useNavigate } from 'react-router-dom';

const front_url = process.env.REACT_APP_FRONTEND_URL;
const Login: React.FC = () => {

  const handleKakaoLogin = () => {
    // RestAPI키와 RedirectURI는 외부로 노출되도 상관 없음; 토큰 획득시에 사용하는 ClientSecret만 보안 관리 대상
    const REST_API_KEY = '40857df827a0ec72c7a9345d3e3f3e8b';
    const REDIRECT_URI = `${front_url}/callback/login/kakao`;
    const kakaoAuthURL = `https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}`;

    window.location.href = kakaoAuthURL;
  };

  const navigate = useNavigate();

  useEffect(() => {
    const token = sessionStorage.getItem('accessToken');
    if (token) {
      // 엑세스 토큰이 있으면 로그인 페이지에서 메인 페이지로 이동
      navigate('/', { replace: true });
    }
  }, [navigate]);

  return (
    <div
      className="w-full h-screen md:w-[456px] md:h-[820px] mx-auto border flex flex-col dark:bg-[#3b3838] bg-[#9CA1D7]">
      <div className="flex-1 flex items-center justify-center">
        <h1 className="text-4xl font-bold text-[#E8E8E8]">LinkPay</h1>
      </div>

      {/* 하단 버튼 */}
      <div className="p-4">
        <button className="w-full max-w-xs mx-auto block"
                onClick={() => handleKakaoLogin()}
        >
          <img
            src={kakaoLoginImage}
            alt="카카오로 시작"
            className="w-full object-contain"
          />
        </button>
      </div>
    </div>
  );
};

export default Login;

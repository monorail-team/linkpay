import React, { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import axios from 'axios';

const KakaoCallback: React.FC = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  const base_url = process.env.REACT_APP_API_URL;
  useEffect(() => {
    const code = searchParams.get('code');
    if (!code) {
      return;
    }
    const fetchAccessToken = async () => {
      try {
        // POST 요청 시 body가 필요 없으면 null을 전달
        const response = await axios.post(
          `${base_url}/api/auth/login/kakao?code=${code}`,
          null,
          {
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded',
            },
          }
        );
        const { accessToken } = response.data;
        console.log('Access Token:', accessToken);
        sessionStorage.setItem('accessToken', accessToken);

        const mypageResponse = await axios.get(`${base_url}/api/mypage`, {
          headers: { 'Authorization': `Bearer ${accessToken}` },
        });
        const { email } = mypageResponse.data;
        console.log('Member email:', email);
        sessionStorage.setItem('memberEmail', email);
        navigate('/', { replace: true });
      } catch (error) {
        console.error('로그인 실패:', error);
        // 에러 처리 로직 추가 가능
      }
    };

    fetchAccessToken();
  }, [searchParams, navigate]);

  return (
    <div className="flex justify-center items-center h-screen text-white bg-black">
      로그인 중입니다...
    </div>
  );
};

export default KakaoCallback;

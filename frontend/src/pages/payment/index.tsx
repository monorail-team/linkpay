// src/pages/Payment.tsx
import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import useDoubleBackExit from '@/hooks/useDoubleBackToExit';
import axios from 'axios';
import useNfcScan from '@/hooks/useNfcScan';
import { Card } from '@/model/Card';
import adjustColorBrightness from '@/util/colorset';

const Payment: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const base_url = process.env.REACT_APP_API_URL;

  const cardData: Card = location.state.cardData;
  const paymentToken: string = location.state.paymentToken;
  const initTime: number = 45;
  const guideText: string = '디바이스의 뒷면을 카드 리더기에 대세요.';

  const [timeLeft, setTimeLeft] = useState(initTime);
  const [showGuideModal, setShowGuideModal] = useState(false);
  const [showBackWarning, setShowBackWarning] = useState(false);
  useDoubleBackExit(setShowBackWarning);

  // 타이머 카운트다운
  const [readValue, setReadValue] = useState('');
  useEffect(() => {
    if (timeLeft === 0) {
      navigate('/', { replace: true });
      return;
    }
    const timer = setTimeout(() => setTimeLeft((t) => t - 1), 1000);
    return () => clearTimeout(timer);
  }, [timeLeft, navigate]);

  const backgroundStyle = {  background: `linear-gradient(155deg, ${cardData.cardColor}, ${adjustColorBrightness(cardData.cardColor, -20)} 70%)` };

  useNfcScan({
    onRead: async (data) => {
      console.log('읽기 성공', data);
      setReadValue(data); // TODO: remove
      const txData = JSON.parse(data);
      console.log('txData변환 -> ', txData);
      const token = sessionStorage.getItem('accessToken');
      await axios.post(
        `${base_url}/api/payments`,
        {
          linkCardId: cardData.linkCardId,
          storeId: txData.storeId,
          amount: txData.amount,
          transactionSignature: txData.transactionSignature,
          paymentToken: paymentToken,
        },
        {
          headers: {
            'Content-Type': `application/json`,
            'Authorization': `Bearer ${token}`,
          }
        }
      ).then((response) => {
        console.log('API 요청 성공', response);
        // navigate('/success');
      }).catch((error) => {
        console.error('API 요청 실패', error);
        // navigate('/fail');
      });
    },
    onError: (err) => {
      console.warn('🚫 NFC 오류:', err.message);
    }
  });


  return (
    <div className="flex flex-col items-center justify-center h-screen text-white px-4 relative bg-[#938F99]">
      {/* 카드 */}
      <div
        className="text-center text-black rounded-2xl shadow-lg w-[249px] h-[384px] mb-12 flex flex-col justify-between p-6"
        style={backgroundStyle}>
        <div className="flex flex-col justify-center mt-24 items-start ml-4">
          <p className="text-sm text-gray-700 mb-1 truncate w-full text-left">카드명</p>
          <h2 className="text-2xl font-semibold text-gray-600 truncate w-full text-left">{cardData.cardName}</h2>
        </div>
        <div className="mt-auto text-xs text-gray-500 text-right">만료일 {cardData.expiredAt}</div>
      </div>

      {/* 카운트다운 */}
      <div className="text-center">
        <div className="mx-auto border border-dashed border-white rounded-full w-1/3 px-6 py-2 mb-3 text-lg font-semibold">
          {timeLeft}
        </div>
        <p className="text-sm mb-2 opacity-90">{guideText}</p>
        <button
          onClick={() => setShowGuideModal(true)}
          className="underline text-sm font-medium hover:opacity-80"
        >
          가이드 보기
        </button>
      </div>

      {/* 🔁 경고 메시지 */}
      {showBackWarning && (
        <div className="fixed bottom-10 text-sm text-white bg-black bg-opacity-60 px-5 py-2 rounded-md">
          뒤로가기를 연속 2번 누르면 취소됩니다.
        </div>
      )}

      {/* NFC 읽기 디버깅 */}
      {readValue && <p className="text-green-600 mt-4">✅ 읽음: {readValue}</p>}

      {/* 가이드 모달 */}
      {showGuideModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
          <div className="bg-white text-black p-6 rounded-lg w-80 shadow-xl">
            <h3 className="text-lg font-semibold mb-3">카드 가이드</h3>
            <p className="text-sm leading-relaxed">
              여기에 상세한 가이드 내용을 작성하세요. 카드 리더기에 카드를 가까이 대고 인식이 완료될 때까지 기다리세요.
            </p>
            <button
              className="mt-5 bg-gray-700 text-white px-4 py-2 rounded hover:bg-gray-600"
              onClick={() => setShowGuideModal(false)}
            >
              닫기
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default Payment;

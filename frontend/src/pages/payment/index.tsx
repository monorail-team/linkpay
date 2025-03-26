// src/pages/Payment.tsx
import React, { useEffect, useRef, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

interface PaymentState {
  cardName: string;
  description: string;
  expireDate: string;
  guideText: string;
  initialTime?: number;
}

const Payment: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const state = location.state as PaymentState;

  const {
    cardName,
    description,
    expireDate,
    guideText,
    initialTime = 45
  } = state || {};

  const [timeLeft, setTimeLeft] = useState(initialTime);
  const [showBackWarning, setShowBackWarning] = useState(false);
  const backTimerRef = useRef<NodeJS.Timeout | null>(null);
  const [showGuideModal, setShowGuideModal] = useState(false);

  // 카운트다운
  useEffect(() => {
    if (timeLeft === 0) {
      navigate('/');
      return;
    }
    const timer = setTimeout(() => setTimeLeft((t) => t - 1), 1000);
    return () => clearTimeout(timer);
  }, [timeLeft, navigate]);

  // 뒤로가기 이벤트 핸들러
  useEffect(() => {
    const handlePopState = () => {
      if (!showBackWarning) {
        setShowBackWarning(true);
        backTimerRef.current = setTimeout(() => setShowBackWarning(false), 2000);
      } else {
        navigate('/');
      }
    };

    window.addEventListener('popstate', handlePopState);
    return () => {
      window.removeEventListener('popstate', handlePopState);
      if (backTimerRef.current) clearTimeout(backTimerRef.current);
    };
  }, [navigate, showBackWarning]);

  return (
    <div className="flex flex-col items-center justify-center h-screen bg-[#938F99] text-white px-4 relative">
      {/* 카드 */}
      <div className="bg-[#F5F5F5] text-center text-black px-6 py-8 rounded-2xl shadow-lg w-[249px] h-[384px] mb-12">
        <p className="text-sm text-gray-700 mb-1">{cardName}</p>
        <h2 className="text-2xl font-semibold text-gray-600 mb-4">{description}</h2>
        <p className="text-xs text-right text-gray-500">만료일 {expireDate}</p>
      </div>

      {/* 카운트다운 및 안내 */}
      <div className="text-center">
        <div className="border border-dashed border-white rounded-full px-6 py-2 mb-3 text-lg font-semibold">
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

      {/* 뒤로가기 안내 메시지 */}
      {showBackWarning && (
        <div className="fixed bottom-10 text-sm text-white bg-black bg-opacity-60 px-5 py-2 rounded-md">
          뒤로가기를 2번 누르면 취소됩니다.
        </div>
      )}

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

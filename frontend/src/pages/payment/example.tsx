// src/pages/ExamplePage.tsx
import React from 'react';
import { useNavigate } from 'react-router-dom';

const ExamplePage: React.FC = () => {
  const navigate = useNavigate();

  const goToPayment = () => {
    navigate('/payment', {
      state: {
        cardData: {
          linkCardId: '1',
          cardName: 'A105',
          description: 'Description',
          expireAt: '25.12.23',
          guideText: '디바이스의 뒷면을 카드 리더기에 대세요.',
          initialTime: 45,
          cardColor: '#ff11ff'
        },
        paymentToken: 'WebAuthn 결과로 전달받은 결제 유효성 검증용 토큰'
      }
    });
  };

  return (
    <div className="h-screen flex flex-col justify-center items-center bg-gray-100">
      <h1 className="text-2xl font-bold mb-4">Example Page</h1>
      <button
        onClick={goToPayment}
        className="px-6 py-3 bg-blue-500 text-white rounded-lg"
      >
        결제 화면 이동
      </button>
    </div>
  );
};

export default ExamplePage;

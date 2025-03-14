// src/pages/Home.tsx
import React from 'react';
import Header from '../components/Header';
import Card from '../components/Card';
import FingerprintIcon from '../components/FingerprintIcon';

const Home: React.FC = () => {
  return (
    <div className="flex flex-col h-screen">
      {/* 상단 헤더 */}
      <Header />

     
      <div className="flex overflow-x-auto snap-x snap-mandatory p-4 space-x-4 hide-scrollbar">
    <div className="snap-center flex-shrink-0">
        <Card
        title="카드명"
        description="Description"
        expireDate="25.12.23"
        used={54000}
        limit={100000}
        />
    </div>
    <div className="snap-center flex-shrink-0">
        <Card
        title="다른 카드"
        description="Another Card"
        expireDate="01.01.24"
        used={30000}
        limit={80000}
        />
    </div>
    <div className="snap-center flex-shrink-0">
        <Card
        title="세번째 카드"
        description="Third Card"
        expireDate="12.05.24"
        used={10000}
        limit={50000}
        />
    </div>
    </div>


      {/* my link 섹션 */}
      <div className="flex flex-col flex-1 items-center justify-center">
        <div className="text-2xl font-bold mb-2">₩ 100,000</div>
        <div className="text-sm text-gray-600">/ 500,000</div>
      </div>

      {/* 지문 아이콘 및 결제 문구 */}
      <footer className="flex flex-col items-center p-4">
        <FingerprintIcon width={60} height={60} />
        <div className="mt-2 text-sm text-gray-800">지문으로 결제하세요</div>
      </footer>
    </div>
    
  );
};

export default Home;

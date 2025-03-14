// src/components/LinkCardDashboard.tsx
import React, { useState } from 'react';
import LinkCardTabs from '@/components/LinkCardTabs';

interface LinkCardItem {
  cardName: string;
  description: string;
  userCount: number;
  amount: number; // 사용 금액
  expireDate: string; // '25.12.23' 형식 등
}

const LinkCardDashboard: React.FC = () => {
  // 탭 인덱스: 0=생성한, 1=공유한, 2=받은
  const [activeTab, setActiveTab] = useState(1); // 기본 두 번째 탭 선택 예시

  // 임의 데이터: 실제로는 API나 상태에서 받아온 데이터를 탭별로 분기
  const sampleCards: LinkCardItem[] = [
    {
      cardName: '카드명',
      description: 'Description',
      userCount: 5,
      amount: 58000,
      expireDate: '25.12.23',
    },
    {
      cardName: '카드명',
      description: 'Description',
      userCount: 5,
      amount: 58000,
      expireDate: '25.12.23',
    },
    {
      cardName: '카드명',
      description: 'Description',
      userCount: 5,
      amount: 58000,
      expireDate: '25.12.23',
    },
  ];

  return (
    <div className="relative w-[456px] h-[892px] bg-white shadow-md overflow-hidden">

      {/* 탭 메뉴 */}
      <LinkCardTabs activeTab={activeTab} onTabChange={setActiveTab} />

      {/* 실제 카드 리스트 영역 */}
      <div className="px-4 py-4">
        {sampleCards.map((card, idx) => (
          <div
            key={idx}
            className="bg-gray-100 rounded-xl shadow-sm p-4 mb-4 flex flex-col sm:flex-row sm:justify-between"
          >
            {/* 왼쪽: 카드명 / Description / 사용자 수 */}
            <div className="mb-2 sm:mb-0">
              <div className="text-base font-semibold">{card.cardName}</div>
              <div className="text-sm text-gray-500">{card.description}</div>
              <div className="text-xs text-gray-400 mt-1">
                사용자 수 {card.userCount}명
              </div>
            </div>

            {/* 오른쪽: 사용 금액 / 만료일 */}
            <div className="text-right">
              <div className="text-base font-semibold">
                {card.amount.toLocaleString('ko-KR')}
                <span className="ml-1 text-sm text-gray-600">원</span>
              </div>
              <div className="text-xs text-gray-400 mt-1">
                만료일 {card.expireDate}
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default LinkCardDashboard;

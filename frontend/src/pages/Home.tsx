import React, { useState } from 'react';
import { useSwipeable } from 'react-swipeable';
import Header from '../components/Header';
import Card from '../components/Card';
import Icon from '../components/Icon';

const cards = [
  { title: "카드명", description: "Description", expireDate: "25.12.23", used: 54000, limit: 100000 },
  { title: "다른 카드", description: "Another Card", expireDate: "01.01.24", used: 30000, limit: 80000 },
  { title: "세번째 카드", description: "Third Card", expireDate: "12.05.24", used: 10000, limit: 50000 },
];

const Home: React.FC = () => {
  const [currentIndex, setCurrentIndex] = useState(0);

  const handlers = useSwipeable({
    onSwipedLeft: () => {
      if (currentIndex < cards.length - 1) setCurrentIndex(currentIndex + 1);
    },
    onSwipedRight: () => {
      if (currentIndex > 0) setCurrentIndex(currentIndex - 1);
    },
    trackMouse: true,
  });

  // 상대 단위로 계산 (퍼센트)
  const containerPositionPercent = 11; // (100 - 78) / 2
  const cardTotalWidthPercent = 78 + 3.5; // 카드 너비 78% + gap 3.5% = 81.5%
  const translatePercent = containerPositionPercent - currentIndex * cardTotalWidthPercent;

  return (
    <div>
      <Header />

      {/* 카드 영역 */}
      <div className="relative w-full md:w-[456px] mx-auto overflow-hidden mt-[200px]">
        <div
          {...handlers}
          className="flex transition-transform duration-300"
          style={{ transform: `translateX(${translatePercent}%)` }}
        >
          {cards.map((card, index) => (
            <div 
              key={index} 
              className="flex-shrink-0 mr-[3.5%] last:mr-0 transition-opacity duration-300 w-[78%]"
              style={{ opacity: Math.abs(currentIndex - index) > 1 ? 0.5 : 1 }}
            >
              <Card {...card} />
            </div>
          ))}
        </div>
      </div>

      {/* Link Pay 문구 */}
      <div className="text-center text-lg text-gray-300 font-bold mt-3">Link Pay</div>

      {/* my link 섹션 */}
      <div className="flex flex-col flex-1 items-center justify-center mt-10">
        <div className="text-2xl font-bold mb-2">₩ 100,000</div>
        <div className="text-sm text-gray-600">/ 500,000</div>
      </div>

      {/* 지문 아이콘 및 결제 문구 */}
      <footer className="flex flex-col items-center p-4 mt-10">
        <Icon name="fingerprint" width={60} height={60} />
        <div className="mt-2 text-sm text-gray-800">지문으로 결제하세요</div>
      </footer>
    </div>
  );
};

export default Home;

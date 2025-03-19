import React, { useState } from 'react';
import { useSwipeable } from 'react-swipeable';
import { useNavigate } from 'react-router-dom';
import Header from '../components/Header';
import Card from '../components/Card';
import Icon from '../components/Icon';
import { cards } from '@/mocks/cards';
import { wallets } from '@/mocks/wallets';
import AddLinkCard from '@/components/AddLinkCard';

const Home: React.FC = () => {
  const [currentIndex, setCurrentIndex] = useState(0);
  const totalCardsCount = cards.length + 1;
  const navigate = useNavigate();

  const handlers = useSwipeable({
    onSwipedLeft: () => {
      if (currentIndex < totalCardsCount - 1) setCurrentIndex(currentIndex + 1);
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

  let currentCardType: string | null = null;
  if (currentIndex < cards.length) {
    currentCardType = cards[currentIndex].cardType;
  }
  const walletInfo = wallets.find(wallet => wallet.type === currentCardType);


  // 플러스 카드 클릭 시 호출
  const handleAddLinkClick = () => {
    navigate('/register');
  };
  return (
    <div>
    <Header headerType="menu" onMenuClick={() => console.log('메뉴 클릭')} />
      {/* 카드 영역 */}
      <div className="relative w-full md:w-[456px] mx-auto overflow-hidden mt-[200px]">
        <div
          {...handlers}
          className="flex transition-transform duration-300"
          style={{ transform: `translateX(${translatePercent}%)` }}
        > 
          {/* 실제 카드들을 화면에 표시 */}
          {cards.map((card) => (
            <div 
              key={card.linkCardId} 
              className="flex-shrink-0 mr-[3.5%] last:mr-0 transition-opacity duration-300 w-[78%]"
              style={{ opacity: Math.abs(currentIndex - cards.indexOf(card)) > 1 ? 0.5 : 1 }}
            >
              <Card {...card} />
            </div>
          ))}

          {/* 마지막에 카드 추가 버튼인 플러스카드를 표시 */}
          <div
            className="flex-shrink-0 mr-[3.5%] last:mr-0 transition-opacity duration-300 w-[78%]"
            style={{ opacity: currentIndex === cards.length ? 1 : 0.5 }}
          >
            <AddLinkCard onClick={handleAddLinkClick}/>
          </div>
        </div>
      </div>

      {/* Link Pay 문구 */}
      <div className="text-center text-lg text-gray-300 font-bold mt-3">Link Pay</div>

      {/* my link 섹션 */}
      <div className="flex flex-col flex-1 items-center justify-center mt-10 min-h-[80px]">
        {currentCardType && currentCardType !== 'otherWallet' && walletInfo ? (
          <div className="text-2xl font-medium mb-2 text-center">
             지갑 잔액 {walletInfo.remainingPoints.toLocaleString()} 원
          </div>
        ) : 
        (
        // 다른 사람의 지갑인 경우
          <div className="h-8"></div>
        )}
    </div>

      {/* 지문 아이콘 및 결제 문구 */}
      <footer className="flex flex-col items-center p-4 mt-10 min-h-[120px]">
        <Icon name="fingerprint" width={60} height={60} />
        <div className="mt-2 text-sm text-gray-800">지문으로 결제하세요</div>
      </footer>
    </div>
  );
};

export default Home;

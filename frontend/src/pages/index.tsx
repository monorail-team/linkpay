import React, { useState } from 'react';
import { useSwipeable } from 'react-swipeable';
import { useNavigate } from 'react-router-dom';
import Header from '../components/Header';
import Card from '../components/Card';
import Icon from '../components/Icon';
import { cards } from '@/mocks/cards';
import { wallets } from '@/mocks/wallets';
import AddLinkCard from '@/components/AddLinkCard';
import { useThemeStore } from '@/store/themeStore';

const Home: React.FC = () => {
  const [currentIndex, setCurrentIndex] = useState(0);
  const totalCardsCount = cards.length + 1;
  const navigate = useNavigate();
 const { theme } = useThemeStore();

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
  const containerPositionPercent = 0; // (100 - 78) / 2
  const cardTotalWidthPercent = 103; // 카드 너비 78% + gap 3.5% = 81.5%
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
    <div className='dark:bg-[#3b3838]'>
    <Header headerType="menu" onMenuClick={() => console.log('메뉴 클릭')} />

    <div className="relative w-4/5 max:w-[456px] mx-auto overflow-hidden mt-20 rounded-lg">
      <div className='p-4 
            bg-white dark:bg-[#5a5757]
            border border-gray-200 dark:border-gray-600
            rounded-lg'>
        {/* 카드 영역 */}
          <div
            {...handlers}
            className="flex transition-transform duration-300 mt-[90px]"
            style={{ transform: `translateX(${translatePercent}%)` }}
          > 
            {/* 실제 카드들을 화면에 표시 */}
            {cards.map((card) => (
              <div 
                key={card.linkCardId} 
                className="flex-shrink-0 mr-[3.5%] last:mr-0 transition-opacity duration-300 w-full"
                style={{ opacity: Math.abs(currentIndex - cards.indexOf(card)) > 1 ? 0.5 : 1 }}
              >
                <Card {...card} />
              </div>
            ))}

            {/* 마지막에 카드 추가 버튼인 플러스카드를 표시 */}
            <div
              className="flex-shrink-0 mr-[3.5%] last:mr-0 transition-opacity duration-300 w-full"
              style={{ opacity: currentIndex === cards.length ? 1 : 0.5 }}
            >
              <AddLinkCard onClick={handleAddLinkClick}/>
            </div>
          </div>
            {/* Link Pay 문구 */}
            <div className="text-center text-lg text-gray-300 font-bold mt-3">Link Pay</div>

            {/* my link 섹션 */}
            <div className="flex flex-col flex-1 items-center justify-center mt-10 min-h-[80px]">
              {currentCardType && currentCardType !== 'otherWallet' && walletInfo ? (
                <div className="text-2xl font-medium mb-2 text-center dark:text-white">
                  지갑 잔액 {walletInfo.remainingPoints.toLocaleString()} 원
                </div>
              ) : 
              (
              // 다른 사람의 지갑인 경우
                <div className="h-8"></div>
              )}
            </div>
          </div>
      </div>
      

      {/* 지문 아이콘 및 결제 문구 */}
      <footer className="flex flex-col items-center p-4 mt-10 min-h-[120px]">
      <Icon
            name={theme === 'dark' ? 'fingerprintDarkIcon' : 'fingerprint'}
            width={78}
            height={78}
            alt="메뉴뉴"
          />
        <div className="mt-2 text-sm text-gray-800 dark:text-white">지문으로 결제하세요</div>
      </footer>
    </div>
  );
};

export default Home;

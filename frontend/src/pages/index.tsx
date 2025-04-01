import React, { useState, useEffect } from 'react';
import { useSwipeable } from 'react-swipeable';
import { useNavigate } from 'react-router-dom';
import { useThemeStore } from '@/store/themeStore';
import useWebAuthn from '@/hooks/useWebAuthn';

import Header from '@/components/Header';
import CardComponent from '@/components/Card';
import Icon from '@/components/Icon';
import AddLinkCard from '@/components/AddLinkCard';
import MenuModal from '@/modal/MenuModal';
import { Card } from '@/model/Card';
import axios from 'axios';

const base_url = process.env.REACT_APP_API_URL;

const Home: React.FC = () => {
  const [currentIndex, setCurrentIndex] = useState(0);
  const [cards, setCards] = useState<Card[]>([]);
  const [walletInfo, setWalletInfo] = useState<{ amount: number } | null>(null);
  const navigate = useNavigate();
  const { theme } = useThemeStore();

  const { handleWebAuthn, loading } = useWebAuthn();

  const onFingerprintClick = async () => {
    const assertionResult = await handleWebAuthn();
    if (assertionResult) {
      const selectedCard = cards[currentIndex];
      navigate("/payment", { state: { cardData: selectedCard} });
    } else {
      console.error("assertionResult 생성 실패");
    }
  };

  const [showMenu, setShowMenu] = useState(false);
  
  const handleMenuClick = () => {
    setShowMenu(true);
  };
  
  const handleMenuClose = () => {
    setShowMenu(false);
  };

 
  // 카드 API 호출
  useEffect(() => {
    const fetchCards = async () => {
      try {
        const token = sessionStorage.getItem('accessToken');
        if (!token) {
          console.error('accessToken이 없습니다.');
          return;
        }
        const response = await axios.get(`${base_url}/api/cards/registered`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        const cardsData = response.data.linkCards as Card[];
        setCards(cardsData);
      } catch (error) {
        console.error('카드 정보를 불러오는데 실패했습니다.', error);
      }
    };
    fetchCards();
  }, []);


   // 지갑 API 호출
   useEffect(() => {
    const fetchWallet = async () => {
      try {
        const token = sessionStorage.getItem('accessToken');
        if (!token) {
          console.error('accessToken이 없습니다.');
          return;
        }
        const response = await axios.get(`${base_url}/api/my-wallets`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setWalletInfo(response.data);
      } catch (error) {
        console.error('지갑 정보를 불러오는데 실패했습니다.', error);
      }
    };
    fetchWallet();
  }, []);

  //카드의 개수
  const totalCardsCount = cards.length + 1;

  // 스와이프 함수
  const handlers = useSwipeable({
    onSwipedLeft: () => {
      if (currentIndex < totalCardsCount - 1) setCurrentIndex(currentIndex + 1);
    },
    onSwipedRight: () => {
      if (currentIndex > 0) setCurrentIndex(currentIndex - 1);
    },
    trackMouse: true,
  });

  // 카드 위치 계산
  const containerPositionPercent = 0; // (100 - 78) / 2
  const cardTotalWidthPercent = 110; // 카드 너비 78% + gap 3.5% = 81.5%
  const translatePercent = containerPositionPercent - currentIndex * cardTotalWidthPercent;




  // 플러스 카드 클릭 시 호출
  const handleAddLinkClick = () => {
    navigate('/register');
  };

  return (
    <div className='dark:bg-[#3b3838] overflow-hidden'>
    <Header headerType="menu" onMenuClick={handleMenuClick} />
      {showMenu && <MenuModal onClose={handleMenuClose} />}

    <div className="relative w-4/5 max:w-[456px] mx-auto  mt-20 rounded-lg">
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
                className="flex-shrink-0 mr-[10%] last:mr-0 transition-opacity duration-300 w-full"
                style={{ opacity: Math.abs(currentIndex - cards.indexOf(card)) > 1 ? 0.5 : 1 }}
              >
                <CardComponent {...card} />
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
              {walletInfo?.amount !== undefined ?(
                <div className="text-2xl font-medium mb-2 text-center dark:text-white">
                  지갑 잔액 {walletInfo.amount.toLocaleString()} 원
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
        <button
          onClick={onFingerprintClick}
          disabled={loading}
          className="focus:outline-none"
        >
          <Icon
            name={theme === 'dark' ? 'fingerprintDarkIcon' : 'fingerprintIcon'}
            width={78}
            height={78}
            alt="지문 인증"
          />
        </button>
        {loading && <p className="mt-4 text-white">처리중...</p>}
        <p className="mt-2 text-white">지문 버튼을 눌러 결제를 진행하세요.</p>
      </footer>
    </div>
  );
};

export default Home;

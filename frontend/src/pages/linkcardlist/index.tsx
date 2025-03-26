import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import Header from '@/components/Header';
import LinkCardItem from '@/components/LinkCardItem';
import MenuModal from '@/modal/MenuModal';
import { cards } from '@/mocks/cards';


// 탭 이름을 상수로 관리
const TAB_CREATED = 'created';
const TAB_SHARED = 'shared';
const TAB_RECEIVED = 'received';



const LinkCardListPage: React.FC = () => {
  // 현재 선택된 탭 상태
  const [activeTab, setActiveTab] = useState<string>(TAB_CREATED);

  const [showMenu, setShowMenu] = useState(false);

  const handleMenuClick = () => {
  setShowMenu(true);
  };

  const handleMenuClose = () => {
  setShowMenu(false);
  };


  const navigate = useNavigate();
  // 탭 버튼을 클릭했을 때 호출되는 함수
  const handleTabClick = (tab: string) => {
    setActiveTab(tab);
  };

  const handleCardClick = (cardid: number) => {
    navigate(`/cards/${cardid}`);
  }
  // activeTab에 따라 다른 카드 리스트 반환
  const getCardList = () => {
    switch (activeTab) {
      case TAB_CREATED:
        // 생성한 링크카드 => cardType === 'OWNED'
        return cards.filter((card) => card.cardType === 'OWNED');
      case TAB_SHARED:
        // 공유한 링크카드 => cardType === 'SHARED'
        return cards.filter((card) => card.cardType === 'SHARED');
      case TAB_RECEIVED:
        // 받은 링크카드 => cardType === 'RECEIVED'
        return cards.filter((card) => card.cardType === 'RECEIVED');
      default:
        return [];
    }
  };

  return (
    <div className="w-full h-screen max-w-md mx-auto dark:bg-[#3b3838] flex flex-col">
      {/* 헤더 */}
      <Header headerType="menu" onMenuClick={handleMenuClick} />
      {showMenu && <MenuModal onClose={handleMenuClose} />}

      {/* 탭 영역 */}
      <div className="flex justify-around items-center border-b border-gray-200 dark:border-gray-700 text-sm mx-10">
        <button
          className={`py-3 w-full 
            ${activeTab === TAB_CREATED ? 'text-[#76558F] dark:text-[#D8D5F8] first-letter:font-bold border-b-2 border-[#76558F] dark:border-[#D8D5F8]' : 'text-gray-500 dark:text-white'}`}
          onClick={() => handleTabClick(TAB_CREATED)}
        >
          생성한 링크카드
        </button>
        <button
          className={`py-3 w-full 
            ${activeTab === TAB_SHARED ? 'text-[#76558F] dark:text-[#D8D5F8] font-bold border-b-2 border-[#76558F] dark:border-[#D8D5F8]' : 'text-gray-500 dark:text-white'}`}
          onClick={() => handleTabClick(TAB_SHARED)}
        >
          공유한 링크카드
        </button>
        <button
          className={`py-3 w-full 
            ${activeTab === TAB_RECEIVED ? 'text-[#76558F] dark:text-[#D8D5F8] font-bold border-b-2 border-[#76558F] dark:border-[#D8D5F8]' : 'text-gray-500 dark:text-white'}`}
          onClick={() => handleTabClick(TAB_RECEIVED)}
        >
          받은 링크카드
        </button>
      </div>

      {/* 카드 목록 영역 */}
      <div className="p-4 flex-1 overflow-auto space-y-4">
        {getCardList().map((card) => (
          <div 
            key={card.id}
            className="my-1 box-border rounded-lg w-5/6 p-4 mx-auto bg-center h-[150px]"
            style={{ backgroundColor: card.cardColor }}
            onClick={() => handleCardClick(card.id)}
          >
             <LinkCardItem
                cardName={card.cardName}
                usedpoint={card.usedpoint}
                limitPrice={card.limitPrice}
                expiredAt={card.expiredAt}
                isRegistered={card.isRegistered}
            />
          </div>
        ))}
      </div>
    </div>
  );
};

export default LinkCardListPage;

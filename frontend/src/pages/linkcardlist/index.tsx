import React, { useState, useEffect, useRef, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from '@/components/Header';
import LinkCardItem from '@/components/LinkCardItem';
import MenuModal from '@/modal/MenuModal';
import axios from 'axios';
import { Card } from '@/model/Card';
import { useThemeStore } from '@/store/themeStore';



const base_url = process.env.REACT_APP_API_URL;
const PAGE_SIZE = 10;

const TAB_OWNED  = 'owned';
const TAB_LINKED  = 'linked';
const TAB_SHARED = 'shared';



const LinkCardListPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState<string>(TAB_OWNED);
  const [cards, setCards] = useState<Card[]>([]);
  const [hasNext, setHasNext] = useState<boolean>(true);
  const [lastId, setLastId] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [showMenu, setShowMenu] = useState<boolean>(false);
  const { theme } = useThemeStore();

  console.log(activeTab)
  const navigate = useNavigate();
  const observerRef = useRef<HTMLDivElement>(null);

  const fetchCardsRef = useRef<() => void>(() => {});

  const fetchCards = useCallback(async (resetData = false) => {
    if (loading || (!hasNext && !resetData)) return;

    setLoading(true);

    try {
      const token = sessionStorage.getItem('accessToken');
      let url = `${base_url}/api/cards?type=${activeTab}`;

      const currentLastId = resetData ? null : lastId;
      
      if (currentLastId) {
        url += `&lastId=${lastId}&size=${PAGE_SIZE}`;
      } else {
        url += `&size=${PAGE_SIZE}`;
      }
      const response = await axios.get(url, {
        headers: { 'Authorization': `Bearer ${token}` },
      });
      console.log('API 응답:', response.data);
      
      const newCards: Card[] = response.data.linkCards;

      setCards((prev) => {
        const baseCards = resetData ? [] : prev;
        const allCards = [...baseCards, ...newCards];
        // 중복 제거: 같은 linkCardId가 여러 번 들어가지 않도록 처리
        const uniqueCards = allCards.filter((card, index, self) =>
          index === self.findIndex((c) => c.linkCardId === card.linkCardId)
        );
        return uniqueCards;
      });

      setHasNext(response.data.hasNext);
      if (newCards.length > 0) {
        setLastId(newCards[newCards.length - 1].linkCardId.toString());
      }
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  }, [activeTab, lastId, loading, hasNext]);


  useEffect(() => {
    fetchCardsRef.current = () => fetchCards(false);
  }, [fetchCards]);

  useEffect(() => {
    setCards([]);
    setHasNext(true);
    setLastId(null);
    setTimeout(() => {
      fetchCards(true);
    }, 0);
  }, [activeTab]);

  // 무한 스크롤: IntersectionObserver로 스크롤 끝 감지 시 추가 데이터 호출
  useEffect(() => {
    const currentObserver = observerRef.current;
    if (!currentObserver) return;

    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && hasNext && !loading) {
          fetchCardsRef.current();
        }
      },
      { threshold: 0.5 }
    );

    observer.observe(currentObserver);
    
    return () => {
      if (observerRef.current) {
        observer.unobserve(observerRef.current);
      }
    };
  }, [hasNext, loading]);

  const handleMenuClick = () => {
    setShowMenu(true);
  };

  const handleMenuClose = () => {
    setShowMenu(false);
  };

  // 탭 버튼을 클릭했을 때 호출되는 함수
  const handleTabClick = (tab: string) => {
    setActiveTab(tab);
  };

  const handleCardClick = (cardid: number) => {
    navigate(`/cards/${cardid}`);
  }


  return (
    <div className="w-full h-screen max-w-md mx-auto dark:bg-[#3b3838] flex flex-col">
      {/* 헤더 */}
      <Header headerType="menu" onMenuClick={handleMenuClick} />
      {showMenu && <MenuModal onClose={handleMenuClose} />}

      {/* 탭 영역 */}
      <div className="flex justify-around items-center  text-[16px] mx-10 gap-3" style={{borderBottom:"1px solid #ccc", borderBottomColor:theme=='dark'?"#666":"#ccc"}}>
        <button
          className={`py-3 w-full 
            ${activeTab === TAB_OWNED ? 'text-[#76558F] dark:text-[#D8D5F8] first-letter:font-bold ' : 'text-gray-500 dark:text-white'}`}
          onClick={() => handleTabClick(TAB_OWNED)}
         style={{
      borderBottom: '2px solid',
      borderBottomColor:
        activeTab === TAB_OWNED
          ? theme === 'dark'
            ? '#D8D5F8'
            : '#76558F'
          : 'transparent',
    }}
        >
          내 지갑 카드
        </button>
        <button
          className={`py-3 w-full 
            ${activeTab === TAB_LINKED ? 'text-[#76558F] dark:text-[#D8D5F8] font-bold ' : 'text-gray-500 dark:text-white'}`}
          onClick={() => handleTabClick(TAB_LINKED)}
          style={{
      borderBottom: '2px solid',
      borderBottomColor:
        activeTab === TAB_LINKED
          ? theme === 'dark'
            ? '#D8D5F8'
            : '#76558F'
          : 'transparent',
    }}
        >
          링크 지갑 카드
        </button>
        <button
          className={`py-3 w-full 
            ${activeTab === TAB_SHARED ? 'text-[#76558F] dark:text-[#D8D5F8] font-bold' : 'text-gray-500 dark:text-white'}`}
          onClick={() => handleTabClick(TAB_SHARED)}
          style={{
      borderBottom: '2px solid',
      borderBottomColor:
        activeTab === TAB_SHARED
          ? theme === 'dark'
            ? '#D8D5F8'
            : '#76558F'
          : 'transparent',
    }}
        >
          공유한 카드
        </button>
      </div>

      {/* 카드 목록 영역 */}
      <div className="p-4 flex-1 overflow-auto space-y-4">
        {cards.map((card) => (
          <div 
            key={card.linkCardId}
            className="my-1 box-border rounded-lg w-5/6 mx-auto bg-center h-[150px]"
            style={{ backgroundColor: card.cardColor }}
            onClick={() => handleCardClick(card.linkCardId)}
          >
             <LinkCardItem
                cardName={card.cardName}
                usedPoint={card.usedPoint}
                limitPrice={card.limitPrice}
                expiredAt={card.expiredAt}
                state={card.state}
            />
          </div>
        ))}
        {(cards.length==0)&&
          <div className="text-center text-gray-500 text-[16px]">
            해당하는 링크카드가 존재하지 않습니다.
          </div>
        }
        <div ref={observerRef} />
        {loading && <p className="text-center text-gray-500">불러오는 중...</p>}
      </div>
    </div>
  );
};

export default LinkCardListPage;

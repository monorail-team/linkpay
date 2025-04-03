import React, { useState, useEffect, useRef, useCallback} from 'react';
import Header from '@/components/Header';
import { useNavigate } from 'react-router-dom';
import LinkCardItem from '@/components/LinkCardItem';
import axios from 'axios';
import { Card } from '@/model/Card';

const base_url = process.env.REACT_APP_API_URL;
const PAGE_SIZE = 10;

const Register: React.FC = () => {


  const [selectedIndices, setSelectedIndices] = useState<number[]>([]);
  const [cards, setCards] = useState<Card[]>([]);
  const [hasNext, setHasNext] = useState(true);
  const [lastId, setLastId] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();
  const observerRef = useRef<HTMLDivElement>(null)
  
  
  const fetchCards = useCallback(async () => {
    if (loading || !hasNext) return;
    setLoading(true);
    try {
      const token = sessionStorage.getItem('accessToken');
      let url = `${base_url}/api/cards/unregistered`;
      if (lastId) {
        url += `&lastId=${lastId}&size=${PAGE_SIZE}`;
      } else {
        url += `?size=${PAGE_SIZE}`;
      }
      const response = await axios.get(url, {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });

      const newCards = response.data.linkCards;
      setCards(
        (prev) => 
         {
          const allCards = [...prev, ...newCards];
          const uniqueCards = allCards.filter((card, index, self) =>
            index === self.findIndex((c) => c.linkCardId === card.linkCardId)
          );
          return uniqueCards;
         }
        );
      setHasNext(response.data.hasNext);
      if (newCards.length > 0) {
        // 다음 호출을 위해 마지막 카드의 id 업데이트
        const newLastId = newCards[newCards.length - 1].linkCardId;
        setLastId(newLastId);
      }
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  }, [lastId, loading, hasNext]);

  // 컴포넌트가 마운트되면 카드리스트트 호출
  useEffect(() => {
    fetchCards();
  }, [fetchCards]);

  // IntersectionObserver를 이용해 스크롤이 바닥에 도달하면 다음 페이지를 불러옴
  useEffect(() => {
    const observer = new IntersectionObserver((entries) => {
      if (entries[0].isIntersecting && hasNext && !loading) {
        fetchCards();
      }
    }, { threshold: 1.0 });

    if (observerRef.current) {
      observer.observe(observerRef.current);
    }

    return () => {
      if (observerRef.current) {
        observer.unobserve(observerRef.current);
      }
    };
  }, [fetchCards, hasNext, loading]);

  const handleSelect = (index: number) => {
    setSelectedIndices((prev) => {
      if (prev.includes(index)) {
        return prev.filter((i) => i !== index);
      } else {
        return [...prev, index];
      }
    });
  };

  const handleRegister = async () => {
    if (selectedIndices.length > 0) {
      try {
        const token = sessionStorage.getItem('accessToken');
        await axios.patch(
          `${base_url}/api/cards/activate`,
          { linkCardIds: selectedIndices },
          {
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${token}`,
            },
          }
        );
        alert('카드 등록이 완료되었습니다.');
        navigate('/');
      } catch (error) {
        console.error(error);
        alert('카드 등록 중 에러가 발생했습니다.');
      }
    }
  };



  const handleBackClick = () => {
    navigate('/');
  };

  return (
    <div className="w-full h-full md:w-[456px] md:h-[820px] mx-auto border flex flex-col dark:bg-[#3b3838]">
      {/* Header: 뒤로가기 모드 */}
      <Header headerType="back" onBackClick={handleBackClick} />

      <div className="p-4 flex-1 flex flex-col">
        <h2 className="text-lg font-bold mb-4 text-center dark:text-white">
          등록할 링크 카드를 선택해주세요.
        </h2>

        {/* 카드 목록 */}
         <div className="overflow-y-auto space-y-4 hide-scrollbar  max-h-[75vh]" >
          {cards.map((card) => (
            <label
              key={card.linkCardId}
              className="flex items-center cursor-pointer"
              onClick={() => handleSelect(card.linkCardId)}
            >
              {/* 카드 영역: 선택된 경우 테두리 강조 */}
              <div
                className={`my-1 box-border rounded-lg w-5/6 p-4 mx-auto bg-center h-[150px] ${
                  selectedIndices.includes(card.linkCardId)
                    ? 'outline outline-4 outline-gray-400 brightness-90 dark:outline-white '
                    : ''
                }`}
                style={{ backgroundColor: card.cardColor }}
              >
                 <LinkCardItem
                    cardName={card.cardName}
                    usedPoint={card.usedPoint}
                    limitPrice={card.limitPrice}
                    expiredAt={card.expiredAt}
                    state={card.state}
                  />
              </div>
            </label>
          ))}
          <div ref={observerRef}/>
          {loading && <p className="text-center text-gray-500">불러오는 중...</p>}
        </div>

        {/* 등록하기 버튼 */}
        <button
          className="mt-auto mx-auto w-4/5 py-3 bg-[#9CA1D7] text-white rounded-3xl disabled:bg-gray-300 dark:bg-[#252527] dark:text-white dark:disabled:text-gray-500"
          onClick={handleRegister}
          disabled={selectedIndices.length === 0}
        >
          등록하기
        </button>
      </div>
    </div>
  );
};

export default Register;

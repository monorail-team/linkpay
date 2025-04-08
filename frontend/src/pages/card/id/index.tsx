import React, { useEffect, useState, useCallback, useRef} from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import Header from '@/components/Header';
import CardComponent from '@/components/Card';
import ButtonModal from '@/modal/ButtonModal';
import CardHistoryModal from '@/modal/CardHistoryModal';
import axios from 'axios';
import { Card } from '@/model/Card';
import { CardHistory } from '@/model/CardHistory';
import { formatDateTime } from '@/util/formatdate';



const base_url = process.env.REACT_APP_API_URL;

const CardDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>(); // 카드 id를 URL 파라미터로 받음
  const location = useLocation();
  const initialCardData = location.state as Card | undefined;
  const navigate = useNavigate();

  const [cardData] = useState<Card | null>(initialCardData || null);
  const [cardHistory, setCardHistory] = useState<CardHistory[]>([]);
  const [showDiscardModal, setShowDiscardModal] = useState(false);
  const [lastId, setLastId] = useState<string | null>(null); 
  const [hasNext, setHasNext] = useState<boolean>(true);
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const [selectedHistory, setSelectedHistory] = useState<CardHistory | null>(null);
  const [showHistoryModal, setShowHistoryModal] = useState(false);

  const sentinelRef = useRef<HTMLDivElement | null>(null);

  // 카드 내역 가져오기
  const fetchCardHistory = useCallback(
    async (lastIdParam: string | null) => {
      try {
        const token = sessionStorage.getItem('accessToken');
        if (!token) return;
        setIsLoading(true);
        const params = new URLSearchParams();
        params.append('size', '10');
        if (lastIdParam !== null) {
          params.append('lastId', lastIdParam);
        }

        const url = `${base_url}/api/cards/card-histories/${id}?${params.toString()}`;
        const response = await axios.get(url, {
          headers: { Authorization: `Bearer ${token}` },
        });
        const newHistories: CardHistory[] = response.data.linkCardHistories;
        setCardHistory((prev) => (lastIdParam ? [...prev, ...newHistories] : newHistories));
        setHasNext(response.data.hasNext);
        if (newHistories.length > 0) {
          const lastHistory = newHistories[newHistories.length - 1];
          setLastId(lastHistory.paymentId); // 마지막 카드 내역의 paymentId를 lastId로 설정
        }
      } catch (error) {
        console.error('카드 내역을 불러오는 중 오류 발생', error);
      } finally {
        setIsLoading(false);
      }
    },
    [id]
  );

  useEffect(() => {
    if (id) {
      fetchCardHistory(null);
    }
  }, [id, fetchCardHistory]);

  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && hasNext && !isLoading) {
          fetchCardHistory(lastId);
        }
      },
      {
        root: null,
        rootMargin: '0px',
        threshold: 1.0,
      }
    );
    const currentSentinel = sentinelRef.current;
    if (currentSentinel) {
      observer.observe(currentSentinel);
    }
    return () => {
      if (currentSentinel) {
        observer.unobserve(currentSentinel);
      }
      observer.disconnect();
    };
  }, [hasNext, isLoading, lastId, fetchCardHistory]);

  const handleDiscard = async () => {
    try {
      const token = sessionStorage.getItem('accessToken');
      if (!token) return;
      await axios.delete(`${base_url}/api/cards?linkCardId=${id}`, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/x-www-form-urlencoded',
        },
      });
      alert('카드가 폐기되었습니다.');
      navigate(-1);
    } catch (error) {
      console.error('카드 폐기 실패', error);
      alert('카드 폐기 실패');
    }
  };

  const handleHistoryClick = (history: CardHistory) => {
    setSelectedHistory(history);
    setShowHistoryModal(true);
  };

  return (
    <div className="w-full h-screen max-w-md mx-auto dark:bg-[#3b3838] flex flex-col">
        {/* 헤더: menu 타입 */}
        <Header headerType="menu" onMenuClick={() => {}} />

        {/* 헤더와 카드 사이 오른쪽에 위치한 폐기 버튼 */}
        <div className="flex justify-end mr-11 mt-1">
            <button
            className="
                px-3 py-0.5
                border border-gray-400 
                rounded-lg
                text-gray-800
                dark:text-white
                hover:bg-gray-200 dark:hover:bg-gray-600
                transition mb-2
            "
            onClick={() => setShowDiscardModal(true)}
            >
            폐기
            </button>
        </div>

        {/* 카드 상세 정보 영역 */}
        <div className="w-5/6 h-1/4 mx-auto flex justify-center">
            {cardData ? (
            <CardComponent {...cardData} />
            ) : (
            <p className="text-white">카드 정보를 불러오는 중...</p>
            )}
        </div>

        {/* 사용 내역 영역 */}
        <div className="mt-12 mx-6 flex-1 overflow-auto">
            <h3 className="text-lg text-[#969595] ml-4">사용 내역</h3>
                {cardHistory.length > 0 ? (
                    <ul className="mt-3 mx-5">
                    {cardHistory.map((item, index) => (
                    <li
                        key={index}
                        className="border-b flex justify-between items-center text-lg border-gray-300 dark:border-[#515151] dark:bg-[#3b3838]"
                        onClick={() => handleHistoryClick(item)}
                    >
                        {/* 왼쪽: 사용처 + 날짜 (아래에 작게) */}
                        <div className="flex flex-col">
                        <p className="text-black dark:text-white font-medium">{item.storeName}</p>
                        <p className="text-sm text-gray-500 dark:text-gray-300 mt-1">
                            {formatDateTime(item.time)}
                        </p>
                        </div>
                
                        {/* 오른쪽: 금액 */}
                        <div className="flex flex-col items-end">
                        <p className="text-black dark:text-white">
                            {item.usedPoint.toLocaleString()}원
                        </p>
                        </div>
                    </li>
                    ))}
                </ul>
                ) : (
                <p className="text-black dark:text-white mt-4 mx-4">사용 내역이 없습니다.</p>
                )}
        </div>

        {/* 폐기 확인 모달 */}
        {showDiscardModal && (
            <ButtonModal
             type="confirmAndCancel"
             onClose={() => setShowDiscardModal(false)}
             onConfirm={handleDiscard}
            >
                <h3 className="text-lg font-bold mb-4 text-black dark:text-white">
                카드를 폐기하시겠습니까?
                </h3>
           </ButtonModal>
        )}
         {/* 거래 내역 상세 모달 */}
        <CardHistoryModal
          isOpen={showHistoryModal}
          onClose={() => setShowHistoryModal(false)}
          historyData={selectedHistory}
        />
    </div>
  );
};

export default CardDetailPage;

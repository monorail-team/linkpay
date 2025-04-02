import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Header from '@/components/Header';
import CardComponent from '@/components/Card';
import ButtonModal from '@/modal/ButtonModal';
import axios from 'axios';

interface CardData {
  linkCardId: number;
  cardName: string;
  expiredAt: string;
  usedpoint: number;
  limitPrice: number;
  cardColor: string;
}

interface UsageItem {
  detail: string;
  date: string;
  point: number;
}


// 목 데이터 정의
const mockCardData: CardData = {
    linkCardId: 1,
    cardName: "카드 1",
    expiredAt: "25.12.23",
    usedpoint: 54000,
    limitPrice: 100000,
    cardColor: "#DAD8FC",
  };
  
const mockUsageHistory: UsageItem[] = [
  { detail: "음식 결제", date: "2023.07.01", point: 12000 },
  { detail: "쇼핑 결제", date: "2023.07.03", point: 18000 },
  { detail: "교통비", date: "2023.07.05", point: 6000 },
]
const base_url = process.env.REACT_APP_API_URL;

const CardDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>(); // 카드 id를 URL 파라미터로 받음
  const navigate = useNavigate();

  // 카드 정보와 사용 내역 상태
  const [cardData, setCardData] = useState<CardData | null>(null);
  const [usageHistory, setUsageHistory] = useState<UsageItem[]>([]);
  const [showDiscardModal, setShowDiscardModal] = useState(false);


//   useEffect(() => {
//     const fetchCardDetails = async () => {
//       try {
//         const token = sessionStorage.getItem('accessToken');
//         if (!token) return;
//         const response = await axios.get(`${base_url}/api/cards/${id}`, {
//           headers: { Authorization: `Bearer ${token}` },
//         });
//         // response.data 예시: { card: {...}, usageHistory: [...] }
//         setCardData(response.data.card);
//         setUsageHistory(response.data.usageHistory);
//       } catch (error) {
//         console.error('카드 상세 정보를 불러오는데 실패했습니다.', error);
//       }
//     };

//     if (id) {
//       fetchCardDetails();
//     }
//   }, [id]);

//   // 폐기 API 호출 함수
//   const handleDiscard = async () => {
//     try {
//       const token = sessionStorage.getItem('accessToken');
//       if (!token) return;
//       await axios.delete(`${base_url}/api/cards/${id}`, {
//         headers: { Authorization: `Bearer ${token}` },
//       });
//       alert('카드가 폐기되었습니다.');
//       navigate(-1);
//     } catch (error) {
//       console.error('카드 폐기 실패', error);
//       alert('카드 폐기 실패');
//     }
//   };

// 목 데이터를 활용해 카드 정보와 사용 내역을 설정
useEffect(() => {
    // 실제 API가 준비되면 아래 코드를 axios 호출로 대체
    setTimeout(() => {
      setCardData(mockCardData);
      setUsageHistory(mockUsageHistory);
    }, 500); // 0.5초 후 목 데이터를 적용
  }, [id]);

  // 폐기 버튼을 누른 경우: 목 데이터이므로 실제 API 호출 대신 알림 후 이전 페이지로 이동
  const handleDiscard = async () => {
    // 실제 API 호출 시 axios.delete 등으로 처리
    alert('카드가 폐기되었습니다.');
    navigate(-1);
  };

  return (
    <div className="w-full h-screen max-w-md mx-auto dark:bg-[#3b3838] flex flex-col">
        {/* 헤더: menu 타입 */}
        <Header headerType="menu" onMenuClick={() => {}} />

        {/* 헤더와 카드 사이 오른쪽에 위치한 폐기 버튼 */}
        <div className="flex justify-end mr-16 mt-1">
            <button
            className="
                px-3 py-0.5
                border border-gray-400 
                rounded-lg
                text-gray-800
                dark:text-white
                hover:bg-gray-200 dark:hover:bg-gray-600
                transition
            "
            onClick={() => setShowDiscardModal(true)}
            >
            폐기
            </button>
        </div>

        {/* 카드 상세 정보 영역 */}
        <div className="mt-1 mx-auto">
            {cardData ? (
            <CardComponent {...cardData} />
            ) : (
            <p className="text-white">카드 정보를 불러오는 중...</p>
            )}
        </div>

        {/* 사용 내역 영역 */}
        <div className="mt-12 mx-6 flex-1 overflow-auto">
            <h3 className="text-lg text-[#969595] ml-4">사용 내역</h3>
                {usageHistory.length > 0 ? (
                    <ul className="mt-3 mx-5">
                    {usageHistory.map((item, index) => (
                    <li
                        key={index}
                        className="border-b flex justify-between items-center text-lg border-gray-300 dark:border-[#515151] dark:bg-[#3b3838]"
                    >
                        {/* 왼쪽: 사용처 + 날짜 (아래에 작게) */}
                        <div className="flex flex-col">
                        <p className="text-black dark:text-white font-medium">{item.detail}</p>
                        <p className="text-sm text-gray-500 dark:text-gray-300 mt-1">
                            {item.date}
                        </p>
                        </div>
                
                        {/* 오른쪽: 금액 */}
                        <div className="flex flex-col items-end">
                        <p className="text-black dark:text-white">
                            {item.point.toLocaleString()}원
                        </p>
                        </div>
                    </li>
                    ))}
                </ul>
                ) : (
                <p className="text-white mt-2">사용 내역이 없습니다.</p>
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
    </div>
  );
};

export default CardDetailPage;

import React, { useState, useEffect} from 'react';
import Header from '@/components/Header';
import { useNavigate } from 'react-router-dom';
import LinkCardItem from '@/components/LinkCardItem';
import axios from 'axios';
import { Card } from '@/model/Card';


const Register: React.FC = () => {


  const [selectedIndices, setSelectedIndices] = useState<number[]>([]);
  const [cards, setCards] = useState<Card[]>([]);

  const navigate = useNavigate();

  useEffect(() => {
    const fetchCards = async () => {
      try {
        const token = sessionStorage.getItem('accessToken');
        const response = await axios.get('http://localhost:8080/api/cards/unregistered', {
          headers: {
            'Authorization': `Bearer ${token}`,
          },
        });
        setCards(response.data.linkCards);
      } catch (err) {
        console.error(err);
        
      } 
    };

    fetchCards();
  }, []);

  const handleSelect = (index: number) => {
    setSelectedIndices((prev) => {
      // 이미 선택된 경우 -> 선택 취소
      if (prev.includes(index)) {
        return prev.filter((i) => i !== index);
      } else {
        // 새로 선택
        return [...prev, index];
      }
    });
  };

  const handleRegister = async () => {
    if (selectedIndices.length > 0) {
      try {
        const token = sessionStorage.getItem('accessToken');
        await axios.patch(
          'http://localhost:8080/api/cards/activate',
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
    // 뒤로가기 로직 (예: navigate(-1))
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
              key={card.id}
              className="flex items-center cursor-pointer"
              onClick={() => handleSelect(card.id)}
            >
              {/* 카드 영역: 선택된 경우 테두리 강조 */}
              <div
                className={`my-1 box-border rounded-lg w-5/6 p-4 mx-auto bg-center h-[150px] ${
                  selectedIndices.includes(card.id)
                    ? 'outline outline-4 outline-gray-400 brightness-90 dark:outline-white '
                    : ''
                }`}
                style={{ backgroundColor: card.cardColor }}
              >
                 <LinkCardItem
                    cardName={card.cardName}
                    usedpoint={card.usedpoint}
                    limitPrice={card.limitPrice}
                    expiredAt={card.expiredAt}
                    isRegistered={card.linkedWalletId !== undefined}
                  />
              </div>
            </label>
          ))}
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

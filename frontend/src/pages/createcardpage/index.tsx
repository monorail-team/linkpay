import React, { useState } from 'react';
import Header from '@/components/Header';
import Icon from '@/components/Icon';

const CreateCardPage: React.FC = () => {
  const [cardName, setCardName] = useState('');
  const [cardLimit, setCardLimit] = useState('');
  const [expireDate, setExpireDate] = useState('');
  const [isCalendarOpen, setIsCalendarOpen] = useState(false);

  const handleClearCardName = () => setCardName('');
  const handleClearCardLimit = () => setCardLimit('');
  const toggleCalendar = () => setIsCalendarOpen(prev => !prev);

  // 나중에 달력으로 대체할 선택 함수
  const handleDateSelect = (date: string) => {
    setExpireDate(date);
    setIsCalendarOpen(false);
  };

  return (
    <div className="w-full h-full max-w-md mx-auto border flex flex-col">
      <Header headerType="back" onBackClick={() => console.log('뒤로가기')} />

      <div className="p-4 flex-1 space-y-8 mx-4">
        
        {/* 카드 이름 입력 */}
        <div>
          <span className="text-sm text-gray-600">카드 이름</span>
          <div className="relative">
            <input
              type="text"
              placeholder="카드 이름을 입력하세요."
              value={cardName}
              onChange={(e) => setCardName(e.target.value)}
              className="w-full py-2 pl-0 pr-8 border-b border-gray-300 focus:outline-none focus:ring-0"
            />
            {cardName && (
              <button 
                onClick={handleClearCardName}
                className="absolute right-0 top-1/2 transform -translate-y-1/2 text-gray-500"
              >
                <Icon name="canceltext" width={24} height={24} alt="입력취소" />
              </button>
            )}
          </div>
        </div>

        {/* 카드 한도 입력 */}
        <div>
        <span className="text-sm text-gray-600">카드 한도(결제 가능 총액)</span>
            <div className="relative">
                <input
                type="text"
                placeholder="카드 한도를 입력하세요."
                value={cardLimit}
                onChange={(e) => {
                    // 입력값에서 콤마 제거 후 숫자만 남김
                    let value = e.target.value.replace(/,/g, '').replace(/\D/g, '');
                    if (value === '') {
                    setCardLimit('');
                    return;
                    }
                    let num = Number(value);
                    if (num > 100000000) num = 100000000;
                    // 숫자를 콤마가 찍힌 문자열로 변환
                    setCardLimit(num.toLocaleString());
                }}
                className="w-full py-2 pl-0 pr-8 border-b border-gray-300 focus:outline-none focus:ring-0"
                />
                {cardLimit && (
                <button 
                    onClick={handleClearCardLimit}
                    className="absolute right-0 top-1/2 transform -translate-y-1/2 text-gray-500"
                >
                    <Icon name="canceltext" width={24} height={24} alt="입력취소" />
                </button>
                )}
            </div>
        </div>



        {/* 만료일 입력 */}
        <div>
          <span className="text-sm text-gray-600">만료일</span>
          <div className="relative flex items-center">
            <input
              type="text"
              placeholder="만료일을 선택하세요."
              value={expireDate}
              readOnly
              className="w-full py-2 pl-0 pr-10 border-b border-gray-300 focus:outline-none focus:ring-0 cursor-pointer"
              onClick={toggleCalendar}
            />
            <button 
              onClick={toggleCalendar}
              className="absolute right-0 text-gray-500"
            >
              <Icon name="calander" width={24} height={24} alt="달력 아이콘" />
            </button>
          </div>
        </div>

        {/* 달력 컴포넌트 자리 (외부 컴포넌트로 교체 예정) */}
        {isCalendarOpen && (
          <div className="mt-4 border border-gray-300 rounded-lg p-4">
            {/* <ExternalCalendar onSelect={handleDateSelect} /> */}
            <p className="text-center text-gray-500">달력 컴포넌트 자리 (추후 교체)</p>
            <button
              onClick={() => handleDateSelect('2025-03-30')}
              className="mt-2 block mx-auto px-4 py-2 bg-blue-500 text-white rounded"
            >
              임시 날짜 선택
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default CreateCardPage;

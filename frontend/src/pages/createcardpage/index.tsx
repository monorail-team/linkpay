import React, { useState } from 'react';
import Header from '@/components/Header';
import Icon from '@/components/Icon';
import { DayPicker, getDefaultClassNames } from "react-day-picker";
import "react-day-picker/style.css";
import { useThemeStore } from '@/store/themeStore';

const CreateCardPage: React.FC = () => {
  const [cardName, setCardName] = useState('');
  const [cardLimit, setCardLimit] = useState('');
  const [expireDate, setExpireDate] = useState('');
  const [isCalendarOpen, setIsCalendarOpen] = useState(false);

  const handleClearCardName = () => setCardName('');
  const handleClearCardLimit = () => setCardLimit('');
  const toggleCalendar = () => setIsCalendarOpen(prev => !prev);
  const { theme } = useThemeStore();

  // 나중에 달력으로 대체할 선택 함수
  const handleDateSelect = (date: string) => {
    setExpireDate(date);
    setIsCalendarOpen(false);
  };


  // 버튼 클릭
  const handleRegister = () => {
    console.log('등록하기 클릭', { cardName, cardLimit, expireDate });
  };

  const isCardNameValid = cardName.length <= 10;

  // 모든 필드가 입력되어야 버튼 활성화
  const isFormComplete = cardName && cardLimit && expireDate && isCardNameValid;
  const defaultClassNames = getDefaultClassNames();

  return (
    <div className="w-full h-screen max-w-md mx-auto flex flex-col flex-1 dark:bg-black">
      <Header headerType="menu" onBackClick={() => console.log('뒤로가기')} />
      <div className="p-4 flex-1 space-y-8 mx-4 overflow-auto"> 
        {/* 카드 이름 입력 */}
        <div>
          <span className="text-sm text-gray-600  dark:text-gray-400">카드 이름</span>
          
            <input
              type="text"
              placeholder="카드 이름을 입력하세요."
              value={cardName}
              onChange={(e) => setCardName(e.target.value)}
              className="w-full py-2 pl-0 pr-8 border-b border-gray-300 focus:outline-none focus:ring-0 dark:bg-black dark:text-white dark:placeholder-white"
            />
            {cardName && (
              <button 
                onClick={handleClearCardName}
                className="absolute right-0 top-1/2 transform -translate-y-1/2 text-gray-500"
              > 
                <Icon name={theme === 'dark' ? "canceltextDarkIcon" : "canceltextIcon"} width={24} height={24} alt="입력취소" />
              </button>
            )}
          
          {cardName.length > 10 && (
            <p className="mt-1 text-red-500 text-xs">카드 이름은 10자 내로 입력해 주세요.</p>
          )}
        </div>

        {/* 카드 한도 입력 */}
        <div>
          <span className="text-sm text-gray-600 dark:text-gray-400">카드 한도(결제 가능 총액)</span>
              <div className="relative">
                  <input
                  type="text"
                  placeholder="카드 한도를 입력하세요."
                  value={cardLimit}
                  onChange={(e) => {
                      // 입력값에서 콤마 제거 후 숫자만 남김
                      const Rvalue = e.target.value.replace(/,/g, '').replace(/\D/g, '');
                      if (Rvalue === '') {
                        setCardLimit('');
                        return;
                      }
                      let num = Number(Rvalue);
                      if (num > 100000000) num = 100000000;
                      // 숫자를 콤마가 찍힌 문자열로 변환
                      setCardLimit(num.toLocaleString());
                  }}
                  className="w-full py-2 pl-0 pr-8 border-b border-gray-300 focus:outline-none focus:ring-0 dark:bg-black dark:text-white dark:placeholder-white"
                  />
                  {cardLimit && (
                    <button 
                        onClick={handleClearCardLimit}
                        className="absolute right-0 top-1/2 transform -translate-y-1/2 text-gray-500"
                    >
                      
                      <Icon name={theme === 'dark' ? "canceltextDarkIcon" : "canceltextIcon"} width={24} height={24} alt="입력취소" />
                    </button>
                  )}
              </div>
        </div>



        {/* 만료일 입력 */}
        <div className="relative">
          <span className="text-sm text-gray-600 dark:text-gray-400">만료일</span>
              <div className="relative flex items-center">
                <input
                  type="text"
                  placeholder="만료일을 선택하세요."
                  value={expireDate}
                  readOnly
                  className="w-full py-2 pl-0 pr-10 border-b border-gray-300 focus:outline-none focus:ring-0 cursor-pointer dark:bg-black dark:text-white dark:placeholder-white"
                  onClick={toggleCalendar}
                />
                <button 
                  onClick={toggleCalendar}
                  className="absolute right-0 text-gray-500"
                  style={{ anchorName: "--rdp" } as React.CSSProperties}
                >
                  <Icon name={theme === 'dark' ? 'calendarDarkIcon' : 'calandarIcon'} width={24} height={24} alt="달력 아이콘" />
                </button>
              </div>
        
          {/* 달력 컴포넌트 (daisyUI/DayPicker 적용) */}
          {isCalendarOpen && (
            <div>
              <DayPicker
                mode="single"
                selected={expireDate ? new Date(expireDate) : undefined}
                onSelect={(selectedDate: Date | undefined) => {
                  if (selectedDate) {
                    handleDateSelect(selectedDate.toLocaleDateString());
                  }
                }}
                classNames={{
                  today: 'bg-gray-300 text-white dark:bg-gray-400 rounded-lg', // Add a border to today's date
                  selected: `bg-indigo-500 border-amber-500 text-white rounded-lg`, // Highlight the selected day
                  root: `${defaultClassNames.root} shadow-lg p-5 dark:bg-[#010101] dark:text-white `, // Add a shadow to the root element
                  chevron: `${defaultClassNames.chevron} dark:fill-white` // Change the color of the chevron
                }}
              />                                                
            </div>
          )}
        </div>
      
        
      </div>
      <div className="p-4 mt-auto">
        <button
          className="font-bold block w-4/5 py-3 mx-auto bg-[#9CA1D7] text-white rounded-3xl disabled:bg-gray-300 dark:bg-[#252527] dark:text-white dark:disabled:text-gray-500"
          onClick={handleRegister}
          disabled={!isFormComplete}
        >
          생성하기
        </button>
      </div>
    </div>
  );
};

export default CreateCardPage;

import React, { useState } from 'react';
import Header from '@/components/Header';
import { DayPicker, getDefaultClassNames } from "react-day-picker";
import "react-day-picker/style.css";
import { useThemeStore } from '@/store/themeStore';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { MdOutlineCancel } from "react-icons/md";
import { FaRegCalendar } from "react-icons/fa6";
import CardCreationModal from '@/modal/CardCreationModal';

const CreateCardPage: React.FC = () => {
  const [cardName, setCardName] = useState('');
  const [cardLimit, setCardLimit] = useState('');
  const [expireDate, setExpireDate] = useState('');
  const [isCalendarOpen, setIsCalendarOpen] = useState(false);
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [, setIsSubmitting] = useState(false);
  
  const navigate = useNavigate();
  const handleClearCardName = () => setCardName('');
  const handleClearCardLimit = () => setCardLimit('');
  const toggleCalendar = () => setIsCalendarOpen(prev => !prev);
  const { theme } = useThemeStore();

  const base_url = process.env.REACT_APP_API_URL;
    const CancelIcon = MdOutlineCancel as unknown as (props: React.ComponentProps<'svg'>) => JSX.Element;
    const CalendarIcon = FaRegCalendar as unknown as (props: React.ComponentProps<'svg'>) => JSX.Element;
  
  
  
  // 달력 선택 함수
  const handleDateSelect = (date: string) => {
    setExpireDate(date);
    setIsCalendarOpen(false);
  };
  
  const handleOpenConfirmModal = () => {
    if (cardName && cardLimit && expireDate) {
      setShowConfirmModal(true);
    }
  };

  // 모달의 '확인' 버튼 클릭 시 API 호출
  const handleConfirmCreation = async () => {
    const accessToken = sessionStorage.getItem('accessToken');
    if (!accessToken) return;

    setIsSubmitting(true);

    // 카드 한도 문자열에서 콤마를 제거한 후 숫자로 변환
    const limitPrice = Number(cardLimit.replace(/,/g, ''));
    const dateObj = new Date(expireDate);
    const year = dateObj.getFullYear();
    const month = dateObj.getMonth() + 1;
    const day = dateObj.getDate();

    const payload = {
      cardName,
      limitPrice,
      expiredAt: [year, month, day],
    };

    try {
      const response = await axios.post(`${base_url}/api/cards`, payload, {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${accessToken}`,
        },
      });
      if (response.status === 201) {
        // 카드 생성 성공 시 모달을 닫고 mywallet 페이지로 이동
        setShowConfirmModal(false);
        navigate('/mywallet');
      }
    } catch (error) {
      console.error('Failed to create card:', error);
      alert('카드 생성에 실패했습니다.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const isCardNameValid = cardName.length <= 10;
  const isFormComplete = cardName && cardLimit && expireDate && isCardNameValid;
  const defaultClassNames = getDefaultClassNames();

  return (
    <div className="w-full h-screen max-w-md mx-auto flex flex-col flex-1 dark:bg-[#3b3838]">
      <Header headerType="back" onBackClick={() => navigate("/mywallet")} />
      <div className="p-4 flex-1 space-y-8 mx-4 overflow-auto"> 
        {/* 카드 이름 입력 */}
        <div>
          <span className="text-sm text-gray-600  dark:text-gray-400">카드 이름</span>
            <div className="relative">
              <input
                type="text"
                placeholder="카드 이름을 입력하세요."
                value={cardName}
                onChange={(e) => setCardName(e.target.value)}
                className="text-[20px] w-full py-1  pr-8 border-b border-gray-300 focus:outline-none focus:ring-0 dark:bg-[#3b3838] dark:text-white dark:placeholder-[#ccc]"
                style={{borderBottom:"1px solid #ccc", borderBottomColor:theme==="dark"?"#333":"#ccc"}}
              />
              {cardName && (
                <button 
                  onClick={handleClearCardName}
                  className="absolute right-2 top-1/2 transform -translate-y-1/2 text-gray-500"
                > 
                <CancelIcon style={{width:"24px", height:"24px"}}/>
                </button>
              )}
          {cardName.length > 10 && (
            <p className=" absolute text-red-500 text-xs">카드 이름은 10자 내로 입력해 주세요.</p>
          )}
            </div>
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
                      if (num > 10000000) num = 10000000;
                      // 숫자를 콤마가 찍힌 문자열로 변환
                      setCardLimit(num.toLocaleString());
                  }}
                  className="text-[20px] w-full py-1 pl-0 pr-8 border-b border-gray-300 focus:outline-none focus:ring-0 dark:bg-[#3b3838] dark:text-white dark:placeholder-[#ccc] "
                   style={{borderBottom:"1px solid #ccc", borderBottomColor:theme==="dark"?"#333":"#ccc"}}
                  />
                  {cardLimit && (
                    <button 
                        onClick={handleClearCardLimit}
                        className="absolute right-2 top-1/2 transform -translate-y-1/2 text-gray-500"
                    >
                      
                     <CancelIcon style={{width:"24px", height:"24px"}}/>
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
                  className="text-[20px] w-full py-1 pl-0 pr-10 border-b border-gray-300 focus:outline-none focus:ring-0 cursor-pointer dark:bg-[#3b3838] dark:text-white dark:placeholder-[#ccc]"
                  onClick={toggleCalendar}
                   style={{borderBottom:"1px solid #ccc", borderBottomColor:theme==="dark"?"#333":"#ccc"}}
                />
                <button 
                  onClick={toggleCalendar}
                  className="absolute right-2 -translate-y-1/2  text-gray-500"
                  style={{ anchorName: "--rdp" } as React.CSSProperties}
                >
                  <CalendarIcon style={{width:"24px", height:"24px"}}/>
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
                  today: 'bg-gray-300 text-white dark:bg-gray-400 rounded-lg', 
                  selected: `bg-indigo-500 border-amber-500 text-white rounded-lg`, 
                  root: `${defaultClassNames.root} shadow-lg p-5 dark:bg-[#010101] dark:text-white `,
                  chevron: `${defaultClassNames.chevron} dark:fill-white`
                }}
              />                                                
            </div>
          )}
        </div>
      
        
      </div>
      <div className="p-4 mt-auto">
        <button
          className="font-bold block w-4/5 py-3 mx-auto bg-[#9CA1D7] text-white rounded-3xl disabled:bg-gray-300 dark:bg-[#252527] dark:text-white dark:disabled:text-gray-500"
          onClick={handleOpenConfirmModal}
          disabled={!isFormComplete}
        >
          생성하기
        </button>
      </div>
      {showConfirmModal && (
        <CardCreationModal 
          cardName={cardName}
          cardLimit={Number(cardLimit.replace(/,/g, ''))}
          expiryDate={expireDate}
          onClose={() => setShowConfirmModal(false)}
          onConfirm={handleConfirmCreation}
        />
      )}
    </div>
  );
};

export default CreateCardPage;

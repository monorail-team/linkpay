import React from 'react';
import { CardHistory } from '@/model/CardHistory';
import { format } from 'date-fns';

interface CardHistoryModal {
  isOpen: boolean;
  onClose: () => void;
  historyData: CardHistory | null;
}

function formatDateTime(dateTimeStr: string) {
  return format(new Date(dateTimeStr), "yyyy.MM.dd HH:mm:ss");
}

const CardHistoryModal: React.FC<CardHistoryModal> = ({
  isOpen,
  onClose,
  historyData
}) => {
  if (!isOpen || !historyData) return null;

  return (
    <div className="fixed inset-0 flex items-center justify-center z-50">
      <div 
        className="absolute inset-0 bg-black bg-opacity-50"
        onClick={onClose}
      ></div>
      
      <div className="w-full max-w-md bg-white dark:bg-[#3b3838] rounded-none relative h-full flex flex-col">
        <div className="flex justify-between items-center p-4 border-b dark:border-gray-700">
          <h2 className="text-base dark:text-white mt-2">거래내역상세</h2>
          <button onClick={onClose} className="text-5xl text-gray-600 dark:text-gray-300 mr-2">
            &times;
          </button>
        </div>

        {/* Title */}
        <div className="px-6 py-4">
          <h3 className="text-3xl dark:text-white mb-4">{historyData.storeName}</h3>
          <div className='border-b border-gray-500'></div>
        </div>

        

        {/* Transaction Details */}
        <div className="px-6 py-4 flex-grow">
          <div className="flex justify-between py-2">
            <span className="text-gray-600 dark:text-gray-300">거래일시</span>
            <span className="dark:text-white">{formatDateTime(historyData.time)}</span>
          </div>
          
          <div className="flex justify-between py-2">
            <span className="text-gray-600 dark:text-gray-300">카드명</span>
            <span className="dark:text-white">{historyData.linkCardName}</span>
          </div>
          
          <div className="flex justify-between py-2">
            <span className="text-gray-600 dark:text-gray-300">거래금액</span>
            <span className="dark:text-white">{historyData.usedPoint.toLocaleString()}원</span>
          </div>
          
          <div className="flex justify-between py-2">
            <span className="text-gray-600 dark:text-gray-300">사용자</span>
            <span className="dark:text-white">{historyData.userName}</span>
          </div>
        </div>

        {/* Confirm Button */}
        <div className="mt-auto p-4 flex justify-center">
          <button 
            onClick={onClose}
            className="w-4/5 py-3  bg-[#9CA1D7] text-white dark:bg-[#252527] dark:text-[#B5B5B7]  rounded-full"
          >
            확인
          </button>
        </div>
      </div>
    </div>
  );
};

export default CardHistoryModal;
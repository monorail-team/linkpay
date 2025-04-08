import React from 'react';
import { CardHistory } from '@/model/CardHistory';
import { formatDateTime } from '@/util/formatdate';

interface CardHistoryModal {
  isOpen: boolean;
  onClose: () => void;
  historyData: CardHistory | null;
}

const CardHistoryModal: React.FC<CardHistoryModal> = ({
  isOpen,
  onClose,
  historyData
}) => {
  if (!isOpen || !historyData) return null;

  return (
    <div className="fixed inset-0 flex items-center justify-center z-50">
      {/* Overlay */}
      <div 
        className="absolute inset-0 bg-black bg-opacity-50"
        onClick={onClose}
      ></div>
      
      {/* Modal Content */}
      <div className="w-full max-w-md bg-white dark:bg-[#3b3838] rounded-none relative h-full flex flex-col">
        {/* Header */}
        <div className="flex justify-between items-center p-4 border-b dark:border-gray-700">
          <h2 className="text-xl font-bold dark:text-white">거래내역상세</h2>
          <button onClick={onClose} className="text-3xl text-gray-600 dark:text-gray-300">
            &times;
          </button>
        </div>

        {/* Title */}
        <div className="px-6 py-4">
          <h3 className="text-2xl font-bold dark:text-white">{historyData.storeName}</h3>
        </div>

        {/* Input field (disabled/readonly) */}
        <div className="px-6 pb-2">
          <input
            type="text"
            className="w-full p-3 border rounded-lg dark:bg-gray-700 dark:text-white dark:border-gray-600"
            placeholder="메모입력(최대 00글자)"
            readOnly
          />
        </div>

        {/* Transaction Details */}
        <div className="px-6 py-4 flex-grow">
          <div className="flex justify-between py-2 border-b dark:border-gray-700">
            <span className="text-gray-600 dark:text-gray-300">거래일시</span>
            <span className="dark:text-white">{formatDateTime(historyData.time)}</span>
          </div>
          
          <div className="flex justify-between py-2 border-b dark:border-gray-700">
            <span className="text-gray-600 dark:text-gray-300">카드명</span>
            <span className="dark:text-white">{historyData.linkCardName}</span>
          </div>
          
          <div className="flex justify-between py-2 border-b dark:border-gray-700">
            <span className="text-gray-600 dark:text-gray-300">거래금액</span>
            <span className="dark:text-white">{historyData.usedPoint.toLocaleString()}원</span>
          </div>
          
          <div className="flex justify-between py-2 border-b dark:border-gray-700">
            <span className="text-gray-600 dark:text-gray-300">사용자</span>
            <span className="dark:text-white">{historyData.userName}</span>
          </div>
        </div>

        {/* Confirm Button */}
        <div className="mt-auto p-4">
          <button 
            onClick={onClose}
            className="w-full py-3 bg-blue-400 hover:bg-blue-500 text-white rounded-full"
          >
            확인
          </button>
        </div>

        {/* Bottom Navigation Indicator */}
        <div className="flex justify-center pb-4 pt-2">
          <div className="w-16 h-1 bg-gray-700 rounded-full"></div>
        </div>
      </div>
    </div>
  );
};

export default CardHistoryModal;
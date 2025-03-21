import React from 'react';
import ButtonModal from './ButtonModal';


export interface CardCreationModalProps {
  onClose: () => void;
  onConfirm: () => void;
  cardName: string;
  cardLimit: number;
  expiryDate: string;
}

const CardCreationModal: React.FC<CardCreationModalProps> = ({
                                                               onClose,
                                                               onConfirm,
                                                               cardName,
                                                               cardLimit,
                                                               expiryDate
                                                             }) => {
  return (
    <ButtonModal type="confirmAndCancel" onConfirm={onConfirm} onClose={onClose}>
      {/* 카드 정보 항목 */}
      <div className="mb-2 flex items-center gap-2">
        <label className="text-gray-600 text-sm w-24">카드이름</label>
        <span className="flex-1 p-2 border rounded-md bg-gray-100 text-gray-700 text-left max-w-full">
            {cardName}
          </span>
      </div>

      <div className="mb-2 flex items-center gap-2">
        <label className="text-gray-600 text-sm w-24">개인한도</label>
        <span className="flex-1 p-2 border rounded-md bg-gray-100 text-gray-700 text-left max-w-full">
            {cardLimit.toLocaleString()}원
          </span>
      </div>

      <div className="mb-3 flex items-center gap-2">
        <label className="text-gray-600 text-sm w-24">만료일</label>
        <span className="flex-1 p-2 border rounded-md bg-gray-100 text-gray-700 text-left max-w-full">
            {expiryDate}
          </span>
      </div>

      {/* 안내 문구 */}
      <p className="text-center text-gray-800">카드를 생성하시겠습니까?</p>
    </ButtonModal>
  );
};

export default CardCreationModal;

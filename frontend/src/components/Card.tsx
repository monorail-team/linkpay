import React from 'react';
import {Card as CardModel} from '@/model/Card';


const Card: React.FC<CardModel> = ({ cardName, expireAt, point, limitPrice ,cardColor}) => {
  const backgroundStyle = {
    backgroundColor: cardColor,
  };
  return (
    <div className="relative w-full max-w-[354px] aspect-[354/210] rounded-xl shadow-md p-4 flex flex-col" style={backgroundStyle}>
      {/* 상단: 카드명 & 만료일 */}
      <div className="flex items-center justify-end">
        <div className="text-sm text-gray-600">만료일 {expireAt}</div>
      </div>

      {/* 중앙: 설명 (Description) */}
      <div className="text-3xl text-gray-400 font-medium mb-6">
        {cardName}
      </div>

      {/* 하단: 사용금액 */}
      <div>
        <div className="text-sm text-gray-500 mt-2">사용금액</div>
        <div className="text-3xl font-medium text-gray-800 text-center mt-3 ml-1">
          {point.toLocaleString()}원
          <span className="text-3xl text-gray-500 ml-1">
            / {limitPrice.toLocaleString()}원
          </span>
        </div>
      </div>
    </div>
  );
};

export default Card;

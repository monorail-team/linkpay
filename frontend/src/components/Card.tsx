import React from 'react';
import {Card as CardModel} from '@/model/Card';
import adjustColorBrightness from '@/util/colorset';


const Card: React.FC<CardModel> = ({ cardName, expiredAt, usedPoint, limitPrice ,cardColor}) => {
  const backgroundStyle = {
  background: `linear-gradient(155deg, ${cardColor}, ${adjustColorBrightness(cardColor, -20)} 70%)`
  };
  return (
    <div className="relative w-full max-w-[354px] mx-auto rounded-xl shadow-md p-4  flex flex-col" style={backgroundStyle}>
      {/* 상단: 카드명 & 만료일 */}
      <div className="flex items-center justify-end">
        <div className="text-sm text-gray-600">만료일 {expiredAt.replaceAll("-", ".")}</div>
      </div>

      {/* 중앙: 설명 (Description) */}
      <div className="text-[25px] text-[#555] font-medium mb-5">
        {cardName}
      </div>

      {/* 하단: 사용금액 */}
      <div>
        <div className="text-sm text-[#555] mt-2">사용금액 / 한도</div>
        <div className="text-2xl font-medium text-black text-center mt-1 mb-2">
        { (usedPoint !== undefined ? usedPoint : 0).toLocaleString() }원
          <span className="text-xl text-[#383838] ml-1">
            / {limitPrice.toLocaleString()}원
          </span>
        </div>
      </div>
    </div>
  );
};

export default Card;

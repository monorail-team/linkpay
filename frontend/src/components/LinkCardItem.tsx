// src/components/LinkCardItem.tsx
import React from 'react';
import {Card} from '@/model/Card';
type LinkCardItemProps = Pick<Card, 'cardName' | 'limitPrice' | 'expiredAt'>;

const LinkCardItem: React.FC<LinkCardItemProps> = ({
  cardName,
  limitPrice,
  expiredAt,
}) => {
  return (
    <div className="flex justify-between items-center h-full">
        {/* 왼쪽 그룹: title과 description */}
        <div className="flex flex-col items-start">
          <div className="text-base text-gray-600">{cardName}</div>
        </div>
        
      <div className="w-1/3 flex flex-col ">
        {/* 한도 라벨 (첫 번째 줄) */}
        <div className="flex justify-between">
          <div className="text-xs text-gray-800">한도</div>
          <div className="" /> {/* 오른쪽 빈 공간 */}
        </div>
        {/* 한도 값 (두 번째 줄) */}
        <div className="flex justify-between">
          <div className="" /> {/* 왼쪽 빈 공간 */}
          <div className="text-base text-gray-500">
            {limitPrice.toLocaleString()}원
          </div>
        </div>
        {/* 만료일 라벨 (세 번째 줄) */}
        <div className="flex justify-between mt-2">
          <div className="text-xs text-gray-800">만료일</div>
          <div className="w-1/2" /> {/* 오른쪽 빈 공간 */}
        </div>
        {/* 만료일 값 (네 번째 줄) */}
        <div className="flex justify-between">
          <div className="w-1/2" /> {/* 왼쪽 빈 공간 */}
          <div className="text-sm text-gray-500">{expiredAt}</div>
        </div>
      </div>
    </div>
  );
};

export default LinkCardItem;

// src/components/Card.tsx
import React from 'react';

interface CardProps {
  title: string;         // 예: "카드명"
  description: string;   // 예: "Description"
  expireDate: string;    // 예: "25.12.23"
  used: number;          // 예: 54000
  limit: number;         // 예: 100000
}

const Card: React.FC<CardProps> = ({ title, description, expireDate, used, limit }) => {
  return (
    <div className="relative w-full max-w-[354px] aspect-[354/210] bg-gray-50 rounded-xl shadow-md p-4 flex flex-col">
      {/* 상단: 카드명 & 만료일 */}
      <div className="flex items-center justify-between mb-2">
        <div className="text-sm text-gray-600">{title}</div>
        <div className="text-sm text-gray-600">만료일 {expireDate}</div>
      </div>

      {/* 중앙: 설명 (Description) */}
      <div className="text-base text-gray-700 font-medium mb-4">
        {description}
      </div>

      {/* 하단: 사용금액 */}
      <div>
        <div className="text-sm text-gray-500 mb-1">사용금액</div>
        <div className="text-2xl font-bold text-gray-800">
          {used.toLocaleString()}원
          <span className="text-base text-gray-500 ml-1">
            / {limit.toLocaleString()}원
          </span>
        </div>
      </div>
    </div>
  );
};

export default Card;

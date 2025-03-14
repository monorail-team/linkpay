// src/components/Card.tsx
import React from 'react';
interface CardProps {
  title: string;
  description: string;
  expireDate: string;
  used: number;
  limit: number;
}

const Card: React.FC<CardProps> = ({ title, description, expireDate, used, limit }) => {
  return (
    <div className="w-[300px] h-44 rounded-lg bg-gray-100 mx-2 flex flex-col justify-center p-4">
      <div className="text-sm text-gray-600 mb-2">{title}</div>
      <div className="text-lg font-bold mb-1">{description}</div>
      <div className="text-xs text-gray-500 mb-2">만료일 {expireDate}</div>
      <div className="text-sm text-gray-700">
        사용금액 {used.toLocaleString()}원 / {limit.toLocaleString()}원
      </div>
    </div>
  );
};

export default Card;

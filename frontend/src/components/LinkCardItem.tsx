import React from 'react';
import {Card} from '@/model/Card';
type LinkCardItemProps = Pick<Card, 'cardName' | 'usedPoint' | 'limitPrice' | 'expiredAt'| 'isRegistered'>;
const LinkCardItem: React.FC<LinkCardItemProps> = ({
  cardName,
  usedPoint,
  limitPrice,
  expiredAt,
  isRegistered = "UNREGISTERED",
}) => {
  return (
    <div className="relative w-full h-28">
    {/* (1) 등록된 카드이면 오른쪽 상단에 배지 표시 */}
    {isRegistered === "REGISTERED" && (
      <div className="absolute -top-4 -right-2 bg-purple-500 text-white text-xs font-bold px-2 py-1 rounded-bl">
        등록됨
      </div>
    )}

    {/* (2) 카드명: 왼쪽 수직 중앙 */}
    <div className="absolute left-4 top-1/2 -translate-y-1/2 text-base text-gray-700">
      {cardName}
    </div>

    {/* (3) 사용현황 영역 (오른쪽 상단) */}
    {isRegistered ? (
      // 등록된 카드: "사용한 포인트 / 한도" (문구 + 실제 값 2줄)
      <div className="absolute top-2 right-2 text-sm w-40">
        <div className="flex justify-between">
          <span className="text-gray-600  ml-8">
            사용 포인트 / 한도
          </span>
          
          <span></span>
        </div>
        <div className="flex justify-between mt-1">
          <span></span>
          <span className="text-gray-800 ">
            {usedPoint.toLocaleString()} / {limitPrice.toLocaleString()}
          </span>
        </div>
      </div>
    ) : (
      // 등록되지 않은 카드: 한도만 표시
      <div className="absolute top-2 right-2 text-sm w-32">
        <div className="flex justify-between">
          <span className="text-gray-600 ">한도</span>
          <span></span>
        </div> 
        <div className="flex justify-between mt-1">
          <span></span>
          <span className="text-gray-800 ">
            {limitPrice.toLocaleString()}
          </span>
        </div>
      </div>
    )}

    {/* (4) 만료일 영역 (오른쪽 하단) */}
    <div className="absolute bottom-2 right-2 text-sm w-32">
      <div className="flex justify-between">
        <span className="text-gray-600">만료일</span>
        <span></span>
      </div>
      <div className="flex justify-between mt-1">
        <span></span>
        <span className="text-gray-800">{expiredAt}</span>
      </div>
    </div>
  </div>
  );
};

export default LinkCardItem;

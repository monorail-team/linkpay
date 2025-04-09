import React from 'react';
import {Card} from '@/model/Card';
type LinkCardItemProps = Pick<Card, 'cardName' | 'usedPoint' | 'limitPrice' | 'expiredAt'| 'state'|'username'|'tab'>;
const LinkCardItem: React.FC<LinkCardItemProps> = ({
  cardName,
  usedPoint,
  limitPrice,
  expiredAt,
  state = "UNREGISTERED",
  username,
  tab
}) => {
  return (
    <div className="relative w-full h-full flex flex-row gap-1 items-center pl-3">
        {/* (1) 등록된 카드이면 오른쪽 상단에 배지 표시 */}
        {state === "REGISTERED" && (
        <div className="absolute top-0 right-3 bg-purple-500 text-white text-xs font-bold px-2 py-1 rounded-bl rounded-br">
            등록됨
        </div>
        )}

        {/* (2) 카드명: 왼쪽 수직 중앙 */}
        <div className='w-[45%] flex flex-col pl-1'>

            <div className=" text-gray-700  text-[18px]">
            {cardName}
            </div>
            {tab=="shared"&&<span>{username}</span>}
        </div>

        <div className='flex flex-col gap-5'>
            {/* (3) 사용현황 영역 (오른쪽 상단) */}
            {state ? (
                // 등록된 카드: "사용한 포인트 / 한도" (문구 + 실제 값 2줄)
                <div className="text-sm ">
                   <div className="text-sm flex flex-col justify-between items-start w-full">
                    <span className="text-[#6d6d6d]">사용 / 한도(원)</span>
                    <span className="text-[#333] text-[18px] whitespace-nowrap">
                        {usedPoint.toLocaleString()}
                        <span className="text-[16px] text-[#4c4c4c]"> / {limitPrice.toLocaleString()}</span>
                    </span>
                    </div>
                </div>
            ) : (
                // 등록되지 않은 카드: 한도만 표시
                <div className=" text-sm ">
                    <div className="text-sm flex flex-col justify-between items-start w-full">
                        <span className="text-[#6d6d6d]">한도</span>
                        <span className="text-[#333] text-[18px] whitespace-nowrap">
                            {limitPrice.toLocaleString()}
                        </span>
                    </div>
                </div>
            )}

            {/* (4) 만료일 영역 (오른쪽 하단) */}
            <div className="text-sm w-32">
                <div className="text-sm flex flex-col justify-between items-start w-full">
                        <span className="text-[#6d6d6d]">만료일</span>
                        <span className="text-[#333] text-[18px] whitespace-nowrap">
                            {expiredAt.replaceAll("-", ".")}
                        </span>
                </div>
            </div>
        </div>
    </div>
  );
};

export default LinkCardItem;

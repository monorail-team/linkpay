import React from 'react';

const MobileUI: React.FC = () => {
  return (
    <div className="relative w-[472px] h-[908px] bg-[#F5F5F5]">
      {/* (옵션) 백그라운드에 확장된 흰색 영역 – SVG의 두 번째 rect */}
      <div className="absolute inset-0 -translate-x-[210px] -translate-y-[1025px] bg-white"></div>

      {/* 전화기 프레임 */}
      <div className="absolute inset-0 m-4 rounded-[22px] border-8 border-[#CAC4D0] overflow-hidden">
        {/* 스크린 영역 */}
        <div className="w-full h-full rounded-[18px] bg-[#938F99] relative">
          {/* 상단 상태바 영역 (예: 시간, 아이콘 등) – SVG의 복잡한 path 대신 placeholder */}
          <div className="absolute top-4 left-4 right-4 flex justify-between items-center text-[#1D1B20]">
            <span className="text-sm font-semibold">9:41</span>
            <div className="flex space-x-1">
              {/* 아이콘 placeholder */}
              <span className="w-3 h-3 bg-[#1D1B20] rounded-full"></span>
              <span className="w-3 h-3 bg-[#1D1B20] rounded-full"></span>
            </div>
          </div>

          {/* 중앙 컨텐츠 영역 – 필요에 따라 구성 */}
          <div className="absolute top-16 left-4 right-4 bottom-80 bg-transparent">
            {/* 여기에 실제 앱 컨텐츠를 배치하면 됩니다. */}
          </div>

          {/* 하단 홈 인디케이터 영역 */}
          <div
            className="absolute"
            style={{
              left: '53px',
              bottom: '140px', // SVG의 y="768"에서 스크린 영역 패딩을 감안하여 조정
              width: '374px',
              height: '59px',
              backgroundSize: 'cover',
              backgroundImage:
                'url("data:image/svg+xml,%3Csvg xmlns=\'http://www.w3.org/2000/svg\' viewBox=\'0 0 374 59\' fill=\'%23CAC4D0\'%3E%3Cpath d=\'M0 0h374v59H0z\'/%3E%3C/svg%3E")',
            }}
          ></div>
        </div>
      </div>
    </div>
  );
};

export default MobileUI;

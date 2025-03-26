import React, { useState } from 'react';
import Header from '@/components/Header';
import { useNavigate } from 'react-router-dom';

// (1) 링크지갑 데이터 타입 (예시)
interface LinkWallet {
  id: number;
  walletName: string;
  participants: number; // 참여자 수
  balance: number;      // 잔액
  expiredAt: string;    // 만료일
  walletColor: string;  // 배경색
}

// (2) 탭 상수
const TAB_OWNED = 'owned';
const TAB_PARTICIPATED = 'participated';

// (3) 목 데이터 예시
const ownedWallets: LinkWallet[] = [
  {
    id: 1,
    walletName: '링크지갑명',
    participants: 0,
    balance: 890000,
    expiredAt: '25.12.23',
    walletColor: '#F6F2FF',
  },
];

const participatedWallets: LinkWallet[] = [
  {
    id: 2,
    walletName: '참여 지갑명',
    participants: 3,
    balance: 120000,
    expiredAt: '25.12.24',
    walletColor: '#FFEFEF',
  },
];

// (4) 페이지 컴포넌트
const LinkWalletListPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState<string>(TAB_OWNED);
  const navigate = useNavigate();

  // 탭 클릭 핸들러
  const handleTabClick = (tab: string) => {
    setActiveTab(tab);
  };

  // 현재 탭에 따라 필터링된 지갑 목록 반환
  const getWalletList = () => {
    if (activeTab === TAB_OWNED) {
      return ownedWallets;
    } else {
      return participatedWallets;
    }
  };

  // (5) 플러스카드 클릭 시 -> 링크지갑 생성 화면으로 이동
  const handleCreateLinkWallet = () => {
    navigate('/linkwalletcreate');
  };

  return (
    <div className="w-full h-screen max-w-md mx-auto dark:bg-black flex flex-col">
      {/* 헤더 */}
      <Header headerType="menu" onMenuClick={() => {}} />

      {/* 탭 영역 */}
      <div className="flex justify-around items-center border-b border-gray-200 dark:border-gray-700 text-sm mx-10">
        <button
          className={`py-3 w-full 
            ${
              activeTab === TAB_OWNED
                ? 'text-[#65558F] font-bold border-b-2 border-[#65558F] dark:text-[#D8D5F8] dark:border-[#D8D5F8]'
                : 'text-gray-500 dark:text-white'
            }`}
          onClick={() => handleTabClick(TAB_OWNED)}
        >
          내 소유 링크지갑
        </button>
        <button
          className={`py-3 w-full 
            ${
              activeTab === TAB_PARTICIPATED
                ? 'text-[#65558F] font-bold border-b-2 border-[#65558F] dark:text-[#D8D5F8] dark:border-[#D8D5F8]'
                : 'text-gray-500 dark:text-white'
            }`}
          onClick={() => handleTabClick(TAB_PARTICIPATED)}
        >
          참여한 링크지갑
        </button>
      </div>

      {/* 링크지갑 목록 */}
      <div className="p-4 flex-1 overflow-auto space-y-4">
        {getWalletList().map((wallet) => (
            <div
                key={wallet.id}
                className="my-1 box-border border rounded-lg w-5/6 p-4 mx-auto bg-center h-[7vh] dark:bg-[#3F3F3F] dark:border-[#706E6E]"
                
            >
                <div className="flex justify-between items-center">
                    {/* 왼쪽 영역: 지갑명, 참여자 수 */}
                    <div className="flex flex-col">
                        {/* 지갑명: 좀 더 크게 */}
                        <p className="text-lg font-semibold text-black dark:text-white">
                            {wallet.walletName}
                        </p>
                        {/* 참여자 수: 조금 더 작은 글씨 */}
                        <p className="text-sm text-gray-500 dark:text-gray-300 mt-1">
                            {wallet.participants}명 참여중
                        </p>
                    </div>

                    {/* 오른쪽 영역: 잔액, 만료일 (2줄) */}
                    <div className="flex flex-col items-end space-y-8">
                        {/* 1) 잔액 */}
                        <div className="flex justify-between w-full">
                            <span className="text-sm text-gray-500 dark:text-gray-300 mr-4">잔액</span>
                            <span className="text-base text-black dark:text-white font-medium">
                                {wallet.balance.toLocaleString()}원
                            </span>
                        </div>
                        {/* 2) 만료일 */}
                        <div className="flex justify-between w-full">
                            <span className="text-sm text-gray-500 dark:text-gray-300 mr-4">만료일</span>
                            <span className="text-base text-black dark:text-white font-medium">
                                {wallet.expiredAt}
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        ))}

        {/* 플러스카드 (새 링크지갑 생성용) */}
        <div
          onClick={handleCreateLinkWallet}
          className="my-1 box-border border rounded-lg w-5/6 p-4 mx-auto bg-center h-[7vh] flex items-center justify-center cursor-pointer bg-[#F2F2F2] dark:bg-black dark:border-[#706E6E]"
        >
          <span className="text-4xl text-gray-500">+</span>
        </div>
      </div>
    </div>
  );
};

export default LinkWalletListPage;

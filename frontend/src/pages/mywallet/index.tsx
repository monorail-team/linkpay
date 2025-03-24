import React,{useState} from 'react';
import Header from '@/components/Header';
import ChargeModal from '@/modal/ChargeModal';
import { MyWalletHistory } from '@/model/MyWalletHistory';
import { walletData } from '@/mocks/walletData';

const MyWallet: React.FC = () => {
  const [showChargeModal, setShowChargeModal] = useState(false);
  const [walletBalance, setWalletBalance] = useState(walletData.availablePoint);

  // 충전 API 호출 함수
  const handleCharge = async (amount: number) => {
    try {
      const accessToken = '사용자_토큰_여기'; 
      const response = await fetch('/api/wallets/charge', {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${accessToken}`,
        },
        body: JSON.stringify({ amount }),
      });
      if (response.status === 204) {
        // API 호출 성공 시 잔액 업데이트 및 모달 닫기
        setWalletBalance(prev => prev + amount);
        setShowChargeModal(false);
      } else {
        alert('충전에 실패했습니다.');
      }
    } catch (error) {
      console.error('Error charging wallet:', error);
      alert('오류가 발생했습니다.');
    }
  };


  return (
    <div className='dark:bg-[#3b3838]'>
      <Header headerType="menu" />
      <div className="w-full max-w-md mx-auto p-4" style={{ height: 'calc(100vh - 64px)' }}>
        {/* 내 지갑 정보 */}
        <div className="w-4/5 h-1/4 bg-[#F7F6F9] rounded-lg mx-auto flex flex-col justify-between relative dark:bg-[#6C6C6C]">
          <p className="test-sm text-[clamp(0.8rem,2vw,1rem)] text-black text-start mt-4 px-4 dark:text-[#D4D4D4]">
          {walletData.userWalletName}의 지갑
          </p>
          <div className='flex flex-col items-center justify-center h-2/3'>
            <p className="text-[clamp(1rem,2.5vw,1.5rem)] sm:text-base text-black text-center dark:text-[#D4D4D4]">
              잔여 포인트
            </p>
            <p className="text-[clamp(1.5rem,4vw,2.5rem)] sm:text-3xl text-black text-center font-bold dark:text-[#D4D4D4]">
              {walletData.availablePoint.toLocaleString()}원
            </p>
          </div>
          <div className="flex justify-between w-full">
            <button className="bg-white text-black py-2 px-4 ml-auto mr-2 mb-4 rounded-lg dark:bg-[#D4D4D4] text-[clamp(0.8rem,2vw,1rem)]">
              카드 생성
            </button>
            <button className="bg-white text-black py-2 px-4 mr-auto ml-2 mb-4 rounded-lg dark:bg-[#D4D4D4] text-[clamp(0.8rem,2vw,1rem)]"
              onClick={() => setShowChargeModal(true)}>
              충전하기
            </button>
          </div>
        </div>
        {/* 입출금 내역 */}
        <div className="mt-12 mx-6">
          <h3 className="text-lg text-[#969595]">입출금 내역</h3>
          <ul className="mt-2">
            {walletData.walletHistory.map((history: MyWalletHistory) => (
              <li
                key={history.walletHistoryId}
                className="py-1.5 border-b flex justify-between items-center text-lg border-gray-300 dark:border-[#515151] dark:bg-[#3b3838]"
              >
                {/* 날짜 & 카드 이름 */}
                <div className="flex items-center w-1/2">
                  <p className="text-black dark:text-white">
                    {history.transactionDate}
                  </p>
                  {history.type !== 'DEPOSIT' && (
                    <p className="text-black ml-12 dark:text-white">
                      {history.cardName}
                    </p>
                  )}
                </div>
                {/* 입출금 종류 & 금액 */}
                <div className="flex flex-col items-end">
                  <p className={`${history.type === 'DEPOSIT' ? 'text-blue-500' : 'text-black dark:text-white'}`}>
                    {history.type === 'DEPOSIT' ? '+' : '-'}{' '}
                    {Math.abs(history.point).toLocaleString()}원
                  </p>
                  <p className="text-sm text-gray-500 dark:text-white">
                    잔액 {history.afterPoint.toLocaleString()}원
                  </p>
                </div>
              </li>
            ))}
          </ul>
        </div>
      </div>
      {showChargeModal && (
        <ChargeModal 
          onClose={() => setShowChargeModal(false)}
          onConfirm={handleCharge}
        />
      )}
    </div>
  );
};

export default MyWallet;

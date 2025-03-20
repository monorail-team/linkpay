import React, { useState } from 'react';
import Header from '@/components/Header';
import Icon from '@/components/Icon';
import { MyWalletHistory } from '@/model/MyWalletHistory';
import { walletHistory } from '@/mocks/walletHistory';

const MyWallet: React.FC = () => {
  const [showSharedPoints, setShowSharedPoints] = useState(false);

  const togglePointsView = () => {
    setShowSharedPoints((prev) => !prev);
  };

  return (
    <div>
      <Header headerType="menu" />
        <div className="w-full max-w-md h-full mx-auto p-4">

        {/* 내 지갑 정보 */}
        <div className="w-4/5 h-1/4 bg-[#F7F6F9] rounded-lg mx-auto flex flex-col justify-between relative">
            <p className="text-sm text-black text-start mt-4 px-4">김싸피의 지갑</p>
            <div className='flex flex-col items-center justify-center h-2/3'>
                <p className="text-sm text-black text-center">{showSharedPoints ? '공유한 포인트' : '사용 가능 금액'}</p>
                <p className="text-2xl text-black text-center font-bold">{showSharedPoints ? '320,000원' : '870,000원'}</p>
            </div>

            <button onClick={togglePointsView} className={`absolute h-5/6 ${showSharedPoints ? 'left-4' : 'right-4'} top-1/2 transform -translate-y-1/2`} >
            <Icon name={showSharedPoints ? "arrowleft" : "arrowright"} width={24} height={24} />
            </button>

            <div className="flex justify-between w-full">
            <button className="bg-white text-black py-2 px-4 ml-auto mr-2 rounded-lg">카드 생성</button>
            <button className="bg-white text-black py-2 px-4 mr-auto ml-2 rounded-lg">충전하기</button>
            </div>
        </div>

        {/* 입출금 내역 */}
            <div className="mt-6">
            <h3 className="text-lg text-[#969595]">입출금 내역</h3>
                <ul className="mt-2 border-t border-gray-200">
                    {walletHistory.map((history: MyWalletHistory) => (
                    <li key={history.walletHistoryId} className="py-3 border-b flex justify-between items-center text-lg">
                        
                        {/* 날짜 & 카드 이름 */}
                        <div className="flex items-center w-1/2">
                        <p className="text-black">{history.transactionDate}</p>
                        <p className="text-black ml-12">{history.cardName}</p>
                        </div>

                        {/* 입출금 종류 & 금액 */}
                        <div className="flex flex-col items-end">
                        <p className="text-black">
                            {history.type === 'DEPOSIT' ? '입금' : '출금'} {Math.abs(history.point).toLocaleString()}원
                        </p>
                        <p className="text-sm text-gray-500">잔액 {history.afterPoint.toLocaleString()}원</p>
                        </div>

                    </li>
                    ))}
                </ul>
            </div>
        </div>
    </div>
  );
};

export default MyWallet;

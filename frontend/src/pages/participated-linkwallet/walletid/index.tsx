import React, { useState, useEffect, useRef, useCallback } from 'react';
import Header from '@/components/Header';
import ChargeModal from '@/modal/ChargeModal';
import MenuModal from '@/modal/MenuModal';
import { MyWalletHistory } from '@/model/MyWalletHistory';
import { useNavigate, useParams } from 'react-router-dom';
import { formatDateTime } from '@/util/formatdate';
import { LinkWallet } from '@/model/LinkWallet';
import axios from 'axios';
import BackgroundImage from '@/components/BackgroundImage';

const ParticipatedLinkWalletPage: React.FC = () => {
  const [showChargeModal, setShowChargeModal] = useState(false);
  const [walletHistories, setWalletHistories] = useState<MyWalletHistory[]>([]);
  const [hasNext, setHasNext] = useState(true);
  const [loading, setLoading] = useState(false);
  const [lastId, setLastId] = useState<string | null>(null);
  const [linkWallet, setLinkWallet] = useState<LinkWallet | null>(null);

  const base_url = process.env.REACT_APP_API_URL;
  const navigate = useNavigate();
  const { walletId } = useParams<{ walletId: string }>();

  //링크 지갑 정보
  useEffect(() => {
    const fetchLinkWallet = async () => {
      try {
        const token = sessionStorage.getItem('accessToken');
        const response = await axios.get(`${base_url}/api/linked-wallets/${walletId}`, {
          headers: { 'Authorization': `Bearer ${token}` },
        });
        setLinkWallet(response.data);
      } catch (error) {
        console.error('링크지갑 정보를 불러오는 중 오류 발생:', error);
      }
    };
    if (walletId) {
        fetchLinkWallet();
    }
  }, [base_url,walletId]);


  // 지갑 내역 API 호출 함수
  const fetchWalletHistories = useCallback(async () => {
    if (loading || !hasNext) return;
    setLoading(true);
    try {
      const token = sessionStorage.getItem('accessToken');
      let url = `${base_url}/api/wallet-histories/linked-wallet?walletId=${walletId}&size=10`;
      if (lastId) {
        url += `&lastId=${lastId}`;
      }
      const response = await axios.get(url, {
        headers: { 'Authorization': `Bearer ${token}` },
      });
      const { walletHistories: newHistories, hasNext: next } = response.data;
      setWalletHistories((prev) => {
        const combined = [...prev, ...newHistories];
        const uniqueHistories = combined.filter((history, index, self) =>
          index === self.findIndex(h => h.walletHistoryId === history.walletHistoryId)
        );
        return uniqueHistories;
      });
      setHasNext(next);
      if (newHistories && newHistories.length > 0) {
        const newLastId = newHistories[newHistories.length - 1].walletHistoryId;
        setLastId(newLastId);
      }
    } catch (error) {
      console.error('지갑 내역을 불러오는 중 오류 발생:', error);
    } finally {
      setLoading(false);
    }
  }, [loading, hasNext, lastId, base_url]);

  // 무한 스크롤: 스크롤 컨테이너의 하단에 ref 요소를 두고 IntersectionObserver를 사용
  const scrollContainerRef = useRef<HTMLDivElement | null>(null);
  const loadMoreRef = useRef<HTMLLIElement | null>(null);

  useEffect(() => {
    if (!scrollContainerRef.current || !loadMoreRef.current) return;
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && hasNext && !loading) {
          fetchWalletHistories();
        }
      },
      {
        root: scrollContainerRef.current,
        threshold: 0.1,
      }
    );
    observer.observe(loadMoreRef.current);
    return () => {
      observer.disconnect();
    };
  }, [fetchWalletHistories, hasNext, loading]);

  useEffect(() => {
    fetchWalletHistories();
  }, [fetchWalletHistories]);

  // 충전 API 호출 함수
  const handleCharge = async (amount: number) => {
    try {
      const token = sessionStorage.getItem('accessToken');
      const response = await axios.patch(`${base_url}/api/linked-wallets/charge/${walletId}`,
        { amount: amount },
        {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
          },
        }
      );
      if (response.status === 204) {
        setLinkWallet(prev =>
            prev ? { ...prev, amount: prev.amount + amount } : prev
          );
        setShowChargeModal(false);
        window.location.reload(); 
      } else {
        alert('충전에 실패했습니다.');
      }
    } catch (error) {
      console.error('Error charging wallet:', error);
      alert('오류가 발생했습니다.');
    }
  };

  const handleManageLinkWallet = (walletId: string) => {
    navigate(`/linkwallet/${walletId}/participants`);
  };

  // 메뉴 관련 핸들러
  const [showMenu, setShowMenu] = useState(false);
  const handleMenuClose = () => setShowMenu(false);

  return (
    <div className="dark:bg-[#3b3838]">
      <Header headerType="back" onBackClick={() => navigate("/linkwalletlist")} />
      {showMenu && <MenuModal onClose={handleMenuClose} />}
      <div className="w-full max-w-md mx-auto p-4" style={{ height: 'calc(100vh - 64px)' }}>
        {/* 지갑 정보 영역 (카드 생성 버튼은 제거됨) */}
        <div className="w-4/5 h-1/4 bg-[#F7F6F9] rounded-lg mx-auto flex flex-col justify-between relative dark:bg-[#6C6C6C]">
        <BackgroundImage />
          <p className="text-[clamp(0.8rem,2vw,1rem)] text-black text-start mt-4 px-4 dark:text-[#D4D4D4] z-[3]">
            {linkWallet?.linkedWalletName || '로딩 중...'}
          </p>
          <div className="flex flex-col items-center justify-center h-2/3">
            <p className="text-[clamp(1rem,2.5vw,1.5rem)] sm:text-base text-black text-center dark:text-[#D4D4D4] z-[3]">
              잔여 포인트
            </p>
            <p className="text-[clamp(1.5rem,4vw,2.5rem)] sm:text-3xl text-black text-center font-bold dark:text-[#D4D4D4] z-[3]">
              {linkWallet?.amount?.toLocaleString() || '0'}원
            </p>
          </div>
          <div className="flex justify-center w-full">
            <button className="bg-white text-black py-2 w-2/3 mb-4 rounded-lg dark:bg-[#D4D4D4] text-[clamp(0.8rem,2vw,1rem)] z-[3]"
              onClick={() => setShowChargeModal(true)}>
              충전하기
            </button>
          </div>
        </div>
        <div className="flex justify-end mr-10 mt-3">
          <button
            onClick={() => handleManageLinkWallet(walletId!)}
            className="bg-white border text-black py-1 px-2 rounded-lg dark:bg-[#4F4F4F] dark:text-white dark:border-[#4F4F4F] "
          >
            참여자목록
          </button>
        </div>
        {/* 입출금 내역 영역 */}
        <div className="mt-12 mx-6" ref={scrollContainerRef} style={{ maxHeight: '50vh', overflowY: 'auto' }}>
          <h3 className="text-lg text-[#969595]">입출금 내역</h3>
          <ul className="mt-2 hide-scrollbar">
            {walletHistories.map((history: MyWalletHistory) => (
              <li
                key={history.walletHistoryId}
                className="py-1.5 border-b flex justify-between items-center text-lg border-gray-300 dark:border-[#515151] dark:bg-[#3b3838]"
              >
                <div className="flex items-center w-1/2">
                  <p className="text-black dark:text-white">
                    {formatDateTime(history.time)}
                  </p>
                  {history.transactionType !== 'DEPOSIT' && (
                    <p className="text-black ml-12 dark:text-white">카드</p>
                  )}
                </div>
                <div className="flex flex-col items-end">
                  <p className={`${history.transactionType === 'DEPOSIT' ? 'text-blue-500' : 'text-black dark:text-white'}`}>
                    {history.transactionType === 'DEPOSIT' ? '+' : '-'} {Math.abs(history.amount).toLocaleString()}원
                  </p>
                  <p className="text-sm text-gray-500 dark:text-white">
                    잔액 {history.remaining.toLocaleString()}원
                  </p>
                </div>
              </li>
            ))}
            <li ref={loadMoreRef} />
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

export default ParticipatedLinkWalletPage;

import React,{ useState, useEffect, useRef, useCallback} from 'react';
import Header from '@/components/Header';
import ChargeModal from '@/modal/ChargeModal';
import MenuModal from '@/modal/MenuModal';
import { MyWalletHistory } from '@/model/MyWalletHistory';
import { useNavigate } from 'react-router-dom';
import { formatDateTime } from'@/util/formatdate';
import { useThemeStore } from '@/store/themeStore';
import axios from 'axios';

const MyWallet: React.FC = () => {
  const [showChargeModal, setShowChargeModal] = useState(false);
  const [walletBalance, setWalletBalance] = useState(0);
  const [walletHistories, setWalletHistories] = useState<MyWalletHistory[]>([]);
  const [hasNext, setHasNext] = useState(true);
  const [loading, setLoading] = useState(false);
  const [lastId, setLastId] = useState<string | null>(null);
  const [username, setUsername] = useState('');
  const { theme } = useThemeStore();
  const base_url = process.env.REACT_APP_API_URL;
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const token = sessionStorage.getItem('accessToken');
        const response = await axios.get(`${base_url}/api/mypage`, {
          headers: { 'Authorization': `Bearer ${token}` },
        });
        setUsername(response.data.username);
      } catch (error) {
        console.error('사용자 정보를 불러오는 중 오류 발생:', error);
      }
    };
    fetchUserInfo();
  }, [base_url]);

    // 지갑 잔액을 API에서 가져오는 useEffect
    useEffect(() => {
      const fetchWalletBalance = async () => {
        try {
          const token = sessionStorage.getItem('accessToken');
          const response = await axios.get(`${base_url}/api/my-wallets`, {
            headers: {
              'Authorization': `Bearer ${token}`,
            },
          });
          // API 응답 데이터의 amount 값을 walletBalance에 저장
          setWalletBalance(response.data.amount);
        } catch (error) {
          console.error('지갑 정보를 불러오는 중 오류 발생:', error);
          alert('지갑 정보를 불러오는데 실패했습니다.');
        }
      };
  
      fetchWalletBalance();
    }, [base_url]);

    // 지갑 내역 API 호출 함수
  const fetchWalletHistories =  useCallback(async () => {
    if (loading || !hasNext) return;
    setLoading(true);
    try {
      const token = sessionStorage.getItem('accessToken');
      let url = `${base_url}/api/wallet-histories/my-wallet?size=10`;
      if (lastId) {
        url += `&lastId=${lastId}`;
      }
      const response = await axios.get(url, {
        headers: { 'Authorization': `Bearer ${token}` },
      });
      const { walletHistories: newHistories, hasNext: next } = response.data;
      // 새 데이터를 기존 데이터에 추가
      setWalletHistories((prev) => {
        // 기존 데이터와 새 데이터를 합칩니다.
        const combined = [...prev, ...newHistories];
        // walletHistoryId를 기준으로 중복 제거
        const uniqueHistories = combined.filter((history, index, self) =>
          index === self.findIndex(h => h.walletHistoryId === history.walletHistoryId)
        );
        return uniqueHistories;
      });
      setHasNext(next);
      // 다음 페이지 요청을 위해 마지막 항목의 ID를 갱신 (newHistories가 존재하는 경우)
      if (newHistories && newHistories.length > 0) {
        const newLastId = newHistories[newHistories.length - 1].walletHistoryId;
        setLastId(newLastId);
      }
    } catch (error) {
      console.error('지갑 내역을 불러오는 중 오류 발생:', error);
      alert('지갑 내역을 불러오는데 실패했습니다.');
    } finally {
      setLoading(false);
    }
  }, [loading, hasNext, lastId, base_url]);

   // 무한 스크롤 구현: observer를 사용해 loadMoreRef가 화면에 나타나면 추가 데이터를 불러옴
   const loadMoreRef = useRef<HTMLDivElement | null>(null);
   const observer = useRef<IntersectionObserver>();
 
   const handleObserver = useCallback(
    (entries: IntersectionObserverEntry[]) => {
      const target = entries[0];
      if (target.isIntersecting && hasNext && !loading) {
        fetchWalletHistories();
      }
    },
    [hasNext, loading, fetchWalletHistories] 
  );

   useEffect(() => {
    if (observer.current) observer.current.disconnect();
    observer.current = new IntersectionObserver(handleObserver, {
      threshold: 0.1,
    });
    if (loadMoreRef.current) {
      observer.current.observe(loadMoreRef.current);
    }
    return () => observer.current?.disconnect();
  }, [handleObserver]);

  useEffect(() => {
    fetchWalletHistories();
  }, [fetchWalletHistories]);

  useEffect(() => {
      const root = document.documentElement;
      if (theme === 'dark') {
        root.classList.add('dark');
      } else {
        root.classList.remove('dark');
      }
    }, [theme]);
    
  // 충전 API 호출 함수
  const handleCharge = async (amount: number) => {
    try {
      const token = sessionStorage.getItem('accessToken');
      const response = await axios.patch(`${base_url}/api/my-wallets/charge`, 
        { amount: amount },
          {
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${token}`,
            },
          }
      );
      if (response.status === 204) {
        // API 호출 성공 시 잔액 업데이트 및 모달 닫기
        setWalletBalance(prev => prev + amount);
        console.log('충전 성공');
        setShowChargeModal(false);
        navigate('/mywallet');
      } else {
        alert('충전에 실패했습니다.');
      }
    } catch (error) {
      console.error('Error charging wallet:', error);
      alert('오류가 발생했습니다.');
    }
  };


  const handleCreateCard = () => {
    navigate('/createcard');
  };

  const [showMenu, setShowMenu] = useState(false);

  const handleMenuClick = () => {
    setShowMenu(true);
  };

  const handleMenuClose = () => {
    setShowMenu(false);
  };
  return (
    <div className='dark:bg-[#3b3838]'>
      <Header headerType="menu" onMenuClick={handleMenuClick} />
      {showMenu && <MenuModal onClose={handleMenuClose} />}
      <div className="w-full max-w-md mx-auto p-4" style={{ height: 'calc(100vh - 64px)' }}>
        {/* 내 지갑 정보 */}
        <div className="w-4/5 bg-[#F7F6F9] rounded-lg mx-auto flex flex-col gap-2 justify-between relative dark:bg-[#6C6C6C]">
          <p className=" text-[clamp(0.9rem,2vw,1rem)] text-[#333] text-start mt-4 px-4 dark:text-[#D4D4D4]">
          {username}의 지갑
          </p>
          <div className='flex flex-col items-center justify-center '>
            <p className="text-[clamp(1.1rem,2.5vw,1.5rem)] sm:text-base text-black text-center dark:text-[#D4D4D4]">
              잔여 포인트
            </p>
            <p className="text-[clamp(1.6rem,4vw,2.5rem)] sm:text-3xl text-black text-center font-bold dark:text-[#D4D4D4]">
              {walletBalance.toLocaleString()}원
            </p>
          </div>
          <div className="flex justify-between w-full gap-7 mt-2">
            <button
            onClick={handleCreateCard} 
            className="bg-white text-black py-2 px-4 ml-auto mb-4 rounded-lg dark:bg-[#D4D4D4] text-[clamp(1rem,2vw,1rem)]">
              카드 생성
            </button>
            <button className="bg-white text-black py-2 px-4 mr-auto mb-4 rounded-lg dark:bg-[#D4D4D4] text-[clamp(1rem,2vw,1rem)]"
              onClick={() => setShowChargeModal(true)}>
              충전하기
            </button>
          </div>
        </div>
        {/* 입출금 내역 */}
        <div className="mt-12 mx-6" >
          <h3 className="text-lg text-[#969595]">입출금 내역</h3>
          <ul className="mt-2 hide-scrollbar" style={{ maxHeight: '50vh', overflowY: 'auto' }}>
            {walletHistories.map((history: MyWalletHistory) => (
              <li
                key={history.walletHistoryId}
                className="py-1.5 border-b flex justify-between items-center text-lg border-gray-300 dark:border-[#515151] dark:bg-[#3b3838]"
              >
                {/* 날짜 & 카드 이름 */}
                <div className="flex items-center w-1/2">
                  <p className="text-black dark:text-white">
                    {formatDateTime(history.time)}
                  </p>
                  {history.transactionType  !== 'DEPOSIT' && (
                    <p className="text-black ml-12 dark:text-white">
                      {history.linkCardName}
                    </p>
                  )}
                </div>
                {/* 입출금 종류 & 금액 */}
                <div className="flex flex-col items-end">
                  <p className={`${history.transactionType === 'DEPOSIT' ? 'text-blue-500' : 'text-black dark:text-white'}`}>
                    {history.transactionType === 'DEPOSIT' ? '+' : '-'}{' '}
                    {Math.abs(history.amount).toLocaleString()}원
                  </p>
                  <p className="text-sm text-gray-500 dark:text-white">
                    잔액 {history.remaining.toLocaleString()}원
                  </p>
                </div>
              </li>
            ))}
          </ul>
          <div ref={loadMoreRef} />
        </div>
      </div>
      {showChargeModal && (
        <ChargeModal 
          onClose={() => setShowChargeModal(false)}
          onConfirm={handleCharge}
          returnPage={"mywallet"}
        />
      )}
    </div>
  );
};

export default MyWallet;

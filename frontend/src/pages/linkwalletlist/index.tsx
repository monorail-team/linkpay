import React, { useState, useEffect, useRef, useCallback} from 'react';
import Header from '@/components/Header';
import { useNavigate } from 'react-router-dom';
import MenuModal from '@/modal/MenuModal';
import axios from 'axios';
import { LinkWallet } from '@/model/LinkWallet';
import BackgroundImage from "@/components/BackgroundImage"
import { useThemeStore } from '@/store/themeStore';
const base_url = process.env.REACT_APP_API_URL;
const TAB_OWNED = 'CREATOR';
const TAB_PARTICIPATED = 'PARTICIPANT';
const PAGE_SIZE = 10;

const LinkWalletListPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState<string>(TAB_OWNED);
  const [wallets, setWallets] = useState<LinkWallet[]>([]);
  const [hasNext, setHasNext] = useState<boolean>(true);
  const [lastId, setLastId] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [showMenu, setShowMenu] = useState(false);
  const navigate = useNavigate();
  const observerRef = useRef<HTMLDivElement>(null);
  const { theme } = useThemeStore();

  const role = activeTab === TAB_OWNED ? 'CREATOR' : 'PARTICIPANT';
  
  const fetchWallets = useCallback(async () => {
    if (loading || !hasNext) return;

    setLoading(true);

    try {
      const token = sessionStorage.getItem('accessToken');
      let url = `${base_url}/api/linked-wallets`;
      
      if (lastId) {
        url += `?lastId=${lastId}&size=${PAGE_SIZE}&role=${role}`;
      } else {
        url += `?size=${PAGE_SIZE}&role=${role}`;
      }
      const response = await axios.get(url, {
        headers: { 'Authorization': `Bearer ${token}` },
      });

      const newWallets: LinkWallet[] = response.data.linkedWallets;

      setWallets((prev) => {
        const allWallets = [...prev, ...newWallets];
        const uniqueWallets = allWallets.filter(
          (wallet, index, self) =>
            index === self.findIndex((w) => w.linkedWalletId === wallet.linkedWalletId)
        );
        return uniqueWallets;
      });

      setHasNext(response.data.hasNext);
      if (newWallets.length > 0) {
        setLastId(newWallets[newWallets.length - 1].linkedWalletId);
      }

    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  }, [lastId, loading, hasNext, role]);


  useEffect(() => {
        const root = document.documentElement;
        if (theme === 'dark') {
          root.classList.add('dark');
        } else {
          root.classList.remove('dark');
        }
  }, [theme]);

  useEffect(() => {
    setWallets([]);
    setHasNext(true);
    setLastId(null);
  }, [activeTab]); 

  useEffect(() => {
    if(hasNext && lastId === null) {
      fetchWallets();
    }
  }, [fetchWallets, hasNext, lastId]);

  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && hasNext && !loading) {
          fetchWallets();
        }
      },
      { threshold: 1.0 }
    );
    if (observerRef.current) {
      observer.observe(observerRef.current);
    }
    return () => {
      if (observerRef.current) {
        observer.unobserve(observerRef.current);
      }
    };
  }, [fetchWallets, hasNext, loading]);

  const handleTabClick = (tab: string) => {
    setActiveTab(tab);
  };

  const handleCreateLinkWallet = () => {
      navigate('/createlinkwallet');
  };

  const handleLinkWallet = (walletId: string) => {
    if (activeTab === TAB_OWNED) {
      navigate(`/owned-linkwallet/${walletId}`);
    } else if (activeTab === TAB_PARTICIPATED) {
      navigate(`/participated-linkwallet/${walletId}`);
    }
  }

      
  const handleMenuClick = () => {
      setShowMenu(true);
  };
  
  const handleMenuClose = () => {
      setShowMenu(false);
  };

  return (
    <div className="w-full h-screen max-w-md mx-auto flex flex-col dark:bg-[#3b3838]">
      {/* 헤더 */}
      <Header headerType="menu" onMenuClick={handleMenuClick} />
      {showMenu && <MenuModal onClose={handleMenuClose} />}

      {/* 탭 영역 */}
      <div className="flex justify-around items-center border-b border-gray-200  dark:border-gray-700 text-[16px] mx-10">
        <button
          className={`py-3 w-full border-b-2
            ${
              activeTab === TAB_OWNED
                ? 'text-[#65558F] font-bold  border-[#65558F] dark:text-[#D8D5F8] dark:border-[#D8D5F8]'
                : 'text-gray-500 dark:text-white border-[transparent]'
            }`}
          onClick={() => handleTabClick(TAB_OWNED)}
        >
          내 소유 링크지갑
        </button>
        <button
          className={`py-3 w-full border-b-2
            ${
              activeTab === TAB_PARTICIPATED
                ? 'text-[#65558F] font-bold  border-[#65558F] dark:text-[#D8D5F8] dark:border-[#D8D5F8]'
                : 'text-gray-500 dark:text-white border-[transparent]'
            }`}
          onClick={() => handleTabClick(TAB_PARTICIPATED)}
        >
          참여한 링크지갑
        </button>
      </div>

      {/* 링크지갑 목록 */}
      <div className="p-4 flex-1 overflow-auto space-y-4">
        {wallets.map((wallet) => (
            <div
                key={wallet.linkedWalletId}
                className="relative my-1 box-border border rounded-lg w-5/6 p-4 mx-auto bg-center h-[7vh] min-h-[120px]  dark:border-[#706E6E]"
                onClick={() => handleLinkWallet(wallet.linkedWalletId)}
            >
                <BackgroundImage />

                {/* 왼쪽: 지갑명 (수직 중앙) */}
                <div className="absolute left-4 top-1/2 -translate-y-1/2">
                    <p className="text-sm text-gray-800 dark:text-white">
                      링크 지갑명
                    </p>
                    <p className="text-lg  text-gray-700 dark:text-white">
                        {wallet.linkedWalletName}
                    </p>
                </div>

                {/* 오른쪽 상단: 참여자 수수 */}
                <div className="absolute top-2 right-2 text-sm w-32">
                    <div className="flex justify-between">
                        <span className="text-gray-600 dark:text-gray-200 mt-2 ml-2">참여자 수</span>
                        <span></span>
                    </div>
                    <div className="flex justify-between mt-1">
                        <span></span>
                        <span className="text-gray-800 dark:text-white mr-2">
                          {wallet.participantCount} 명
                        </span>
                    </div>
                </div>

                {/* 오른쪽 하단: 잔액액 */}
                <div className="absolute bottom-2 right-2 text-sm w-32">
                    <div className="flex justify-between">
                        <span className="text-gray-600 dark:text-gray-200 ml-2">잔액</span>
                        <span></span>
                    </div>
                    <div className="flex justify-between mt-1">
                        <span></span>
                        <span className="text-gray-800 dark:text-white mb-2 mr-2">
                          {wallet.amount.toLocaleString()}원
                        </span>
                    </div>
                </div>
            </div>
        ))}

        {/* 플러스카드 (새 링크지갑 생성용) */}
        {activeTab === TAB_OWNED && (
          <div
            onClick={handleCreateLinkWallet}
            className="my-1 box-border border rounded-lg w-5/6 p-4 mx-auto bg-center h-[7vh] min-h-[120px] flex items-center justify-center cursor-pointer bg-[#FFFFFF] dark:bg-[#3b3838] dark:border-[#706E6E]"
          >
            <span className="text-4xl text-gray-500">+</span>
          </div>
        )}
        {activeTab === TAB_PARTICIPATED&&(wallets.length==0)&&
          <div className="text-center text-gray-500 text-[16px]">
            참여한 링크지갑이 존재하지 않습니다.
          </div>
        }
      </div>
    </div>
  );
};

export default LinkWalletListPage;

import React from 'react';
import { useNavigate } from 'react-router-dom';
import Icon from '@/components/Icon';
import { useThemeStore } from '@/store/themeStore';
import { PiWallet } from "react-icons/pi";
import { IoCardOutline } from "react-icons/io5";
import { HiOutlineWallet } from "react-icons/hi2";

export interface MenuModalProps {
  onClose: () => void;
}
const WalletIcon = PiWallet as unknown as (props: React.ComponentProps<'svg'>) => JSX.Element;
const CardIcon = IoCardOutline as unknown as (props: React.ComponentProps<'svg'>) => JSX.Element;
const LinkedWalletIcon = HiOutlineWallet as unknown as (props: React.ComponentProps<'svg'>) => JSX.Element;


const MenuModal: React.FC<MenuModalProps> = ({ onClose }) => {
  const navigate = useNavigate();
  const { theme } = useThemeStore();

  return (
    <div
      className={`fixed inset-0 z-50 flex items-start justify-center bg-opacity-30 ${theme === 'dark' ? 'dark' : ''}`}>
      <div
        className="w-full max-w-md mx-auto h-full bg-white dark:bg-[#3B3838] text-black dark:text-white rounded-lg overflow-hidden">
        {/* Header */}
        <div className="flex items-center justify-between p-4 h-16">
             <div className="text-2xl font-bold text-gray-300  dark:text-white-500" style={{fontFamily:"KakaoBigSans-ExtraBold"}}>LinkPay</div>
          <button onClick={onClose} aria-label="닫기" className="text-2xl">
            <Icon
              name={theme === 'dark' ? 'closeDarkIcon' : 'closeIcon'}
              width={30}
              height={30}
              alt="닫기"
            />
          </button>
        </div>

        {/* Body */}
        <div className="p-4">
          {/*메뉴 박스 영역*/}
          <div className="h-[calc(100vh-90px)] bg-white dark:bg-[#5a5757] border border-white dark:border-[#3B3838]
            rounded-lg">
            {/* 메뉴 리스트 */}
            <div className="flex flex-col divide-y divide-gray-200 dark:divide-none text-[18px] h-full">
              <button
                onClick={() => {
                  navigate('/mywallet');
                  onClose();
                }}
                className="flex items-center gap-3 px-4 py-4 hover:bg-gray-100 dark:hover:bg-[#3c3c3c]"
              >

                <WalletIcon style={{height:"28px", width:"28px"}}/>

                <span>내 지갑</span>
              </button>
              <button
                onClick={() => {
                  navigate('/linkcardlist');
                  onClose();
                }}
                className="flex items-center gap-3 px-4 py-4 hover:bg-gray-100 dark:hover:bg-[#3c3c3c]"
              >
               <CardIcon style={{height:"28px", width:"28px"}}/>
                <span>링크카드 관리</span>
              </button>
              <button
                onClick={() => {
                  navigate('/linkwalletlist');
                  onClose();
                }}
                className="flex items-center gap-3 px-4 py-4 hover:bg-gray-100 dark:hover:bg-[#3c3c3c]"
              >
                <LinkedWalletIcon style={{height:"28px", width:"28px"}}/>
                <span>링크지갑</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MenuModal;

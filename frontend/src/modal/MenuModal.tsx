import React from 'react';
import { useNavigate } from 'react-router-dom';
import Icon from '@/components/Icon';
import { useThemeStore } from '@/store/themeStore';

export interface MenuModalProps {
  onClose: () => void;
}

const MenuModal: React.FC<MenuModalProps> = ({ onClose }) => {
  const navigate = useNavigate();
  const { theme } = useThemeStore();

  return (
    <div
      className={`fixed inset-0 z-50 flex items-start justify-center bg-black bg-opacity-30 ${theme === 'dark' ? 'dark' : ''}`}>
      <div
        className="w-full max-w-sm bg-white dark:bg-[#3b3838] text-black dark:text-white h-full rounded-lg overflow-hidden">
        {/* Header */}
        <div className="flex items-center justify-between p-4 border-b border-gray-200 dark:border-gray-700">
          <div className="text-xl font-bold text-gray-300 dark:text-white-500">LinkPay</div>
          <button onClick={onClose} aria-label="닫기" className="text-2xl">
            <Icon
              name={theme === 'dark' ? 'closeDarkIcon' : 'closeIcon'}
              width={24}
              height={24}
              alt="닫기"
            />
          </button>
        </div>

        {/* Body */}
        <div className="p-4">
          {/*메뉴 박스 영역*/}
          <div className="h-[calc(100vh-90px)] bg-white dark:bg-[#5a5757] border border-white dark:border-gray-600
            rounded-lg">
            {/* 메뉴 리스트 */}
            <div className="flex flex-col divide-y divide-gray-200 dark:divide-gray-600 text-sm h-full">
              <button
                onClick={() => {
                  navigate('/mywallethistory');
                  onClose();
                }}
                className="flex items-center gap-2 px-4 py-4 hover:bg-gray-100 dark:hover:bg-[#3c3c3c]"
              >
                <Icon
                  name={theme === 'dark' ? 'personDarkIcon' : 'personIcon'}
                  width={24}
                  height={24}
                  alt="내 지갑"
                />
                <span>내 지갑</span>
              </button>
              <button
                onClick={() => {
                  navigate('/링크카드관리페이지');
                  onClose();
                }}
                className="flex items-center gap-2 px-4 py-4 hover:bg-gray-100 dark:hover:bg-[#3c3c3c]"
              >
                <Icon
                  name={theme === 'dark' ? 'personDarkIcon' : 'personIcon'}
                  width={24}
                  height={24}
                  alt="링크카드 관리"
                />
                <span>링크카드 관리</span>
              </button>
              <button
                onClick={() => {
                  navigate('/링크지갑페이지');
                  onClose();
                }}
                className="flex items-center gap-2 px-4 py-4 hover:bg-gray-100 dark:hover:bg-[#3c3c3c]"
              >
                <Icon
                  name={theme === 'dark' ? 'cissorsDarkIcon' : 'cissorsIcon'}
                  width={24}
                  height={24}
                  alt="링크지갑"
                />
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

import React from 'react';
import Icon from '@/components/Icon';
import { useThemeStore } from '@/store/themeStore';

interface HeaderProps {
  headerType?: 'menu' | 'back';
  onBackClick?: () => void;
  onMenuClick?: () => void;
}

const Header: React.FC<HeaderProps> = ({
  headerType = 'menu',
  onBackClick,
  onMenuClick,
}) => {

  const { theme, toggleTheme } = useThemeStore();

  return (
    <header className="flex items-center justify-between h-16 px-4 bg-white border-b border-gray-200 dark:bg-[#3b3838] dark:border-gray-700">
      {/* 뒤로가기 버튼이 있는 헤더 */}
      {headerType === 'back' ? (
        <button onClick={onBackClick} className="text-lg focus:outline-none">
          <Icon
            name={theme === 'dark' ? 'backDarkIcon' : 'backIcon'}
            width={24}
            height={24}
            alt="뒤로가기"
          />
        </button>
      ) : (
        <div className="text-xl font-bold text-gray-300  dark:text-white-500">LinkPay</div>
      )}

      {/* 메뉴 버튼이 있는 헤더 */}
      {headerType === 'menu' ? (
        <div className="flex items-center">
          {/* 다크모드 토글 버튼 */}
          <button
            onClick={toggleTheme}
            className="mr-2 text-sm focus:outline-none p-2 border rounded dark:text-gray-300"
          >
            <Icon name="themeIcon" width={24} height={24} alt="다크 모드 토글" />
          </button>
          <button onClick={onMenuClick} className="text-lg focus:outline-none">
          <Icon
            name={theme === 'dark' ? 'menuDarkIcon' : 'menuIcon'}
            width={24}
            height={24}
            alt="메뉴"
          />
          </button>
        </div>
      ) : (
        <div></div>
      )}
    </header>
  );
};

export default Header;

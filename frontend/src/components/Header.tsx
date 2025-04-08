import React from 'react';
import { useNavigate } from 'react-router-dom';
import Icon from '@/components/Icon';
import { useThemeStore } from '@/store/themeStore';
import { IoArrowBack } from "react-icons/io5";

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

  const navigate = useNavigate();
  const { theme, toggleTheme } = useThemeStore();

  const handleLinkPayClick = () => {
    navigate('/');
  };

  const ArrowIcon = IoArrowBack as unknown as (props: React.ComponentProps<'svg'>) => JSX.Element;

  return (
    <header className="flex items-center justify-between h-16 px-4 bg-white border-b border-gray-200 dark:bg-[#3b3838] dark:border-gray-700">
      {/* 뒤로가기 버튼이 있는 헤더 */}
      {headerType === 'back' ? (
        <button onClick={onBackClick} className="text-lg focus:outline-none">
          <ArrowIcon style={{width:"40px", height:"40px", color:theme=="dark"?"#ccc":"#aaa"}}/>
        </button>
      ) : (
        <div className="text-2xl font-bold text-gray-300 dark:text-white-500" onClick={handleLinkPayClick} style={{fontFamily:"KakaoBigSans-ExtraBold"}}>LinkPay</div>
      )}

      {/* 메뉴 버튼이 있는 헤더 */}
      {headerType === 'menu' && (
        <div className="flex items-center gap-4">
          {/* 다크모드 토글 버튼 */}
          <button
            onClick={toggleTheme}
            className=" text-sm focus:outline-none p-2 border rounded dark:text-gray-300"
          >
            <Icon name="themeIcon" width={30} height={30} alt="다크 모드 토글" />
          </button>
          <button onClick={onMenuClick} className="text-lg focus:outline-none">
          <Icon
            name={theme === 'dark' ? 'menuDarkIcon' : 'menuIcon'}
            width={40}
            height={40}
            alt="메뉴"
          />
          </button>
        </div>
      ) }
    </header>
  );
};

export default Header;

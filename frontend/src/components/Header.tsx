import React from 'react';
import Icon from '@/components/Icon';

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
  return (
    <header className="flex items-center justify-between h-16 px-4 bg-white border-b border-gray-200">
      {headerType === 'back' ? (
        <button onClick={onBackClick} className="text-lg focus:outline-none">
          <Icon name="back" width={24} height={24} alt="뒤로가기" />
        </button>
      ) : (
        <div className="text-xl font-bold text-gray-300">LinkPay</div>
      )}
      {headerType === 'menu' ? (
        <button onClick={onMenuClick} className="text-lg focus:outline-none">
          <Icon name="menu" width={24} height={24} alt="메뉴" />
        </button>
      ) : (
        <div></div>
      )}
    </header>
  );
};

export default Header;

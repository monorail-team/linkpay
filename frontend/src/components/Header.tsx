// src/components/Header.tsx
import React from 'react';
import Icon from '@/components/Icon';
const Header: React.FC = () => {
  return (
    <header className="flex items-center justify-between h-16 px-4 bg-white border-b border-gray-200">
      <div className="text-xl font-bold text-gray-300">LinkPay</div>
      <button className="text-lg focus:outline-none">
        <Icon name="menu" width={24} height={24} alt="menu"/>
      </button>
    </header>
  );
};

export default Header;

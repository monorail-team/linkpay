// src/components/Header.tsx
import React from 'react';

const Header: React.FC = () => {
  return (
    <header className="flex items-center justify-between h-16 px-4 bg-white border-b border-gray-200">
      <div className="text-xl font-bold text-gray-800">LinkPay</div>
      <button className="text-lg focus:outline-none">
        <span>☰</span>
      </button>
    </header>
  );
};

export default Header;

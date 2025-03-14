// src/components/LinkCardTabs.tsx
import React from 'react';

interface LinkCardTabsProps {
  activeTab: number;
  onTabChange: (tabIndex: number) => void;
}

const TABS = ['생성한 링크카드', '공유한 링크카드', '받은 링크카드'];

const LinkCardTabs: React.FC<LinkCardTabsProps> = ({ activeTab, onTabChange }) => {
  return (
    <div className="flex justify-around border-b border-gray-300 bg-white">
      {TABS.map((tab, index) => {
        const isActive = index === activeTab;
        return (
          <button
            key={tab}
            className={`relative px-4 py-2 text-sm font-medium 
              ${
                isActive
                  ? 'text-purple-600'
                  : 'text-gray-600 hover:text-gray-800'
              }
            `}
            onClick={() => onTabChange(index)}
          >
            {tab}
            {/* 선택된 탭 아래쪽에 보라색 밑줄 */}
            {isActive && (
              <div className="absolute left-0 right-0 -bottom-1 h-0.5 bg-purple-600" />
            )}
          </button>
        );
      })}
    </div>
  );
};

export default LinkCardTabs;

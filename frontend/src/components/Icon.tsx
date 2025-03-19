// src/components/Icon.tsx
import React from 'react';
import fingerprintIcon from '@/assets/icons/Fingerprint.png';
import menuIcon from '@/assets/icons/Menu.png';
import backIcon from '@/assets/icons/arrow_back.png';
// 아이콘 매핑: icon 이름과 해당 파일 경로를 매핑합니다.
// 만약 추가 아이콘이 있다면 이 객체에 추가하면 됩니다.
const iconMap: { [key: string]: string } = {
  fingerprint: fingerprintIcon,
  menu: menuIcon,
  back: backIcon,
  // 예시: home: require('@/assets/icons/Home.png'),
  // 예시: search: require('@/assets/icons/Search.png'),
};

interface IconProps {
  name: keyof typeof iconMap; // icon 이름을 제한하여 안전성을 높입니다.
  width?: number;
  height?: number;
  alt?: string;
}

const Icon: React.FC<IconProps> = ({ name, width = 60, height = 60, alt }) => {
  const src = iconMap[name];
  return (
    <img
      src={src}
      alt={alt || `${name} icon`}
      style={{ width, height }}
    />
  );
};

export default Icon;

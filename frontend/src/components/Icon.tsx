import React from 'react';
import fingerprintIcon from '@/assets/icons/fingerprint.png';
import fingerprintDarkIcon from '@/assets/icons/fingerprint-darkmode.png';
import menuIcon from '@/assets/icons/menu.png';
import menuDarkIcon from '@/assets/icons/menu-darkmode.png';
import backIcon from '@/assets/icons/arrow-back.png';
import backDarkIcon from '@/assets/icons/arrow-back-darkmode.png';
import theme from '@/assets/icons/day-and-night.png';

const iconMap: { [key: string]: string } = {
  fingerprint: fingerprintIcon,
  fingerprintDarkIcon: fingerprintDarkIcon,
  menu: menuIcon,
  menuDarkIcon: menuDarkIcon,
  back: backIcon,
  backDarkIcon: backDarkIcon,
  theme: theme,
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

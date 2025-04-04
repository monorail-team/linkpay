import React from 'react';
import fingerprintIcon from '@/assets/icons/fingerprint.png';
import fingerprintDarkIcon from '@/assets/icons/fingerprint-darkmode.png';
import menuIcon from '@/assets/icons/menu.png';
import menuDarkIcon from '@/assets/icons/menu-darkmode.png';
import backIcon from '@/assets/icons/arrow-back.png';
import backDarkIcon from '@/assets/icons/arrow-back-darkmode.png';
import theme from '@/assets/icons/day-and-night.png';
import arrowRightIcon from '@/assets/icons/arrow-right.png';
import arrowLeftIcon from '@/assets/icons/arrow-left.png';
import closeIcon from '@/assets/icons/x-close.png';
import closeDarkIcon from '@/assets/icons/x-close-darkmode.png';
import cissorsIcon from '@/assets/icons/cissors.png';
import cissorsDarkIcon from '@/assets/icons/cissors-darkmode.png';
import personIcon from '@/assets/icons/person.png';
import personDarkIcon from '@/assets/icons/person-drakmode.png';
import calandarIcon from '@/assets/icons/calendar.png';
import canceltextIcon from '@/assets/icons/cancel-text.png';
import calendarDarkIcon from '@/assets/icons/calendar-darkmode.png';
import canceltextDarkIcon from '@/assets/icons/cancel-text-darkmode.png';
import searchIcon from '@/assets/icons/search.png';
import searchDarkIcon from '@/assets/icons/search-darkmode.png';
import searchcalcelIcon from '@/assets/icons/searchcancel.png';
import searchcalcelDarkIcon from '@/assets/icons/searchcancel-darkmode.png';

// 아이콘 매핑: icon 이름과 해당 파일 경로를 매핑합니다.
// 만약 추가 아이콘이 있다면 이 객체에 추가하면 됩니다.
const iconMap: { [key: string]: string } = {
  fingerprintIcon: fingerprintIcon,
  fingerprintDarkIcon: fingerprintDarkIcon,
  menuIcon: menuIcon,
  menuDarkIcon: menuDarkIcon,
  backIcon: backIcon,
  backDarkIcon: backDarkIcon,
  arrowright: arrowRightIcon,
  arrowleft: arrowLeftIcon,
  closeIcon: closeIcon,
  closeDarkIcon: closeDarkIcon,
  personIcon: personIcon,
  personDarkIcon: personDarkIcon,
  cissorsIcon: cissorsIcon,
  cissorsDarkIcon: cissorsDarkIcon,
  themeIcon: theme,
  arrowrightIcon: arrowRightIcon,
  arrowleftIcon: arrowLeftIcon,
  calandarIcon: calandarIcon,
  calendarDarkIcon: calendarDarkIcon,
  canceltextIcon: canceltextIcon,
  canceltextDarkIcon: canceltextDarkIcon,
  searchIcon: searchIcon,
  searchDarkIcon: searchDarkIcon,
  searchcalcelIcon: searchcalcelIcon,
  searchcalcelDarkIcon: searchcalcelDarkIcon,
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

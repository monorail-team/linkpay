// src/components/FingerprintIcon.tsx
import React from 'react';
import fingerprintImg from '@/assets/icons/Fingerprint.png';

interface FingerprintIconProps {
  width?: number;
  height?: number;
}

const FingerprintIcon: React.FC<FingerprintIconProps> = ({ width = 60, height = 60 }) => {
  return (
    <img 
      src={fingerprintImg} 
      alt="Fingerprint Icon" 
      style={{ width, height }}
    />
  );
};

export default FingerprintIcon;

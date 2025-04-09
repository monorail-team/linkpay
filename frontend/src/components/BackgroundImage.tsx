import React from 'react';
import flower from '../assets/images/flower.png'; 
import flower2 from '../assets/images/flower2.png';

const BackgroundImage: React.FC = () => {
  const images: string[] = [flower, flower2];
  const randomImage: string = images[Math.floor(Math.random() * images.length)];

  return (
    <div
      className="absolute inset-0 z-0"
      style={{
        backgroundImage: `url(${randomImage})`,
        backgroundSize: '170%',
        backgroundPosition: 'center',
        opacity: 0.4,
      }}
    />
  );
};

export default BackgroundImage;

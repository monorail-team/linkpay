import React from 'react';

interface AddLinkCardProps {
    onClick?: () => void;
  }
const AddLinkCard: React.FC<AddLinkCardProps> = ({ onClick }) => {
  return (
    <div 
        onClick={onClick}
        className="relative w-full max-w-[354px] aspect-[354/210] bg-[#938F99] rounded-xl shadow-md p-4 flex flex-col items-center justify-center">
    <div className="text-5xl text-white font-bold">+</div>
    <div className="mt-2 text-center text-base text-white">링크 등록</div>
    </div>
  );
};

export default AddLinkCard;

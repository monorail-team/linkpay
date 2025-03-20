import React from 'react';

interface ButtonProps {
  type: 'modal' | 'page';
  label: string;
  onClick?: () => void;
}

const Button: React.FC<ButtonProps> = ({ type = 'modal', label, onClick }) => {
  switch (type) {
    case 'modal':
      return (
        <button
          className="mx-4 w-[105px] h-[40px] bg-[#7C8DBD] text-white font-bold rounded-lg hover:bg-[#6b7aa1] active:bg-[#5a6787] transition"
          onClick={onClick}
        >
          {label}
        </button>
      );
    case 'page':
      return (
        <button
          className="w-[105px] h-[40px] bg-[#7C8DBD] text-white font-bold rounded-lg hover:bg-[#6b7aa1] active:bg-[#5a6787] transition"
          onClick={onClick}
        >
          page버튼은 병합 후 추가하겠음{label}
        </button>
      );
    default:
      return null;
  }
};

export default Button;

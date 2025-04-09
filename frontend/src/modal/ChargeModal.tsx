import React, { useState } from 'react';
import ButtonModal from '@/modal/ButtonModal';
import { useNavigate } from 'react-router-dom';

export interface ChargeModalProps {
  onClose: () => void;
  onConfirm: (amount: number) => void;
  returnPage: String,
}

const ChargeModal: React.FC<ChargeModalProps> = ({ onClose, onConfirm, returnPage }) => {
  const [amount, setAmount] = useState<string>('');
  const navigate = useNavigate();

  // 🔹 입력값을 숫자로 변환 & 1000단위 콤마 추가
  const formatNumber = (value: string) => {
    // 숫자만 남기기 (정수값만 입력 가능)
    let numericValue = value.replace(/\D/g, '');
    if (parseInt(numericValue) > 10000000) {
      alert('최대 입력 가능 금액은 10,000,000원 입니다.');
      numericValue = amount; // 최대값 제한
    }
    // 1000 단위 콤마 추가
    return numericValue.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  };

  // 🔹 입력 변경 핸들러
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const rawValue = e.target.value;
    setAmount(formatNumber(rawValue));
  };

  const handleConfirm = () => {
    const numAmount = parseInt(amount.replace(/,/g, ''), 10);
    if (!isNaN(numAmount) && numAmount > 0) {
      navigate(`/checkout?amount=${numAmount}&returnPage=${returnPage}`)
    } else {
      alert('올바른 금액을 입력해주세요.');
    }
  };

  return (
    <>
      <ButtonModal onClose={onClose} onConfirm={handleConfirm}>
        <h2 className="text-lg font-semibold text-center mb-4">
          충전할 금액을 입력해주세요.
        </h2>
        <input
          type="text" // 🔹 숫자가 아닌 'text'로 설정하여 콤마 표시 가능
          placeholder="금액을 입력해주세요."
          value={amount}
          onChange={handleChange}
          className="w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 text-left"
          style={{border:"1px solid #ccc"}}
        />
      </ButtonModal>
    </>
  );
};

export default ChargeModal;

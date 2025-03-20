import React from 'react';
import Button from '../components/Button';

export interface ButtonModalProps {
  isOpen: boolean;
  onClose: () => void;
  onConfirm: () => void;
  children: React.ReactNode;
}

const ButtonModal: React.FC<ButtonModalProps> = ({ isOpen, onClose, onConfirm, children }) => {
  if (!isOpen) return null; // 모달이 열려있을 때만 표시

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-30">
      <div className="bg-white p-6 rounded-xl shadow-lg w-80">
        {children} {/* 🔹 모달 내용 (각 모달에서 다르게 설정 가능) */}

        <div className="flex justify-center mt-4">
          <Button type="modal" label="확인" onClick={onConfirm} />
          <Button type="modal" label="취소" onClick={onClose} />
        </div>
      </div>
    </div>
  );
};

export default ButtonModal;

import React from 'react';
import Button from '../components/Button';

export interface ButtonModalProps {
  type?: 'confirm' | 'confirmAndCancel';
  onClose?: () => void;
  onConfirm: () => void;
  children: React.ReactNode;
}

const ButtonModal: React.FC<ButtonModalProps> = ({ type = 'confirmAndCancel', onClose, onConfirm, children }) => {
  switch (type) {
    case 'confirmAndCancel':
      return (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50" style={{ zIndex: 1000 }}>
          <div className="bg-white p-6 rounded-xl shadow-lg w-80">
            {children}
            <div className="flex justify-center mt-4">
              <Button type="modal" label="확인" onClick={onConfirm} />
              <Button type="modal" label="취소" onClick={onClose} />
            </div>
          </div>
        </div>
      );
    case 'confirm':
      return (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-30">
          <div className="bg-white p-6 rounded-xl shadow-lg w-80">
            {children}
            <div className="flex justify-center mt-4">
              <Button type="modal" label="확인" onClick={onConfirm} />
            </div>
          </div>
        </div>
      );
    default:
      return null;
  }
};

export default ButtonModal;

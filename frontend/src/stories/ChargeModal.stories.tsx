import React, { useState } from 'react';
import { Meta, StoryFn } from '@storybook/react';
import ChargeModal, { ChargeModalProps } from '../modal/ChargeModal';


export default {
  title: 'Components/ChargeModal',
  component: ChargeModal,
  argTypes: {
    onClose: { action: 'closed' },
    onConfirm: { action: 'confirmed' }
  }
} as Meta<typeof ChargeModal>;

const Template: StoryFn<ChargeModalProps> = (args) => {
  const [isOpen, setIsOpen] = useState(true);

  // ✅ JSX.Element를 반환하도록 수정 (null을 반환하지 않음)
  return (
    <>
      <button onClick={() => setIsOpen(true)} className="bg-blue-500 text-white p-2 rounded">
        모달 열기
      </button>
      {isOpen && <ChargeModal {...args} onClose={() => setIsOpen(false)} />}
    </>
  );
};

export const Default = Template.bind({});
Default.args = {
  onConfirm: (amount: number) => alert(`충전 금액: ${amount}원`)
};
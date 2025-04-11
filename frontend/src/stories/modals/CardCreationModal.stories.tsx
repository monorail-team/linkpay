import React from 'react';
import { Meta, StoryFn } from '@storybook/react';
import CardCreationModal, { CardCreationModalProps } from '@/modal/CardCreationModal';


export default {
  title: 'Modals/CardCreationModal',
  component: CardCreationModal,  // ✅ 올바른 컴포넌트 지정
  argTypes: {
    onClose: { action: 'closed' },
    onConfirm: { action: 'confirmed' }
  }
} as Meta<typeof CardCreationModal>;

const Template: StoryFn<CardCreationModalProps> = (args) => <CardCreationModal {...args} />;

export const Default = Template.bind({});
Default.args = {
  onConfirm: () => alert(`확인`),
  onClose: () => alert('취소'),
  cardName: 'A105',         // ✅ 카드이름 동적 전달
  cardLimit: 1000000,     // ✅ 개인한도 동적 전달
  expiryDate: '2025.03.20'  // ✅ 만료일 동적 전달
};
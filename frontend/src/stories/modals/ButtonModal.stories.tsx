import React from 'react';
import { Meta, StoryFn } from '@storybook/react';
import ButtonModal, { ButtonModalProps } from '@/modal/ButtonModal';

export default {
  title: 'Modals/ButtonModal',
  component: ButtonModal,
  argTypes: {
    onClose: { action: 'closed' },
    onConfirm: { action: 'confirmed' },
    isOpen: { control: 'boolean' }
  }
} as Meta<typeof ButtonModal>;

const Template: StoryFn<ButtonModalProps> = (args) => <ButtonModal {...args} />;

// 기본 모달 예제
export const Default = Template.bind({});
Default.args = {
  isOpen: true,
  children: <p className="text-center">이것은 기본 모달입니다.</p>
};
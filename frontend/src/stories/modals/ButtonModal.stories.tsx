import React from 'react';
import { Meta, StoryFn } from '@storybook/react';
import ButtonModal, { ButtonModalProps } from '@/modal/ButtonModal';

export default {
  title: 'Modals/ButtonModal',
  component: ButtonModal,
  argTypes: {
    onClose: { action: 'closed' },
    onConfirm: { action: 'confirmed' },
    type: { control: 'boolean' }
  }
} as Meta<typeof ButtonModal>;

const Template: StoryFn<ButtonModalProps> = (args) => <ButtonModal {...args} />;

// 기본 모달 예제
export const Default = Template.bind({});
Default.args = {
  type: 'confirmAndCancel',
  children: <p className="text-center">기본 모달 with children</p>
};

// 기본 모달 예제
export const Confirm = Template.bind({});
Confirm.args = {
  type: 'confirm',
  children: <p className="text-center">확인만 있는 모달 with children</p>
};
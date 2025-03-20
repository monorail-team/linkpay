import React from 'react';
import { Meta, StoryFn } from '@storybook/react';
import Button from '../components/Button'; // Button 컴포넌트 경로에 맞게 수정

export default {
  title: 'Components/Button',
  component: Button,
  argTypes: {
    onClick: { action: 'clicked' } // Storybook에서 클릭 이벤트 감지 가능
  }
} as Meta<typeof Button>;

const Template: StoryFn<typeof Button> = (args) => <Button {...args} />;

// 기본 버튼 스토리
export const Default = Template.bind({});
Default.args = {
  label: '확인'
};

// 클릭 이벤트 테스트 스토리
export const Modal = Template.bind({});
Modal.args = {
  type: 'modal',
  label: '클릭 테스트',
  onClick: () => alert('버튼이 클릭되었습니다!')
};

export const Page = Template.bind({});
Page.args = {
  type: 'page',
  label: '클릭 테스트',
  onClick: () => alert('버튼이 클릭되었습니다!')
};

import React from 'react';
import { Meta, StoryFn } from '@storybook/react';

import { MemoryRouter } from 'react-router-dom';
import MenuModal, { MenuModalProps } from '@/modal/MenuModal';
import { useThemeStore } from '@/store/themeStore';

export default {
  title: 'Modals/MenuModal',
  component: MenuModal,
  decorators: [
    (Story, context) => {
      // ⚡ zustand 상태를 강제로 세팅
      const theme = (context?.args as any)?.theme ?? 'light';
      useThemeStore.setState({ theme });

      return (
        <MemoryRouter>
          <Story />
        </MemoryRouter>
      );
    }
  ],
  argTypes: {
    theme: {
      control: { type: 'radio' },
      options: ['light', 'dark']
    }
  }
} as Meta<typeof MenuModal>;


const Template: StoryFn<MenuModalProps> = () => <MenuModal onClose={() => alert('닫기')} />;

export const Default = Template.bind({});
Default.args = {};

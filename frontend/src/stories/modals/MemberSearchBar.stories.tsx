import React from 'react';
import { Meta, StoryFn } from '@storybook/react';
import { MemoryRouter } from 'react-router-dom';
import MemberSearchBar from '@/components/MemberSearchBar';

export default {
  title: 'Components/MemberSearchBar',
  component: MemberSearchBar,
  decorators: [
    (Story) => (
      <MemoryRouter>
        <Story />
      </MemoryRouter>
    )
  ]
} as Meta<typeof MemberSearchBar>;

const Template: StoryFn<typeof MemberSearchBar> = (args) => <MemberSearchBar {...args} />;

export const Default = Template.bind({});
Default.args = {};

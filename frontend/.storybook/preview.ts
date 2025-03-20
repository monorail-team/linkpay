import type { Preview } from '@storybook/react';
import '../src/index.css'; // Tailwind가 적용된 글로벌 CSS 불러오기


const preview: Preview = {
  parameters: {
    controls: {
      matchers: {
        color: /(background|color)$/i,
        date: /Date$/i
      }
    }
  }
};

export default preview;
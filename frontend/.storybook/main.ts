import type { StorybookConfig } from '@storybook/react-webpack5';
import path from 'path';

const config: StorybookConfig = {
  'stories': [
    '../src/**/*.mdx',
    '../src/**/*.stories.@(js|jsx|mjs|ts|tsx)'
  ],
  'addons': [
    '@storybook/addon-essentials',
    '@storybook/preset-create-react-app',
    '@storybook/addon-onboarding',
    '@chromatic-com/storybook',
    '@storybook/addon-interactions'
  ],
  'framework': {
    'name': '@storybook/react-webpack5',
    'options': {}
  },
  'staticDirs': [
    '..\\public'
  ],
  webpackFinal: async (config) => {
    if (config.resolve) {
      config.resolve.alias = {
        ...config.resolve.alias,
        '@': path.resolve(__dirname, '../src') // ✅ '@'를 'src' 디렉토리로 매핑
      };
    }
    return config;
  }
};
export default config;
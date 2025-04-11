/** @type {import('tailwindcss').Config} */
import daisyui from 'daisyui';

export const content = [
  './src/**/*.{js,jsx,ts,tsx}',
  './.storybook/**/*.{js,ts,jsx,tsx,mdx}'
];
export const darkMode = 'class';
export const theme = {
  extend: {}
};
export const plugins = [daisyui];

export const daisyuiConfig = {
  themes: ['light', 'dark'],
};


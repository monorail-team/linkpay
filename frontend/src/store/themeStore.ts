import {create} from 'zustand';

interface ThemeStore {
  theme: 'light' | 'dark';
    toggleTheme: () => void;
}

export const useThemeStore = create<ThemeStore>((set, get) => ({
    theme : 'light',
    toggleTheme: () => {
        set({theme: get().theme === 'light' ? 'dark' :  'light'})
    },

}));
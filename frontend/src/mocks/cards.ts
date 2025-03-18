import { Card } from '@/model/Card';

export const cards: Card[] = [
  {
    title: "카드명",
    description: "Description",
    expireDate: "25.12.23",
    used: 54000,
    limit: 100000,
    type: 'myWallet'
  },
  {
    title: "다른 카드",
    description: "Another Card",
    expireDate: "01.01.24",
    used: 30000,
    limit: 80000,
    type: 'sharedWallet'
  },
  {
    title: "세번째 카드",
    description: "Third Card",
    expireDate: "12.05.24",
    used: 10000,
    limit: 50000,
    type: 'otherWallet'
  },
];

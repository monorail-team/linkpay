import { Card } from '@/model/Card';

export const cards: Card[] = [
  {
    id: 1,
    cardName: "카드명",
    expiredAt: "25.12.23",
    usedpoint: 54000,
    limitPrice: 100000,
    cardType: 'OWNED',
    cardColor: '#F19DAB',
  },
  {
    id: 2,
    cardName: "다른 카드",
    expiredAt: "01.01.24",
    usedpoint: 30000,
    limitPrice: 80000,
    cardType: 'SHARED',
    cardColor: '#FFD681',
  },
];

import { Card } from '@/model/Card';

export const cards: Card[] = [
  {
    linkCardId: 1,
    cardName: "카드명",
    expireAt: "25.12.23",
    point: 54000,
    limitPrice: 100000,
    cardType: 'OWNED',
    cardColor: '#F19DAB',
  },
  {
    linkCardId: 2,
    cardName: "다른 카드",
    expireAt: "01.01.24",
    point: 30000,
    limitPrice: 80000,
    cardType: 'SHARED',
    cardColor: '#FFD681',
  },
  {
    linkCardId: 3,
    cardName: "세번째 카드",
    expireAt: "12.05.24",
    point: 10000,
    limitPrice: 50000,
    cardType: 'RECEIVED',
    cardColor: '#EEFFBA',
  },
  {
    linkCardId: 4,
    cardName: "세번째 카드",
    expireAt: "12.05.24",
    point: 10000,
    limitPrice: 50000,
    cardType: 'RECEIVED',
    cardColor: '#B7F8FF',
  },
];

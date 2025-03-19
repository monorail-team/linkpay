export interface Card {
    cardName: string;
    expireAt: string;
    point: number;
    limitPrice: number;
    cardColor: string;
    cardType: 'OWNED' | 'SHARED' | 'RECEIVED';
  }
  
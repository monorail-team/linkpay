export interface Card {
    id: number;
    cardName: string;
    expireAt: string;
    usedpoint: number;
    limitPrice: number;
    cardColor: string;
    cardType: 'OWNED' | 'SHARED';
    linkedWalletId?: number;
  }
  
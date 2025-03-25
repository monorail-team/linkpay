export interface Card {
    id: number;
    cardName: string;
    expiredAt: string;
    usedpoint: number;
    limitPrice: number;
    cardColor: string;
    cardType: 'OWNED' | 'SHARED';
    linkedWalletId?: number;
  }
  
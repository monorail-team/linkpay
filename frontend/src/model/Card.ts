export interface Card {
    linkCardId: number;
    limitPrice: number;
    cardType?: 'OWNED' | 'SHARED';
    cardColor: string;
    cardName: string;
    expiredAt: string;
    usedPoint: number;
    state?: string;
    linkedWalletId?: number;
  }
  
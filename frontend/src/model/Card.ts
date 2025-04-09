export interface Card {
    linkCardId: string;
    limitPrice: number;
    cardType?: 'OWNED' | 'SHARED';
    cardColor: string;
    cardName: string;
    expiredAt: string;
    usedPoint: number;
    state?: string;
    linkedWalletId?: number;
    username?:string;
    tab?:string
  }
  
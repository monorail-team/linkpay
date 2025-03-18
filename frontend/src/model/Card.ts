export interface Card {
    title: string;
    description: string;
    expireDate: string;
    used: number;
    limit: number;
    type: 'myWallet' | 'sharedWallet' | 'otherWallet';
  }
  
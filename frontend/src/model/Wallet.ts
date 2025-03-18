export interface Wallet {
    type: 'myWallet' | 'sharedWallet' | 'otherWallet';
    remainingPoints: number;
}
  
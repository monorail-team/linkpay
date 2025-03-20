export interface Wallet {
    type: 'OWNED' | 'SHARED';
    remainingPoints: number;
}
  
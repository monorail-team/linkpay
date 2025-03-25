export interface Wallet {
    id: number;
    type: 'OWNED' | 'SHARED';
    remainingPoints: number;
}
  
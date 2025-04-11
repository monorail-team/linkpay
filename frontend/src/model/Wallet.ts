export interface Wallet {
    id: string;
    type: 'OWNED' | 'SHARED';
    remainingPoints: number;
}
  
export interface MyWalletHistory {
    walletHistoryId: number;
    point: number;
    transactionDate: string;
    type: 'DEPOSIT' | 'WITHDRAWAL';
    afterPoint : number;
    cardName : string;
}
  
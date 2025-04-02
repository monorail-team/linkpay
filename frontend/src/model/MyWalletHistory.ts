export interface MyWalletHistory {
    walletHistoryId: number;
    amount: number;
    remaining : number;
    transactionType: 'DEPOSIT' | 'WITHDRAWAL';
    time : string;
}
  
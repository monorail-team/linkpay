export interface MyWalletHistory {
    walletHistoryId: string;
    amount: number;
    remaining : number;
    transactionType: 'DEPOSIT' | 'WITHDRAWAL';
    time : string;
    linkCardName?: string| null;
}
  
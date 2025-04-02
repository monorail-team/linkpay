import { MyWalletHistory } from '@/model/MyWalletHistory';
export const walletHistory: MyWalletHistory[] = [
    { walletHistoryId:1, amount: 100000, time: '03.11', transactionType: 'DEPOSIT', remaining: 870000 },
    { walletHistoryId:2, amount: 30000, time: '03.11', transactionType: 'WITHDRAWAL', remaining: 770000},
    { walletHistoryId:3, amount: 100000, time: '03.09', transactionType: 'WITHDRAWAL', remaining: 800000},
    { walletHistoryId:4, amount: 50000, time: '03.05', transactionType: 'DEPOSIT', remaining: 900000},
];
  
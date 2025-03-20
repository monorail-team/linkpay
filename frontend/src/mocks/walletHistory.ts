import { MyWalletHistory } from '@/model/MyWalletHistory';
export const walletHistory: MyWalletHistory[] = [
    { walletHistoryId:1, point: 100000, transactionDate: '03.11', type: 'DEPOSIT', afterPoint: 870000 , cardName: '카드1' },
    { walletHistoryId:2, point: 30000, transactionDate: '03.11', type: 'WITHDRAWAL', afterPoint: 770000,cardName: '카드2'},
    { walletHistoryId:3, point: 100000, transactionDate: '03.09', type: 'WITHDRAWAL', afterPoint: 800000,cardName: '카드3'},
    { walletHistoryId:4, point: 50000, transactionDate: '03.05', type: 'DEPOSIT', afterPoint: 900000,cardName: '카드4' },
];
  
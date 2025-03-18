import { Wallet } from '@/model/Wallet';

export const wallets: Wallet[] = [
  { type: 'myWallet', remainingPoints: 50000 },
  { type: 'sharedWallet', remainingPoints: 25000 },
  // 'otherWallet'은 남은 포인트를 보여주지 않으므로 데이터가 필요 없거나 0으로 처리
];

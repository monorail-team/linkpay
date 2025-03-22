import { MyWalletHistory } from "@/model/MyWalletHistory";

export interface WalletData {
    userWalletName: string;
    availablePoint: number;
    walletHistory: MyWalletHistory[];
}
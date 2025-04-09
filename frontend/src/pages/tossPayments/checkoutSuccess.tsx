// src/pages/SuccessPage.tsx
import axios from "axios";
import { useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";

const base_url = process.env.REACT_APP_API_URL;

const CheckoutSuccess: React.FC = () => {
  const [isConfirmed, setIsConfirmed] = useState(false);
  const [searchParams] = useSearchParams();
  const paymentKey = searchParams.get("paymentKey");
  const orderId = searchParams.get("orderId");
  const amount = searchParams.get("amount");
  const returnPage = searchParams.get("returnPage");
  const walletId = searchParams.get("walletId");
  const returnUrl = `/${returnPage}` + (walletId === "null" ? `` : `/${walletId}`);
  const navigate = useNavigate();

  const confirmPayment = async() => {
    // const response = await fetch("/sandbox-dev/api/v1/payments/confirm", {
    //   method: "POST",
    //   headers: {
    //     "Content-Type": "application/json",
    //   },
    //   body: JSON.stringify({
    //     paymentKey,
    //     orderId,
    //     amount,
    //   }),
    // });

    // if (response.ok) {
    //   setIsConfirmed(true);
    // }
    await handleDeposit();
    setIsConfirmed(true);
  }

  const handleRedirect = () => {
    navigate(returnUrl, {replace: true});
  }

  const handleDeposit = async () => {
    try {
        const token = sessionStorage.getItem('accessToken');
        const response = await axios.patch(`${base_url}/api/my-wallets/charge`, 
            { amount },
            {
                headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
                },
            }
        );
      } catch (error) {
        console.error(error);
      }
  }

  return (
    <div className="w-full p-4">
        {isConfirmed ? (
            <div className="flex flex-col items-center w-full max-w-[540px] mx-auto space-y-6">
                <img
                    src="https://static.toss.im/illusts/check-blue-spot-ending-frame.png"
                    width="120"
                    height="120"
                    alt="결제 완료"
                />
                <h2 className="text-2xl font-semibold">결제를 완료했어요</h2>
                <div className="w-full space-y-2">
                    <div className="flex justify-between">
                    <span className="text-gray-600 font-medium">결제 금액</span>
                    <span id="amount" className="text-gray-800">{amount}</span>
                    </div>
                    <div className="flex justify-between">
                    <span className="text-gray-600 font-medium">주문번호</span>
                    <span id="orderId" className="text-gray-800">{orderId}</span>
                    </div>
                    <div className="flex justify-between">
                    <span className="text-gray-600 font-medium">paymentKey</span>
                    <span id="paymentKey" className="text-gray-800">{paymentKey}</span>
                    </div>
                </div>
                <button
                    className="w-full bg-blue-500 hover:bg-blue-600 text-white font-medium py-2 px-4 rounded"
                    onClick={handleRedirect}
                >
                    페이지 돌아가기기
                </button>
            </div>
        ) : (
            <div className="flex flex-col items-center w-full max-w-[540px] mx-auto space-y-6">
                <div className="flex flex-col items-center space-y-4">
                    <img
                    src="https://static.toss.im/lotties/loading-spot-apng.png"
                    width="120"
                    height="120"
                    alt="로딩 중"
                    />
                    <h2 className="text-xl font-semibold text-center">결제 요청까지 성공했어요.</h2>
                    <h4 className="text-center text-gray-600">결제 승인하고 완료해보세요.</h4>
                </div>
                <button
                    className="w-full bg-blue-500 hover:bg-blue-600 text-white font-medium py-2 px-4 rounded"
                    onClick={confirmPayment}
                >
                    결제 승인하기
                </button>
            </div>
        )}
    </div>
  );
}

export default CheckoutSuccess;

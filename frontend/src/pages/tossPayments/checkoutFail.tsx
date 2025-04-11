import React from "react";
import { useSearchParams, useNavigate } from "react-router-dom";

const CheckoutFail: React.FC = () => {
  const [searchParams] = useSearchParams();
  const errorCode = searchParams.get("code");
  const errorMessage = searchParams.get("message");
  const returnPage = searchParams.get("returnPage");
  const rawWalletId  = searchParams.get("walletId");
  const walletId =
  rawWalletId && rawWalletId !== "undefined" && rawWalletId !== "null"
    ? rawWalletId
    : null;
  const returnUrl = `/${returnPage}` + (walletId ? `/${walletId}` : '');
  const navigate = useNavigate();

  const handleRedirect = () => {
    navigate(returnUrl, {replace: true});
  }

  return (
    <div className="wrapper w-100 flex flex-col">
      <div className="flex flex-col items-center w-100 max-w-540 space-y-6">
        <img
          src="https://static.toss.im/lotties/error-spot-apng.png"
          width="120"
          height="120"
          alt="에러 이미지"
        />
        <h2 className="title">결제를 실패했어요</h2>
        <div className="response-section w-100">
          <div className="flex justify-between">
            <span className="response-label">code</span>
            <span className="response-text">{errorCode}</span>
          </div>
          <div className="flex justify-between">
            <span className="response-label">message</span>
            <span className="response-text">{errorMessage}</span>
          </div>
        </div>

        <button
            className="w-full bg-blue-500 hover:bg-blue-600 text-white font-medium py-2 px-4 rounded"
            onClick={handleRedirect}
        >
          페이지 돌아가기기
        </button>
      </div>
    </div>
  );
}

export default CheckoutFail;

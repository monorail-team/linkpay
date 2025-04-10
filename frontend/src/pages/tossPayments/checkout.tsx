import React from "react";
import { useEffect, useState } from "react";
import { loadTossPayments, ANONYMOUS } from "@tosspayments/tosspayments-sdk";
import { useSearchParams, useNavigate } from "react-router-dom";

const clientKey = "test_gck_docs_Ovk5rk1EwkEbP0W43n07xlzm";

const Checkout: React.FC = () => {
  const [widgets, setWidgets] = useState<any>(null);
  const [searchParams] = useSearchParams();
  const amount = Number(searchParams.get("amount")) || 0;
  const returnPage = searchParams.get("returnPage");
  const walletId = searchParams.get("walletId");
  const returnUrl = `/${returnPage}` + (walletId === "null" ? `` : `/${walletId}`);
  const navigate = useNavigate();
  useEffect(() => {
    (async () => {
      const tossPayments = await loadTossPayments(clientKey);
      const widgets = tossPayments.widgets({ customerKey: ANONYMOUS });
      setWidgets(widgets);
    })();
  }, []);

  useEffect(() => {
    if (!widgets) return;
    (async () => {
      await widgets.setAmount({ currency: "KRW", value: amount });
      await Promise.all([
        widgets.renderPaymentMethods({ selector: "#payment-method", variantKey: "DEFAULT" }),
        widgets.renderAgreement({ selector: "#agreement", variantKey: "AGREEMENT" }),
      ]);
    })();
  }, [widgets]);

  return (
    <div className="space-y-4">
      <div id="payment-method" className="w-full" />
      <div id="agreement" className="w-full" />
      <button
        className="btn primary w-full"
        onClick={async () => {
          try {
            await widgets?.requestPayment({
              orderId: `ORDER-${Date.now()}`,
              orderName: "상품 결제",
              customerName: "홍길동",
              customerEmail: "customer@example.com",
              successUrl: window.location.origin + `/checkout/success?returnPage=${returnPage}&walletId=${walletId}`,
              failUrl: window.location.origin + `/checkout/fail`,
            });
          } catch (e) {
            console.error("결제 요청 실패", e);
            navigate(returnUrl, {replace: true});
          }
        }}
      >
        결제하기
      </button>
    </div>
  );
}

export default Checkout;

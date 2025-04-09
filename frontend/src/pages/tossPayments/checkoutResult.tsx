import { useEffect } from "react";
import { useNavigate, useSearchParams, useParams } from "react-router-dom";

const CheckoutResult: React.FC = () => {
  const { type } = useParams<{ type: "success" | "fail" }>();
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const returnUrl = searchParams.get("returnUrl") || "/";

  useEffect(() => {
    console.log(type);
    if (type === "success") {
      // 결제 승인 요청
      const confirm = async () => {
        // const paymentKey = searchParams.get("paymentKey");
        // const orderId = searchParams.get("orderId");
        // const amount = searchParams.get("amount");

        // await fetch("/api/payments/confirm", {
        //   method: "POST",
        //   headers: { "Content-Type": "application/json" },
        //   body: JSON.stringify({ paymentKey, orderId, amount }),
        // });

        navigate(returnUrl + `/success`, { replace: true });
      };
      confirm();
    } else {
      // 실패 시 바로 리턴
      navigate(returnUrl + "?payment=fail", { replace: true });
    }
  }, []);

  return <div>{type === "success" ? "결제 확인 중..." : "결제 실패"}</div>;
}

export default CheckoutResult;

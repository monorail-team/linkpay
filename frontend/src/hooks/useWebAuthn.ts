import { useState } from 'react';
import axios from 'axios';

const base_url = process.env.REACT_APP_API_URL;
/* 
    문자열을 Uint8Array로 변환하는 유틸 함수
    WebAuthn API 스펙상 challenge 필드는 반드시 바이너리 데이터(ArrayBuffer 또는 이를 래핑한 Uint8Array)여야 함함
*/
function strToUint8Array(str: string): Uint8Array {
  return Uint8Array.from(str, c => c.charCodeAt(0));
}

interface WebAuthnHook {
  handleWebAuthn: () => Promise<void>;
  loading: boolean;
}

const useWebAuthn = (): WebAuthnHook => {
  const [loading, setLoading] = useState(false);

  const token = sessionStorage.getItem('accessToken');

  // 결제 수행 function
  const performAuthentication = async () => {

    // 1. 인증용 Challenge 요청

    const authChallengeRes = await axios.get(`${base_url}/api/webauthn/auth-challenge`, {
      headers: { Authorization: `Bearer ${token}` }
    });
    const authChallenge = authChallengeRes.data.challenge;
    const credentialId = authChallengeRes.data.credentialId;

    // 2. authenticationOptions 구성
    const publicKeyCredentialRequestOptions: PublicKeyCredentialRequestOptions = {
      challenge: strToUint8Array(authChallenge),
      allowCredentials: [
        {
          id: strToUint8Array(credentialId),
          type: "public-key",
        },
      ],
      timeout: 60000,
      userVerification: "required",
    };

    // 3. Credential을 이용한 인증 (서명 생성)
    const assertion = await navigator.credentials.get({
      publicKey: publicKeyCredentialRequestOptions,
    });
    if (!assertion) {
      throw new Error("인증 실패");
    }

    // 4. 생성된 assertion(서명) 정보를 백엔드에 전송하여 결제 처리
    await axios.post(
      `${base_url}/api/webauthn/authenticate`,
      { assertion },
      { headers: { Authorization: `Bearer ${token}` } }
    );
    console.log("인증 및 결제 처리 성공");
  };

  //전체 기능 function
  const handleWebAuthn = async () => {
    setLoading(true);
    try {
      // 1. 백엔드에 현재 사용자의 등록 여부 확인
      const regStatusResponse = await axios.get(`${base_url}/api/webauthn/registration-status`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      const isRegistered = regStatusResponse.data.registered;

      if (!isRegistered) {
        // 신규 사용자 등록 플로우
        const challengeRes = await axios.get(`${base_url}/api/webauthn/register-challenge`, {
          headers: { Authorization: `Bearer ${token}` }
        });
        const challenge = challengeRes.data.challenge; // 등록용 Challenge (Base64url 문자열)

        const publicKeyCredentialCreationOptions: PublicKeyCredentialCreationOptions = {
          challenge: strToUint8Array(challenge),
          rp: { name: "LinkPay", id: window.location.hostname },
          user: {
            id: strToUint8Array("user-unique-id"), // 실제 사용자 ID로 교체
            name: "user@example.com",              // 실제 사용자 이메일로 교체
            displayName: "User Name",
          },
          pubKeyCredParams: [{ type: "public-key", alg: -7 }], // ES256 알고리즘 사용
          authenticatorSelection: {
            authenticatorAttachment: "platform",
            userVerification: "required",
          },
          timeout: 60000,
        };

        // Credential 생성 (등록)
        const credential = await navigator.credentials.create({
          publicKey: publicKeyCredentialCreationOptions,
        });
        if (!credential) {
          throw new Error("Credential 생성 실패");
        }

        // 생성된 credential 정보를 백엔드에 전송하여 등록 처리
        await axios.post(
          `${base_url}/api/webauthn/register`,
          { credential }, // 실제 상황에서는 credential 데이터를 직렬화하여 전송
          { headers: { Authorization: `Bearer ${token}` } }
        );
        console.log("등록 성공");

        // 등록 후 바로 인증(결제) 플로우로 자동 전환
        await performAuthentication();
      } else {
        // 기존 사용자: 바로 인증(결제) 플로우 실행
        await performAuthentication();
      }
    } catch (error) {
      console.error("WebAuthn 에러", error);
      // 에러 처리: 사용자에게 안내 메시지 표시 등
    } finally {
      setLoading(false);
    }
  };

  return { handleWebAuthn, loading };
};

export default useWebAuthn;

import { useState } from 'react';
import axios from 'axios';

const base_url = process.env.REACT_APP_API_URL;
const email = "sungiljin98@gmail.com";
/* 
    문자열을 Uint8Array로 변환하는 유틸 함수
    WebAuthn API 스펙상 challenge 필드는 반드시 바이너리 데이터(ArrayBuffer 또는 이를 래핑한 Uint8Array)여야 함함
*/
function strToUint8Array(str: string): Uint8Array {
  return Uint8Array.from(str, c => c.charCodeAt(0));
}

interface WebAuthnHook {
  handleWebAuthn: () => Promise<PublicKeyCredential | null>;
  loading: boolean;
}

const useWebAuthn = (): WebAuthnHook => {
  const [loading, setLoading] = useState(false);

  const token = sessionStorage.getItem('accessToken');

  const fetchUserData = async () => {
   /**
    * userEmail을 로컬 스토리지나 세션 스토리지에서 가져와야함.
    */
    if (!email) {
      throw new Error("User email is not available");
    }
    const response = await axios.get(`${base_url}/api/members?email=${email}`, {
      headers: { Authorization: `Bearer ${token}` }
    });
    return response.data; 
  };

  //  WebAuthn 인증을 수행하고 memberSignature을 반환하는 함수
  const performAuthentication = async (): Promise<PublicKeyCredential | null> => {

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
    const memberSignature = await navigator.credentials.get({
      publicKey: publicKeyCredentialRequestOptions,
    }) as PublicKeyCredential;

    if (!memberSignature) {
      throw new Error("인증 실패");
    }

    
    return memberSignature;
  };

  //전체 기능 function
  const handleWebAuthn = async (): Promise<PublicKeyCredential | null> => {
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
        const member = await fetchUserData();
        console.log(window.location.hostname);
        const publicKeyCredentialCreationOptions: PublicKeyCredentialCreationOptions = {
          challenge: strToUint8Array(challenge),
          rp: { name: "LinkPay", id: window.location.hostname },
          user: {
            id: strToUint8Array(member.memberId), // 실제 사용자 ID로 교체
            name: member.email,              // 실제 사용자 이메일로 교체
            displayName: "test-name",
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
        }) as PublicKeyCredential;

        if (!credential) {
          throw new Error("Credential 생성 실패");
        }

        const arrayBufferToBase64 = (buffer: ArrayBuffer): string => {
          const bytes = new Uint8Array(buffer);
          let binary = "";
          for (let i = 0; i < bytes.byteLength; i++) {
            binary += String.fromCharCode(bytes[i]);
          }
          return window.btoa(binary);
        };
        
        const credentialObj = credential;
        const response = credentialObj.response as AuthenticatorAttestationResponse;
        
        const payload = {
          credentialId: credentialObj.id,
          clientDataJSON: arrayBufferToBase64(response.clientDataJSON),
          attestationObject: arrayBufferToBase64(response.attestationObject),
        };
        
        await axios.post(`${base_url}/api/webauthn/register`, payload, {
          headers: { Authorization: `Bearer ${token}` }
        });
        console.log("등록 성공");

        // 등록 후 바로 인증(결제) 플로우로 자동 전환
        const memberSignature = await performAuthentication();
        return memberSignature;
      } else {
        // 기존 사용자: 바로 인증(결제) 플로우 실행
        const memberSignature = await performAuthentication();
        return memberSignature;
      }
    } catch (error) {
      console.error("WebAuthn 에러", error);
      return null;
    } finally {
      setLoading(false);
    }
  };

  return { handleWebAuthn, loading };
};

export default useWebAuthn;

import { useState } from 'react';
import axios, { AxiosResponse } from 'axios';

const base_url = process.env.REACT_APP_API_URL;
interface AuthChallengeResponse {
  challenge: string;
  credentialId: string;
}

/* 
    문자열을 Uint8Array로 변환하는 유틸 함수
    WebAuthn API 스펙상 challenge 필드는 반드시 바이너리 데이터(ArrayBuffer 또는 이를 래핑한 Uint8Array)여야 함함
*/
function base64UrlToUint8Array(base64UrlString: string): Uint8Array {
  // 필요한 패딩 추가: 길이가 4의 배수가 되도록 '=' 문자를 추가
  const padding = '='.repeat((4 - base64UrlString.length % 4) % 4);
  // base64url 형식을 일반 base64 형식으로 변환
  const base64 = (base64UrlString + padding)
    .replace(/-/g, '+')
    .replace(/_/g, '/');
  // base64 디코딩하여 바이너리 문자열로 변환
  const binaryString = window.atob(base64);
  const bytes = new Uint8Array(binaryString.length);
  for (let i = 0; i < binaryString.length; i++) {
    bytes[i] = binaryString.charCodeAt(i);
  }
  return bytes;
}

interface WebAuthnHook {
  handleWebAuthn: () => Promise<string  | null>;
  loading: boolean;
}

const useWebAuthn = (): WebAuthnHook => {
  const [loading, setLoading] = useState(false);

  const token = sessionStorage.getItem('accessToken');

  const fetchUserData = async () => {
    const response = await axios.get(`${base_url}/api/mypage`, {
      headers: { Authorization: `Bearer ${token}` }
    });
    return response.data; 
  };

  const arrayBufferToBase64 = (buffer: ArrayBuffer): string => {
    const bytes = new Uint8Array(buffer);
    let binary = "";
    for (let i = 0; i < bytes.byteLength; i++) {
      binary += String.fromCharCode(bytes[i]);
    }
    return window.btoa(binary);
  };

  //  WebAuthn 인증을 수행하고 assertionResult을 반환하는 함수
  const performAuthentication = async (): Promise<string | null> => {
    try{
      // 1. 인증용 Challenge 요청
      const authChallengeRes = await axios.get(`${base_url}/api/webauthn/authenticate/challenge`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      const authChallenge = authChallengeRes.data.challenge;
      const credentialId = authChallengeRes.data.credentialId;

      // 2. authenticationOptions 구성
      const publicKeyCredentialRequestOptions: PublicKeyCredentialRequestOptions = {
        challenge: base64UrlToUint8Array(authChallenge),
        allowCredentials: [
          {
            id: base64UrlToUint8Array(credentialId),
            type: "public-key",
          },
        ],
        timeout: 60000,
        userVerification: "required",
      };

      // 3. Credential을 이용한 인증 (서명 생성)
      const assertionResult = await navigator.credentials.get({
        publicKey: publicKeyCredentialRequestOptions,
      }) as PublicKeyCredential;

      if (!assertionResult) {
        throw new Error("인증 실패");
      }

      const response = assertionResult.response as AuthenticatorAssertionResponse;
      const clientDataJSON = arrayBufferToBase64(response.clientDataJSON);
      const authenticatorData = arrayBufferToBase64(response.authenticatorData);
      const signature = arrayBufferToBase64(response.signature);

      const verificationPayload = {
        credentialId: assertionResult.id,
        clientDataJSON,
        authenticatorData,
        signature,
      };

  
      const verificationRes = await axios.post<string>(`${base_url}/api/webauthn/authenticate`, verificationPayload, {
        headers: { Authorization: `Bearer ${token}` }
      });
      console.log("인증 성공", verificationRes.data);
      return verificationRes.data;
    }catch (error) {
      console.error("인증 에러", error);
      return null;
    }
  };

  //전체 기능 function
  const handleWebAuthn = async (): Promise<string  | null> => {
    setLoading(true);
    let authChallengeRes: AxiosResponse<AuthChallengeResponse> | undefined;
    try {
      let isRegistered = true;
      

      try {
        authChallengeRes = await axios.get(`${base_url}/api/webauthn/authenticate/challenge`, {
          headers: { Authorization: `Bearer ${token}` }
        });
      } catch (error: unknown) {
        if (axios.isAxiosError(error) && error.response?.data.code === 4004) {
          isRegistered = false;
          console.log("등록된 인증기가 없습니다. 신규 등록이 필요합니다.");
        } else {
          throw error;
        }
      }

      if (!isRegistered) {
        // 신규 사용자 등록 플로우
        const challengeRes = await axios.get(`${base_url}/api/webauthn/register/challenge`, {
          headers: { Authorization: `Bearer ${token}` }
        });
        // 등록용 Challenge (Base64url 문자열)
        const challenge = challengeRes.data.challenge; 
        
        //유저 데이터
        const member = await fetchUserData();

        const publicKeyCredentialCreationOptions: PublicKeyCredentialCreationOptions = {
          challenge: base64UrlToUint8Array(challenge),
          rp: { name: "LinkPay", id: window.location.hostname },
          user: {
            id: base64UrlToUint8Array(member.memberId), 
            name: member.email,             
            displayName: member.username,
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

       
        
        const credentialObj = credential;
        const response = credentialObj.response as AuthenticatorAttestationResponse;
        
        const payload = {
          credentialId: credentialObj.id,
          attestationObject: arrayBufferToBase64(response.attestationObject),
        };
        
        await axios.post(`${base_url}/api/webauthn/register`, payload, {
          headers: { Authorization: `Bearer ${token}` }
        });
        console.log("등록 성공");

        // 등록 후 바로 인증(결제) 플로우로 자동 전환
        const assertionResult = await performAuthentication();
        return assertionResult;
      } else {
        // 기존 사용자: 바로 인증(결제) 플로우 실행
        const assertionResult = await performAuthentication();
        return assertionResult;
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

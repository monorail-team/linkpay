// src/hooks/useNfcWriter.ts
import { useState } from 'react';

const useNfcWriter = () => {
  const [isWriting, setIsWriting] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  const isNFCAvailable = (): boolean => {
    return typeof window !== 'undefined' &&
      'NDEFReader' in window &&
      typeof window.NDEFReader === 'function';
  };


  const writeNfcTag = async (text: string) => {
    setIsWriting(true);
    setError(null);
    setSuccess(false);
    console.log('try 문 진입');
    try {
      if (isNFCAvailable()) {
        console.log('NFC Wirter 생성 시도');
        const writer = new (window as any).NDEFReader();
        console.log('쓰기 시도!');
        await writer.write({
          records: [
            {
              recordType: 'text',
              data: text
            }
          ]
        });
        console.log('쓰기 성공!');
        setSuccess(true);
      } else {
        setError('NFC를 지원하지 않는 브라우저입니다.');
      }
    } catch (err) {
      setError((err as Error).message);
    } finally {
      setIsWriting(false);
    }
  };

  return {
    writeNfcTag,
    isWriting,
    error,
    success
  };
};

export default useNfcWriter;

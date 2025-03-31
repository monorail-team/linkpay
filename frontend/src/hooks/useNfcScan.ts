// src/hooks/useNfcScan.ts
import { useEffect, useRef } from 'react';

interface UseNfcScanOptions {
  onRead: (data: string) => void;
  onError?: (error: Error) => void;
  cardId?: string; // 필요 시 전달받아 API 요청에 활용 가능
}

const useNfcScan = ({ onRead, onError }: UseNfcScanOptions) => {
  const ndefRef = useRef<any>(null);

  useEffect(() => {
    if (!('NDEFReader' in window)) {
      onError?.(new Error('NFC를 지원하지 않는 브라우저입니다.'));
      return;
    }

    const startScan = async () => {
      try {
        const ndef = new (window as any).NDEFReader();
        ndefRef.current = ndef;

        await ndef.scan();
        console.log('📡 NFC 스캔 시작됨');

        ndef.onreading = (event: any) => {
          const message = event.message;
          for (const record of message.records) {
            if (record.recordType === 'text') {
              const decoder = new TextDecoder(record.encoding || 'utf-8');
              const data = decoder.decode(record.data);
              console.log('📥 NFC 데이터:', data);
              onRead(data);
            }
          }
        };

        ndef.onerror = () => {
          onError?.(new Error('NFC 스캔 중 오류 발생'));
        };
      } catch (err: any) {
        onError?.(err);
      }
    };

    startScan();

    return () => {
      if (ndefRef.current?.abort) {
        ndefRef.current.abort();
      }
      console.log('🛑 NFC 스캔 종료');
    };
  }, []);
};

export default useNfcScan;

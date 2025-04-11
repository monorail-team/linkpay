import { useEffect, useRef } from 'react';

const useNfcReader = (
  onRead: (data: string) => void,
  onError?: (err: string) => void
) => {
  const ndefRef = useRef<any>(null);

  const startScan = async () => {
    if (!('NDEFReader' in window)) {
      onError?.('Web NFC를 지원하지 않는 브라우저입니다.');
      return;
    }

    try {
      const ndef = new (window as any).NDEFReader();
      ndefRef.current = ndef;

      // 이벤트 등록 먼저
      ndef.onreading = (event: any) => {
        const message = event.message;
        for (const record of message.records) {
          console.log('data', record);
          if (record.recordType === 'text') {
            const textDecoder = new TextDecoder(record.encoding || 'utf-8');
            const data = textDecoder.decode(record.data);
            onRead(data);

          }
        }
      };

      ndef.onerror = () => {
        onError?.('NFC 리딩 중 오류 발생');
      };

      await ndef.scan();
      console.log('📡 NFC 스캔 시작됨');
    } catch (err: any) {
      onError?.(`NFC 스캔 실패: ${err.message}`);
    }
  };

  useEffect(() => {
    // expose startScan
    (window as any).__startNfcScan = startScan;

    return () => {
      if (ndefRef.current) {
        ndefRef.current.onreading = null;
        ndefRef.current.onerror = null;
      }
    };
  }, []);
};

export default useNfcReader;

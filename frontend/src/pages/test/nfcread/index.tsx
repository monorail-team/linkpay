// src/pages/NfcWritePage.tsx
import React, { useState } from 'react';
import useNfcReader from '@/hooks/useNfcReader';

const NfcRead: React.FC = () => {
  const [error, setError] = useState('');
  const [readValue, setReadValue] = useState('');

  useNfcReader(
    (data) => {
      console.log('📥 NFC 데이터 읽음:', data);
      setReadValue(data);
    },
    (err) => {
      console.warn('🚫 NFC 오류:', err);
      setError(err);
    }
  );

  return (
    <div className="flex flex-col items-center justify-center h-screen bg-gray-100 px-4">
      <h1 className="text-2xl font-bold mb-6">NFC 태그 읽기</h1>

      <button
        onClick={() => (window as any).__startNfcScan()}
        className="px-6 py-2 bg-blue-600 text-white rounded"
      >
        NFC 스캔 시작
      </button>

      {readValue && <p className="text-green-600 mt-4">✅ 읽음: {readValue}</p>}
      {error && <p className="text-red-500 mt-4">{error}</p>}
    </div>
  );
};

export default NfcRead;

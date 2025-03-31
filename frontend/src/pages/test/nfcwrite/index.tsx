// src/pages/NfcWritePage.tsx
import React, { useState } from 'react';
import useNfcWriter from '@/hooks/useNfcWriter';

const NfcWrite: React.FC = () => {
  const [message, setMessage] = useState('');
  const { writeNfcTag, isWriting, error, success } = useNfcWriter();

  console.log(window);
  const handleWrite = async () => {
    await writeNfcTag(message);
  };

  return (
    <div className="flex flex-col items-center justify-center h-screen bg-gray-100 px-4">

      <h1 className="text-2xl font-bold mb-6">NFC 태그 쓰기</h1>

      <textarea
        className="w-full max-w-md h-32 border border-gray-300 rounded p-2 mb-4"
        value={message}
        onChange={(e) => setMessage(e.target.value)}
        placeholder="NFC에 쓸 메시지를 입력하세요"
      />

      <button
        onClick={handleWrite}
        disabled={isWriting}
        className="px-6 py-2 bg-blue-600 text-white rounded disabled:opacity-50"
      >
        {isWriting ? '쓰기 중...' : 'NFC 쓰기'}
      </button>

      {error && <p className="text-red-500 mt-4">{error}</p>}
      {success && <p className="text-green-600 mt-4">✅ NFC 태그에 성공적으로 기록되었습니다!</p>}
    </div>
  );
};

export default NfcWrite;

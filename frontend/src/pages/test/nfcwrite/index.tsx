// src/pages/NfcWritePage.tsx
import React, { useEffect, useState } from 'react';
import useNfcWriter from '@/hooks/useNfcWriter';
import axios from 'axios';

const base_url = process.env.REACT_APP_API_URL;

const NfcWrite: React.FC = () => {
  const [message, setMessage] = useState('');
  const { writeNfcTag, isWriting, error, success } = useNfcWriter();
  const [stores, setStores] = useState([]);
  const [storeName, setStoreName] = useState('');
  const [amount, setAmount] = useState(0);
  const [selectedStoreId, setSelectedStoreId] = useState<string | null>(null);

  // console.log(window);
  const handleWrite = async () => {
    await writeNfcTag(message);
  };

  const handleCreateStore = async () => {
    await createStore();
    await readAllStores();
  };

  useEffect(() => {
    readAllStores();
  }, []);

  const createPaymentTransaction = async () => {
    try {
      const accessToken = sessionStorage.getItem('accessToken');
      if (!accessToken) {
        console.error('Access token not found');
        return;
      }
      if (!selectedStoreId) {
        console.error('no store id found');
        return;
      }
      const response = await axios.post(`${base_url}/api/stores/${selectedStoreId}/transactions`, {
        amount
      }, {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${accessToken}`
        }
      });
      setMessage(response.data.data);
      console.log(response.data);
    } catch (err) {
      console.error(err);
    }
  };

  const createStore = async () => {
    try {
      const accessToken = sessionStorage.getItem('accessToken');
      if (!accessToken) {
        console.error('Access token not found');
        return;
      }
      await axios.post(`${base_url}/api/stores`, {
        storeName
      }, {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${accessToken}`
        }
      });
    } catch (err) {
      console.error(err);
    }
  };

  const readAllStores = async () => {
    try {
      const accessToken = sessionStorage.getItem('accessToken');
      if (!accessToken) {
        console.error('Access token not found');
        return;
      }
      const response = await axios.get(`${base_url}/api/stores`, {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${accessToken}`
        }
      });
      console.log(response.data);
      setStores(response.data.stores);
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="flex flex-col items-center justify-center h-screen bg-gray-100 px-4">

      <h1 className="text-2xl font-bold mb-6">NFC 태그 쓰기</h1>

      <div className="mt-8 w-full max-w-md gap-3 pb-3">
        <input
          type="text"
          value={storeName}
          onChange={(e) => setStoreName(e.target.value)}
          placeholder="새 가맹점 이름 입력"
          className="w-full border border-gray-300 rounded p-2 mb-2"
        />
        <button
          onClick={() => handleCreateStore()}
          className="w-full px-4 py-2 bg-green-600 text-white rounded"
        >
          create
        </button>
      </div>

      <div className="grid grid-cols-3 gap-2 w-full max-w-md h-40 overflow-y-auto">
        {stores.map((store: any) => (
          <div
            key={store.storeId}
            onClick={() =>
              setSelectedStoreId(selectedStoreId === store.storeId ? null : store.storeId)
            }
            className={`cursor-pointer p-3 rounded border text-center transition duration-150 ease-in-out ${
              selectedStoreId === store.storeId
                ? 'border-blue-600 bg-blue-50 shadow'
                : 'border-gray-300 bg-gray-50 text-gray-700'
            }`}
          >
            {store.storeName}
          </div>
        ))}
      </div>

      <div className="mt-8 w-full max-w-md gap-3 pb-3">
        <input
          type="number"
          value={amount}
          onChange={(e) => setAmount(Number(e.target.value))}
          placeholder="가격 입력"
          className="w-full border border-gray-300 rounded p-2 mb-2"
        />
        <button
          onClick={() => createPaymentTransaction()}
          className="w-full px-4 py-2 bg-green-600 text-white rounded"
        >
          create
        </button>
      </div>

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

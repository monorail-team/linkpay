// useFcm.ts (또는 App.tsx 내 useEffect)
import { messaging } from '../firebase';
import { getToken, onMessage } from 'firebase/messaging';
import axios from 'axios';
import { useEffect } from 'react';


const VAPID_KEY = process.env.REACT_APP_VAPID_KEY;
const base_url = process.env.REACT_APP_API_URL;

export const useFcm = async () => {
  useEffect(() => {
    // 앱 로드시 알림 권한 요청 + 토큰 발급
    requestNotificationPermission();

    // 포그라운드 메시지 수신
    onMessage(messaging, (payload) => {
      console.log('📩 메시지 수신 (Foreground): ', payload);
      alert(`🔔 ${payload.notification?.title} - ${payload.notification?.body}`);
    });
  }, []);

  const requestNotificationPermission = async () => {
    try {
      const permission = await Notification.requestPermission();
      if (permission !== 'granted') {
        console.warn('🚫 알림 권한 거부됨');
        return null;
      }
      console.log('Notification permission granted.');

      const fcmToken = await getToken(messaging, { vapidKey: VAPID_KEY });
      console.log('FCM token:', fcmToken);
      const accessToken = sessionStorage.getItem('accessToken');
      // 서버에 token 보내기
      await axios.post(`${base_url}/api/fcm/register?token=${fcmToken}`,
        null,
        {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${accessToken}`
          }
        }
      );

      return fcmToken;

    } catch (err) {
      console.error('FCM 등록 실패', err);
      return null;
    }
  };
};

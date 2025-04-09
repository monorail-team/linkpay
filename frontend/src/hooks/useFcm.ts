import { messaging } from '../firebase';
import { getToken, isSupported, onMessage } from 'firebase/messaging';
import axios from 'axios';
import { useEffect } from 'react';

const VAPID_KEY = process.env.REACT_APP_VAPID_KEY;
const base_url = process.env.REACT_APP_API_URL;
const DEVICE_ID_KEY = 'deviceId';
const FCM_TOKEN_KEY = 'fcmToken';
const EXPIRE_KEY = 'fcmTokenExpiresAt';

export const useFcm = () => {

  const setupFcm = async () => {

    const supported = await isSupported();
    if (!supported) {
      console.warn('🚫 FCM을 지원하지 않는 브라우저입니다.');
    }

    const accessToken = sessionStorage.getItem('accessToken');

    if (!accessToken) {
      console.log('🔒 로그인되어 있지 않아 FCM 등록 생략');
      return;
    }
    requestNotificationPermission(accessToken);
    // 포그라운드 메시지 수신
    onMessage(messaging, (payload) => {
      console.log('📩 메시지 수신 (Foreground): ', payload);
      alert(`🔔 ${payload.notification?.title} - ${payload.notification?.body}`);
    });
  };
  useEffect(() => {
    setupFcm();
  }, []);

  const requestNotificationPermission = async (accessToken: string) => {
    try {
      const permission = await Notification.requestPermission();
      if (permission !== 'granted') {
        console.warn('🚫 알림 권한 거부됨');
        return;
      }
      console.log('Notification permission granted.');

      // deviceId 식별
      let deviceId = sessionStorage.getItem(DEVICE_ID_KEY);
      if (!deviceId) {
        deviceId = crypto.randomUUID(); // 브라우저 내장 UUID 생성기
        sessionStorage.setItem(DEVICE_ID_KEY, deviceId);
        console.log('🆕 deviceId 생성:', deviceId);
      } else {
        console.log('📱 기존 deviceId 사용:', deviceId);
      }

      const fcmToken = await getToken(messaging, { vapidKey: VAPID_KEY });
      console.log('FCM token:', fcmToken);
      const storedToken = sessionStorage.getItem(FCM_TOKEN_KEY);

      const storedExpiresAt = sessionStorage.getItem(EXPIRE_KEY);
      const now = new Date();
      const isExpired = !storedExpiresAt || new Date(storedExpiresAt) < now;
      // 기존 토큰과 다른 경우만 서버에 등록
      if (fcmToken === storedToken && !isExpired) {
        console.log('✅ 기존과 동일한/유효한 FCM 토큰 — 등록 생략');
        return;
      }

      const expiresAt = new Date(Date.now() + 1000 * 60 * 60 * 24 * 30).toISOString(); // 30일 후

      // 서버에 token 보내기
      await axios.put(`${base_url}/api/fcm/register`,
        {
          token: fcmToken,
          deviceId: deviceId,
          expiresAt: expiresAt
        },
        {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${accessToken}`
          }
        }
      );

      sessionStorage.setItem(FCM_TOKEN_KEY, fcmToken);
      sessionStorage.setItem(EXPIRE_KEY, expiresAt);
      console.log('✅ FCM 등록 완료 (만료일:', expiresAt, ')');
    } catch (err) {
      console.error('FCM 등록 실패', err);
    }
  };
};

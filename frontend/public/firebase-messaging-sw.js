/* eslint-disable no-undef */
importScripts('https://www.gstatic.com/firebasejs/9.6.1/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/9.6.1/firebase-messaging-compat.js');

firebase.initializeApp({
  apiKey: 'AIzaSyCYV-wUTq-EnnnUdrakzL60R7TOpcu_zMw',
  projectId: 'linkpay-message',
  messagingSenderId: '126207486181',
  appId: '1:126207486181:web:c4dfe37acbe2bf5dc6cc0c'
});

const messaging = firebase.messaging();

messaging.onBackgroundMessage(function(payload) {
  console.log('[firebase-messaging-sw.js] 백그라운드 메시지 수신:', payload);

  const { title, body } = payload.notification;

  self.registration.showNotification(title, {
    body,
    // icon: '/icon.png',
    // badge: '/badge.png',
    vibrate: [200, 100, 200],
    requireInteraction: true
  });
});

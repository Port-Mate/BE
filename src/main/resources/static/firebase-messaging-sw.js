importScripts("https://www.gstatic.com/firebasejs/12.2.1/firebase-app-compat.js");
importScripts("https://www.gstatic.com/firebasejs/12.2.1/firebase-messaging-compat.js");

firebase.initializeApp({
    apiKey: "AIzaSyDwHKbyXY2YqiV1dwClKArYz8NqOLPJH3o",
    authDomain: "fcmtest-2eeed.firebaseapp.com",
    projectId: "fcmtest-2eeed",
    storageBucket: "fcmtest-2eeed.appspot.com",
    messagingSenderId: "903263294981",
    appId: "1:903263294981:web:c7c45f4fdd279a17deee6c"
});

const messaging = firebase.messaging();

messaging.onBackgroundMessage((payload) => {
    console.log("백그라운드 메시지:", payload);

    self.registration.showNotification(payload.notification.title, {
        body: payload.notification.body,
        icon: "https://www.gstatic.com/webp/gallery/4.sm.jpg" // ✅ 외부 아이콘
    });
});

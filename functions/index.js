const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

// Create an HTTP-triggered function to save FCM token
exports.saveFCMToken = functions.https.onRequest((request, response) => {
  // Ensure the request is a POST
  if (request.method !== "POST") {
    return response.status(405).send("Method Not Allowed");
  }

  // Parse the body to extract the FCM token
  const token = request.body.fcmToken;

  // Check if the token is provided
  if (!token) {
    return response.status(400).send("FCM Token is missing");
  }

  // Save the token in Firestore (or Realtime Database if you prefer)
  const db = admin.firestore();
  db.collection("fcmTokens").add({
    token: token,
    timestamp: admin.firestore.FieldValue.serverTimestamp(),
  })
      .then(() => {
        return response.status(200).send("Token saved successfully");
      })
      .catch((error) => {
        return response.status(500).send("Error saving token: " );
      });
});
